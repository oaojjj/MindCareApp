package com.teamopendata.mindcareapp.ui.records.listener;

import com.teamopendata.mindcareapp.common.model.entity.Record;
import com.teamopendata.mindcareapp.ui.records.fragment.AddEditRecordFragment.EventType;

public interface OnAddEditRecordClickListener {
    void onAddEditRecordClick(EventType type, Record record, OnAddEditRecordEventListener listener);
}
