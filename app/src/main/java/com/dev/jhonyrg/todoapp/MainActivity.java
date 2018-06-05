package com.dev.jhonyrg.todoapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.qbusict.cupboard.QueryResultIterable;
import utils.RecyclerViewAdapter;
import utils.ToDo;
import utils.ToDoHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rvToDo)
    RecyclerView rvToDo;

    private RecyclerViewAdapter adapter;
    private List<ToDo> toDoList;
    static SQLiteDatabase db;
    ToDoHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        dbHelper = new ToDoHelper(this);
        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 1, 2);

        this.toDoList = new ArrayList<ToDo>();
        this.adapter = new RecyclerViewAdapter(toDoList, R.layout.item_view, new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ToDo itemToDo, int position) {
                Toast.makeText(MainActivity.this, "Item "+ position, Toast.LENGTH_SHORT).show();
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
        this.adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.fabAdd)
    public void ClickAction()
    {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        startActivity(intent);
    }

    private void fillList() {
        db = dbHelper.getReadableDatabase();

        ToDo toDo = cupboard().withDatabase(db).query(ToDo.class).get();
        Cursor bunnies = cupboard().withDatabase(db).query(ToDo.class).getCursor();

        try {
            // Iterar
            QueryResultIterable<ToDo> itr = cupboard().withCursor(bunnies).iterate(ToDo.class);

            for (ToDo tempToDo : itr) {
                this.toDoList.add(tempToDo);
            }
        } finally {

            // cerrar el cursor
            bunnies.close();
        }

        //cerrar la base de datos
        db.close();
    }
}
