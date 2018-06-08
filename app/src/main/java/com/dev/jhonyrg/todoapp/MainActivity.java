package com.dev.jhonyrg.todoapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.RecyclerViewAdapter;
import utils.ToDo;
import utils.ToDoHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends AppCompatActivity {
    public static SQLiteDatabase db;
    private RecyclerViewAdapter adapter;
    private List<ToDo> toDoList;
    private ToDoHelper dbHelper;

    @BindView(R.id.rvToDo)
    RecyclerView rvToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        dbHelper = new ToDoHelper(this);
        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 1, 2);

        this.toDoList = new ArrayList<>();
        this.adapter = new RecyclerViewAdapter(toDoList, R.layout.item_view, new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ToDo itemToDo, int position) {
                EditActivity.editRegister(MainActivity.this, String.valueOf(itemToDo._id));
            }
        }, new RecyclerViewAdapter.OnItemLogClickListener() {
            @Override
            public void onItemLongClick(ToDo itemToDo, int position) {
                //Toast.makeText(MainActivity.this, "Item" + position, Toast.LENGTH_SHORT).show();
            }
        });

        this.rvToDo.setLayoutManager(new LinearLayoutManager(this));
        this.rvToDo.setAdapter(this.adapter);
        this.rvToDo.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.fillList();
    }

    @OnClick(R.id.fabAdd)
    public void ClickAction()
    {
        EditActivity.newRegister(MainActivity.this);
    }

    private void fillList() {
        db = dbHelper.getReadableDatabase();
        //Cursor bunnies = cupboard().withDatabase(db).query(ToDo.class).getCursor();

        try {
            //QueryResultIterable<ToDo> itr = cupboard().withDatabase(db).query(ToDo.class).query();
            List<ToDo> itr = cupboard().withDatabase(db).query(ToDo.class).list();

            this.toDoList.clear();
            for (ToDo tempToDo : itr) {
                this.toDoList.add(tempToDo);
            }
        } finally {
            db.close();
            this.adapter.notifyDataSetChanged();
        }
    }
}
