package com.dev.jhonyrg.todoapp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import nl.qbusict.cupboard.DatabaseCompartment;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by gerardo.calderon on 24/11/2015.
 *
 *   DB db = new DB(this);
 *   List<ToDo> todos = db.cb().query(ToDo.class).list();
 */

public class DB {
    DatabaseManager manager;
    SQLiteDatabase db;

    public DB(Context context){
        DatabaseManager.initializeInstance(new ToDoHelper(context));
        this.manager = DatabaseManager.getInstance();
        this.db = manager.openDatabase();
    }

    public DatabaseCompartment cb(){
        return cupboard().withDatabase(this.db);
    }
    public SQLiteDatabase db(){ return this.db; }

    public void close(){
        this.manager.closeDatabase();
    }
}
