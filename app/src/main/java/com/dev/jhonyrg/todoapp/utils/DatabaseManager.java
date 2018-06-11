package com.dev.jhonyrg.todoapp.utils;

import android.database.sqlite.SQLiteDatabase;
import nl.qbusict.cupboard.DatabaseCompartment;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by gerardo.calderon on 16/12/2015.
 * Database manager to prevent access errors
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private static ToDoHelper mDatabaseHelper;
    private static SQLiteDatabase mDatabase;
    private static int mOpenCounter = 0;

    public static synchronized void initializeInstance(ToDoHelper helper) {
        if (instance == null) {
            instance = new DatabaseManager();
            mDatabaseHelper = helper;
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initialize(..) method first.");
        }

        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        mOpenCounter++;
        if(mOpenCounter == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        mOpenCounter--;
        if(mOpenCounter == 0) {
            // Closing database
            mDatabase.close();
        }
    }
}
