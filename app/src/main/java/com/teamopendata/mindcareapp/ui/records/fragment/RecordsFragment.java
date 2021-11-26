package com.teamopendata.mindcareapp.ui.records.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.teamopendata.mindcareapp.R;
import com.teamopendata.mindcareapp.common.model.entity.Record;
import com.teamopendata.mindcareapp.databinding.FragmentRecordsBinding;
import com.teamopendata.mindcareapp.ui.records.FragmentFactoryImpl;
import com.teamopendata.mindcareapp.ui.records.listener.OnAddEditRecordClickListener;
import com.teamopendata.mindcareapp.ui.records.listener.OnAddEditRecordEventListener;


public class RecordsFragment extends Fragment implements OnAddEditRecordClickListener {
    public static final String TAG = "RecordsFragment";
    private FragmentRecordsBinding binding;

    FragmentFactoryImpl fragmentFactory;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        fragmentFactory = new FragmentFactoryImpl();
        getParentFragmentManager().setFragmentFactory(fragmentFactory);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_records, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentFactory.setOnAddEditRecordClickListener(this);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.fl_records_container, getParentFragmentManager().getFragmentFactory().instantiate(
                ClassLoader.getSystemClassLoader(), HomeRecordsFragment.class.getName())).commit();
    }

    @Override
    public void onAddEditRecordClick(AddEditRecordFragment.EventType type, Record record, OnAddEditRecordEventListener listener) {
        fragmentFactory.setEvent(type).setData(record, listener);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_records_container,
                getParentFragmentManager().getFragmentFactory().instantiate(
                        ClassLoader.getSystemClassLoader(), AddEditRecordFragment.class.getName())
        ).addToBackStack(AddEditRecordFragment.class.getName()).commit();
    }
}
