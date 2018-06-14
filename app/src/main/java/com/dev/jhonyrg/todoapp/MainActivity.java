package com.dev.jhonyrg.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.dev.jhonyrg.todoapp.utils.DB;
import com.dev.jhonyrg.todoapp.utils.RecyclerViewAdapter;
import com.dev.jhonyrg.todoapp.items.ToDo;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener, RecyclerViewAdapter.OnItemLogClickListener{
    static final int TODO_REQUEST = 11;
    private RecyclerViewAdapter adapter;
    private List<ToDo> toDoList;
    private DB db;

    @BindView(R.id.rvToDo) RecyclerView rvToDo;
    LinearLayout layoutDelete;
    ImageButton buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        this.toDoList = new ArrayList<>();
        this.adapter = new RecyclerViewAdapter(toDoList, R.layout.item_view, this, this);

        this.rvToDo.setLayoutManager(new LinearLayoutManager(this));
        this.rvToDo.setAdapter(this.adapter);
        this.rvToDo.setItemAnimator(new DefaultItemAnimator());
        registerForContextMenu(this.rvToDo);

        this.fillList();
    }

    private void fillList() {
        this.db = new DB(this);

        try {
            List<ToDo> itr = this.db.cb().query(ToDo.class).list();
            this.toDoList.clear();

            for (ToDo tempToDo : itr) {
                this.toDoList.add(tempToDo);
            }
        } finally {
            this.db.close();
            this.adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TODO_REQUEST)
        {
            if(resultCode == RESULT_OK)
            {
                this.fillList();
            }
        }
    }

    @OnClick(R.id.fabAdd)
    public void ClickAction()
    {
        EditActivity.newRegister(MainActivity.this);
    }

    @Override
    public void onItemLongClick(final ToDo itemToDo, int position, View view) {
        layoutDelete = view.findViewById(R.id.lytDelete);
        buttonDelete = view.findViewById(R.id.ibtnDelete);
        if(layoutDelete.getVisibility() == View.GONE)
        {
            layoutDelete.setVisibility(View.VISIBLE);
        }
        else
        {
            layoutDelete.setVisibility(View.GONE);
        }

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                db = new DB(MainActivity.this);
                db.cb().delete(itemToDo);
                db.close();
                fillList();
            }
        });
    }

    @Override
    public void onItemClick(ToDo itemToDo, int position, View view) {
        layoutDelete = view.findViewById(R.id.lytDelete);
        buttonDelete = view.findViewById(R.id.ibtnDelete);

        if(layoutDelete.getVisibility() == View.VISIBLE)
        {
            layoutDelete.setVisibility(View.GONE);
        }
        else
        {
            EditActivity.editRegister(MainActivity.this, String.valueOf(itemToDo._id));
        }

    }
}
