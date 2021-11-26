package com.teamopendata.mindcareapp.ui.records.item;


import com.teamopendata.mindcareapp.common.model.entity.Record;
import com.teamopendata.mindcareapp.ui.records.adapter.RecordsAdapter.Type;
import com.teamopendata.mindcareapp.ui.records.adapter.RecordsAdapter.Header;

public class RecordItem {
    Object item;
    Type type;

    public RecordItem(Object item) {
        this.item = item;
        if (item instanceof Header) type = Type.TYPE_HEADER;
        else if (item instanceof Record) type = Type.TYPE_ITEM;
    }

    public RecordItem(Type type) {
        this.type = type;
    }

    public RecordItem(Object item, Type type) {
        this.item = item;
        this.type = type;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
