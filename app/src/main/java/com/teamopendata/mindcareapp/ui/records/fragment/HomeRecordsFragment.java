package com.teamopendata.mindcareapp.ui.records.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamopendata.mindcareapp.MindChargeDB;
import com.teamopendata.mindcareapp.R;
import com.teamopendata.mindcareapp.databinding.FragmentRecordsHomeBinding;
import com.teamopendata.mindcareapp.ui.records.adapter.RecordsAdapter;
import com.teamopendata.mindcareapp.ui.records.StickyHeaderItemDecoration;
import com.teamopendata.mindcareapp.ui.records.item.RecordItem;

import com.teamopendata.mindcareapp.ui.records.fragment.AddEditRecordFragment.EventType;
import com.teamopendata.mindcareapp.ui.records.listener.OnAddEditRecordClickListener;

import java.util.ArrayList;

public class HomeRecordsFragment extends Fragment {
    private static final String TAG = "HomeRecordsFragment";
    private FragmentRecordsHomeBinding binding;

    private RecordsAdapter mRecordsAdapter;

    private final OnAddEditRecordClickListener mListener;

    public HomeRecordsFragment(OnAddEditRecordClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        // getAll -> add recordItem
        ArrayList<RecordItem> items = new ArrayList<>();

        new Thread(() -> {
            MindChargeDB.getInstance(requireContext()).getRecordDao().getAll().forEach(record -> {
                Log.d(TAG, "task: " + record.toString());
                items.add(new RecordItem(record));
            });

            new Handler(Looper.getMainLooper()).post(() -> {
                binding.pbRecordsLoading.setVisibility(View.GONE);
                if (!items.isEmpty()) binding.tvTextStickyEmpty.setVisibility(View.GONE);
                mRecordsAdapter.initItems(items);
            });
        }).start();
        mRecordsAdapter = new RecordsAdapter(items, mListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_records_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        if (mRecordsAdapter.getItemCount() > 0) {
            binding.pbRecordsLoading.setVisibility(View.GONE);
            if (mRecordsAdapter.getItemCount() > 1)
                binding.tvTextStickyEmpty.setVisibility(View.GONE);
        }

        binding.rvRecordsList.setAdapter(mRecordsAdapter);
        binding.rvRecordsList.addItemDecoration(new StickyHeaderItemDecoration(mRecordsAdapter));
        binding.fabRecordAdd.setOnClickListener(v -> mListener.onAddEditRecordClick(EventType.EVENT_ADD, null, record -> {
            // TODO 아이템 추가가 끝나고 호출되는 콜백~
            mRecordsAdapter.addItem(record);
            binding.pbRecordsLoading.setVisibility(View.GONE);
            binding.tvTextStickyEmpty.setVisibility(View.GONE);
        }));
        binding.rvRecordsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) binding.fabRecordAdd.hide();
                else if (dy < 0) binding.fabRecordAdd.show();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: ");
        super.onDetach();
    }


}