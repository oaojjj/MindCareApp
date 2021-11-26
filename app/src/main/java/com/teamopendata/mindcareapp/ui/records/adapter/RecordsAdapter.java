package com.teamopendata.mindcareapp.ui.records.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.teamopendata.mindcareapp.R;
import com.teamopendata.mindcareapp.ui.records.StickyHeaderItemDecoration;
import com.teamopendata.mindcareapp.ui.records.fragment.AddEditRecordFragment;
import com.teamopendata.mindcareapp.ui.records.listener.OnAddEditRecordClickListener;

import com.teamopendata.mindcareapp.common.model.entity.Record;
import com.teamopendata.mindcareapp.ui.records.item.RecordItem;
import com.teamopendata.mindcareapp.common.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecordsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderItemDecoration.StickyHeaderInterface {

    public enum Type {
        TYPE_TOP_HEADER,
        TYPE_HEADER,
        TYPE_ITEM
    }

    public static class Header {
        private final LocalDate localDate;
        private int position;

        public Header(LocalDate localDate, int position) {
            this.localDate = localDate;
            this.position = position;
        }

        @NonNull
        @Override
        public String toString() {
            return "Header{" +
                    "localDate=" + localDate +
                    ", position=" + position +
                    '}';
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (getClass() != (obj != null ? obj.getClass() : null)) return false;
            return this.localDate.equals(((Header) obj).localDate);
        }
    }


    private static final String TAG = "RecordsAdapter";
    private final List<RecordItem> mItems;

    /**
     * 새로 추가되는 아이템의 위치를 header의 날짜와 비교해서 알맞은 위치에 추가한다.
     */
    private final List<Header> headers;

    private final OnAddEditRecordClickListener mAddEditRecordListener;

    private int cachedAddPosition = 0;
    private static long cachedRecordId = 1;

    public static long getCachedRecordId() {
        return cachedRecordId;
    }

    public int getCachedAddPosition() {
        return cachedAddPosition;
    }

    public RecordsAdapter(List<RecordItem> data, OnAddEditRecordClickListener addEditRecordListener) {
        headers = new ArrayList<>();
        mItems = new ArrayList<>();
        mAddEditRecordListener = addEditRecordListener;
        initHeaderAndItem(data);
    }

    private void initHeaderAndItem(List<RecordItem> data) {
        if (mItems.isEmpty())
            mItems.add(new RecordItem(Type.TYPE_TOP_HEADER));

        data.forEach(recordItem -> addHeaderAndItem((Record) recordItem.getItem()));
        Log.d(TAG, "addHeaderAndItem: headers(index) -> " + headers.toString());
    }

    private void addHeaderAndItem(Record newRecord) {
        if (headers.isEmpty()) {
            insertHeaderAndItem(mItems.size(), 0, newRecord);
            return;
        }

        // item insert
        for (Header header : headers) {
            LocalDate hDate = header.localDate;
            LocalDate rDate = newRecord.getDate();

            if (hDate.getYear() > rDate.getYear())
                continue;

            // 1. hYear == rYear
            if (hDate.getYear() == rDate.getYear()) {
                // 1.1 hMonth == rMonth -> header 밑으로 아이템 비교 후 삽입
                if (hDate.getMonthValue() == rDate.getMonthValue()) {
                    int index = headers.indexOf(header) + 1;
                    int target;

                    if (index >= headers.size()) target = mItems.size();
                    else target = headers.get(index).position;

                    for (int i = header.position + 1; i < target; i++) {
                        LocalDate recordDate = ((Record) mItems.get(i).getItem()).getDate();

                        if (recordDate.compareTo(newRecord.getDate()) <= 0) {
                            insertItem(newRecord, i);
                            headerIncrementPos(header.position, 1);
                            return;
                        }
                    }
                    insertItem(newRecord, target);
                    headerIncrementPos(headers.indexOf(header), 1);
                    return;
                }
                // 1.2 hMonth > rMonth -> header 자리에 새로운 header 추가 후 아이템 추가
                else if (hDate.getMonthValue() < rDate.getMonthValue()) {
                    insertHeaderAndItem(header.position, headers.indexOf(header), newRecord);
                    return;
                }
                // 2. hYear > rYear -> header 자리에 새로운 header 추가 후 아이템 추가
            } else if (hDate.getYear() < rDate.getYear() || hDate.getMonthValue() < rDate.getMonthValue()) {
                insertHeaderAndItem(header.position, headers.indexOf(header), newRecord);
                return;
            }
        }
        // 3. 일치하는 header 없음 -> 맨 마지막에 추가
        insertHeaderAndItem(mItems.size(), headers.size(), newRecord);
    }

    private void insertItem(Record newRecord, int i) {
        mItems.add(i, new RecordItem(newRecord));
        cachedAddPosition = i;
        cachedRecordId = newRecord.getId() > cachedRecordId ? newRecord.getId() + 1 : cachedRecordId + 1;
        Log.d(TAG, "insertItem: " + cachedRecordId);
    }

    private void insertHeaderAndItem(int position, int listPosition, Record newRecord) {
        if (!headers.isEmpty()) headerIncrementPos(listPosition, 2);
        int itemPos = insertHeader(position, newRecord);
        insertItem(newRecord, itemPos);
    }

    private int insertHeader(int position, Record record) {
        Header header =
                new Header(LocalDate.of(record.getDate().getYear(), record.getDate().getMonthValue(), 1), position);
        mItems.add(position, new RecordItem(header));
        headers.add(header);
        headers.sort((o1, o2) -> o2.localDate.compareTo(o1.localDate));
        return header.position + 1;
    }

    private void headerIncrementPos(int pos, int value) {
        for (int i = pos; i < headers.size(); i++) headers.get(i).position += value;
    }

    private void headerDecrementPos(int pos, int value) {
        for (int i = pos; i < headers.size(); i++) headers.get(i).position -= value;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Type.TYPE_TOP_HEADER.ordinal()) {
            return new TopHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_records_top_header, parent, false));
        } else if (viewType == Type.TYPE_HEADER.ordinal()) {
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_records_header, parent, false));
        } else {
            return new RecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TopHeaderViewHolder) {
            ((TopHeaderViewHolder) holder).bind();
        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind((Header) mItems.get(position).getItem(), position);
        } else {
            ((RecordViewHolder) holder).bind((Record) mItems.get(position).getItem(), position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType().ordinal();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void initItems(ArrayList<RecordItem> items) {
        initHeaderAndItem(items);
        this.notifyDataSetChanged();
    }

    public void addItem(Record newRecord) {
        addHeaderAndItem(newRecord);
        notifyItemInserted(cachedAddPosition);
    }

    @Override
    public boolean isHeader(int position) {
        return mItems.get(position).getType() == Type.TYPE_HEADER;
    }

    @Override
    public View getHeaderLayoutView(RecyclerView parent, int position) {
        View header = LayoutInflater.from(parent.getContext()).inflate(getHeaderLayout(position), parent, false);
        if (mItems.get(position).getType() != Type.TYPE_TOP_HEADER)
            bindHeaderData(header, position);
        return header;
    }

    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        int headerPosition = 0;
        do {
            if (isHeader(itemPosition)) {
                headerPosition = itemPosition;
                break;
            }
            itemPosition -= 1;
        } while (itemPosition >= 0);
        return headerPosition;
    }

    @Override
    public void bindHeaderData(View view, int headerPosition) {
        String year, month;

        if (mItems.get(headerPosition).getType() == Type.TYPE_HEADER) {
            Header header = ((Header) mItems.get(headerPosition).getItem());
            year = header.localDate.getYear() + "년";
            month = header.localDate.getMonthValue() + "월";
        } else {
            Record record = ((Record) mItems.get(headerPosition).getItem());
            year = record.getDate().getYear() + "년";
            month = record.getDate().getMonthValue() + "월";
        }

        view.findViewById(R.id.tv_text_sticky).setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.tv_record_item_year)).setText(year);
        ((TextView) view.findViewById(R.id.tv_record_item_month)).setText(month);
    }

    @Override
    public int getHeaderLayout(int headerPosition) {
        if (mItems.get(headerPosition).getType() == Type.TYPE_TOP_HEADER)
            return R.layout.item_records_top_header;
        else {
            return R.layout.item_records_header;
        }
    }


    private class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayNumber, tvDayText, tvRecordTitle;
        ImageButton btnRecordEdit;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayNumber = itemView.findViewById(R.id.tv_record_day_number);
            tvDayText = itemView.findViewById(R.id.tv_record_day_text);
            tvRecordTitle = itemView.findViewById(R.id.tv_record_title);
            btnRecordEdit = itemView.findViewById(R.id.btn_record_edit);
        }

        public void bind(Record record, int position) {
            Log.d(TAG, "bind: " + record.toString());
            tvDayNumber.setText(String.valueOf(record.getDate().getDayOfMonth()));
            tvDayText.setText(Utils.getDayString(record.getDate().getDayOfWeek()));
            tvRecordTitle.setText(record.getTitle());

            btnRecordEdit.setOnClickListener(v -> mAddEditRecordListener.onAddEditRecordClick(
                    AddEditRecordFragment.EventType.EVENT_EDIT, record,
                    r -> {
                        // TODO 아이템 수정 끝나고 호출 되는 콜백~
                        if (r == null) {
                            removeItem(position);
                        } else {
                            removeItem(position);
                            addItem(r);
                        }
                    }
            ));
        }
    }

    private void removeItem(int position) {
        mItems.remove(position);
        notifyHeader(position);
    }

    private void notifyHeader(int position) {
        Header prevHeader = headers.get(0);
        if (headers.size() > 1) {
            for (Header header : headers) {
                if (header.position >= position) {
                    headerDecrementPos(headers.indexOf(prevHeader) + 1, 1);
                    if (header.position - prevHeader.position == 1)
                        removeHeader(prevHeader);
                    return;
                }
                prevHeader = header;
            }
            if (mItems.size() - prevHeader.position == 1) {
                removeHeader(prevHeader);
            }
        } else {
            if (mItems.size() < 2) {
                headers.remove(0);
                mItems.remove(1);
                notifyItemRemoved(1);
            }
        }
    }

    private void removeHeader(Header header) {
        mItems.remove(header.position);
        headerDecrementPos(headers.indexOf(header), 1);
        headers.remove(header);
        notifyItemRemoved(header.position);
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvYear, tvMonth, tvStickyText;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvYear = itemView.findViewById(R.id.tv_record_item_year);
            tvMonth = itemView.findViewById(R.id.tv_record_item_month);
            tvStickyText = itemView.findViewById(R.id.tv_text_sticky);
        }

        public void bind(Header header, int position) {
            tvStickyText.setVisibility(position == 1 ? View.VISIBLE : View.GONE);

            String year = header.localDate.getYear() + "년";
            String month = header.localDate.getMonthValue() + "월";

            tvYear.setText(year);
            tvMonth.setText(month);
        }
    }

    private static class TopHeaderViewHolder extends RecyclerView.ViewHolder {

        public TopHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind() {
        }
    }
}
