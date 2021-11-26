package com.teamopendata.mindcareapp.ui.records;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import com.teamopendata.mindcareapp.common.model.entity.Record;
import com.teamopendata.mindcareapp.ui.records.fragment.AddEditRecordFragment;
import com.teamopendata.mindcareapp.ui.records.fragment.HomeRecordsFragment;
import com.teamopendata.mindcareapp.ui.records.listener.OnAddEditRecordClickListener;
import com.teamopendata.mindcareapp.ui.records.listener.OnAddEditRecordEventListener;

public class FragmentFactoryImpl extends FragmentFactory {

    private AddEditRecordFragment.EventType mType;
    private Record mData;
    private OnAddEditRecordEventListener mClickListener;
    private OnAddEditRecordClickListener mSaveListener;

    @NonNull
    @Override
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
        if (className.equals(AddEditRecordFragment.class.getName())) {
            return new AddEditRecordFragment(mType, mData, mClickListener);
        } else if (className.equals(HomeRecordsFragment.class.getName())) {
            return new HomeRecordsFragment(mSaveListener);
        } else {
            return super.instantiate(classLoader, className);
        }
    }

    public FragmentFactoryImpl setEvent(AddEditRecordFragment.EventType type) {
        mType = type;
        return this;
    }

    public void setData(Record record, OnAddEditRecordEventListener listener) {
        mData = record;
        mClickListener = listener;
    }

    public void setOnAddEditRecordClickListener(OnAddEditRecordClickListener listener) {
        mSaveListener = listener;
    }
}
