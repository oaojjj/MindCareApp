package com.teamopendata.mindcareapp.ui.records.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.teamopendata.mindcareapp.MindChargeDB;
import com.teamopendata.mindcareapp.R;
import com.teamopendata.mindcareapp.databinding.FragmentAddEditRecordBinding;
import com.teamopendata.mindcareapp.common.model.entity.Record;
import com.teamopendata.mindcareapp.ui.records.adapter.RecordsAdapter;
import com.teamopendata.mindcareapp.ui.records.adapter.TaskAdapter;
import com.teamopendata.mindcareapp.common.model.entity.Task;
import com.teamopendata.mindcareapp.common.Utils;
import com.teamopendata.mindcareapp.ui.records.listener.OnAddEditRecordEventListener;

import java.time.LocalDate;
import java.util.ArrayList;

public class AddEditRecordFragment extends Fragment {
    public enum EventType {
        EVENT_ADD,
        EVENT_EDIT;

        @NonNull
        @Override
        public String toString() {
            return this.name();
        }
    }

    private static final String TAG = "AddEditRecordFragment";
    private FragmentAddEditRecordBinding binding;
    private TaskAdapter taskAdapter;

    private final EventType mEventType;

    private Toast toastEvent;

    private Record mNewRecord;
    private final Record mCachedRecord;

    /**
     * @see HomeRecordsFragment called after being saved record
     */
    private final OnAddEditRecordEventListener mSaveListener;

    public AddEditRecordFragment(EventType type, Record record, OnAddEditRecordEventListener listener) {
        mEventType = type;
        mCachedRecord = record;
        if (mEventType == EventType.EVENT_EDIT) mNewRecord = record.clone();
        else mNewRecord = new Record();
        mSaveListener = listener;

        Log.d(TAG, "AddEditRecordFragment: " + "eventType-> " + mEventType.toString() + "Record-> " + mNewRecord.toString());
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit_record, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toastEvent = Utils.buildSaveToast(view, requireContext(), getLayoutInflater());

        ArrayList<Task> item = new ArrayList<>();

        if (mEventType == EventType.EVENT_ADD) {
            mNewRecord = new Record("", LocalDate.now());
            binding.btnRecordDelete.setVisibility(View.GONE);
        } else {
            item.addAll(mNewRecord.getTasks());
        }
        taskAdapter = new TaskAdapter(item);
        binding.includeRv.rvRecordTask.setAdapter(taskAdapter);
        binding.includeRv.rvRecordTask.addItemDecoration(new DividerItemDecoration(requireContext(), 1));

        binding.includeRv.btnTaskAdd.setOnClickListener(v -> {
            taskAdapter.addTask(new Task());
            taskAdapter.notifyItemChanged(taskAdapter.getItemCount());
        });

        binding.btnRecordSave.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        binding.btnRecordDelete.setOnClickListener(v -> deleteRecord());

        binding.tvRecordPickDate.setText(Utils.LocalDateToString(mNewRecord.getDate()));
        binding.etRecordTitle.setText(mNewRecord.getTitle());

        binding.cvRecordDate.setOnClickListener(v -> showDatePickerDialog());

    }


    private void addRecord() {
        if (newRecord()) {
            showToast();
            new Thread(() -> {
                Log.d(TAG, "addRecord: " + mNewRecord.toString());
                mNewRecord.setId(RecordsAdapter.getCachedRecordId());
                MindChargeDB.getInstance(requireContext()).getRecordDao().insert(mNewRecord);
                mSaveListener.onPerformEvent(mNewRecord);
            }).start();
        }
    }

    private void updateRecord() {
        if (newRecord() && !mNewRecord.equals(mCachedRecord)) {
            Toast.makeText(requireContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
            new Thread(() -> {
                Log.d(TAG, "updateRecord: " + mNewRecord.toString());

                MindChargeDB.getInstance(requireContext()).getRecordDao().update(mNewRecord);
                mSaveListener.onPerformEvent(mNewRecord);
            }).start();

        }
    }

    private void deleteRecord() {
        getParentFragmentManager().popBackStack();
        new Thread(() -> {
            Log.d(TAG, "deleteRecord: " + mNewRecord.toString());

            MindChargeDB.getInstance(requireContext()).getRecordDao().delete(mNewRecord);
            mSaveListener.onPerformEvent(null);
        }).start();
        Toast.makeText(requireContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void showToast() {
        new CountDownTimer(500, 100) {
            public void onTick(long millisUntilFinished) {
                toastEvent.show();
            }

            public void onFinish() {
                toastEvent.cancel();
            }
        }.start();
    }

    /**
     * DatePickerListener
     */
    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mNewRecord.setDate(LocalDate.of(year, month + 1, dayOfMonth));
            binding.tvRecordPickDate.setText(Utils.LocalDateToString(mNewRecord.getDate()));
        }
    };

    private void showDatePickerDialog() {
        LocalDate date = mNewRecord.getDate();
        new DatePickerDialog(requireContext(), mDateSetListener, date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth()).show();
    }

    private boolean newRecord() {
        if (!(binding.etRecordTitle.getText().toString().equals("")) && taskAdapter.getItemCount() != 0) {
            mNewRecord.setTitle(binding.etRecordTitle.getText().toString());
            mNewRecord.setDate(Utils.StringToLocalDate(binding.tvRecordPickDate.getText().toString()));
            mNewRecord.setTasks(taskAdapter.getItem());
            Log.d(TAG, "newTask:" + taskAdapter.getItem());
            Log.d(TAG, "newRecord: " + mNewRecord.toString());
            return true;
        }
        return false;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        if (mEventType == EventType.EVENT_ADD) addRecord();
        else updateRecord();
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {

        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: ");
        super.onDetach();
    }
}