package com.teamopendata.mindcareapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.teamopendata.mindcareapp.common.converters.Converters;
import com.teamopendata.mindcareapp.common.model.dao.RecordDao;
import com.teamopendata.mindcareapp.common.model.entity.Record;
import com.teamopendata.mindcareapp.ui.map.MedicalInstitution;
import com.teamopendata.mindcareapp.ui.map.MedicalInstitutionDao;

@Database(entities = {MedicalInstitution.class, Record.class}, version = 1,exportSchema = false)
@TypeConverters(Converters.class)
public abstract class MindChargeDB extends RoomDatabase {

    private static MindChargeDB INSTANCE = null;

    public MedicalInstitutionDao medicalInstitutionDao() {
        return null;
    }

    public abstract RecordDao getRecordDao();


    public static MindChargeDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    MindChargeDB.class, "medicalinstitution.db").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
