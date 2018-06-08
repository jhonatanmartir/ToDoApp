package com.dev.jhonyrg.todoapp;

import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import utils.ToDo;
import utils.ToDoHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class EditActivity extends AppCompatActivity {
    public static final int CREATE = 1;
    public static final int UPDATE = 2;
    public static final String REG_ID = "id";
    public static final String OPERATION = "operation";

    @NotEmpty(messageId = R.string.error, order = 1)
    @MinLength(value = 3, messageId = R.string.error, order = 2)
    @BindView(R.id.etxtTitle) public MaterialEditText title;

    @NotEmpty(messageId = R.string.error, order = 1)
    @MinLength(value = 4, messageId = R.string.error, order = 2)
    @BindView(R.id.etxtDescription) public MaterialEditText description;

    //SQLiteDatabase db;
    ToDoHelper dbHelper;

    int action;
    String registerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ButterKnife.bind(this);

        dbHelper = new ToDoHelper(this);

        this.action = getIntent().getIntExtra(OPERATION, 0);
        if(this.action == UPDATE)
        {
            this.registerId = getIntent().getStringExtra(REG_ID);

            MainActivity.db = dbHelper.getReadableDatabase();
            ToDo toDo = cupboard().withDatabase(MainActivity.db).get(ToDo.class, Long.valueOf(registerId));
            MainActivity.db.close();

            this.title.setText(toDo.titulo);
            this.description.setText(toDo.descripcion);
        }

    }

    @OnClick(R.id.btnSave)
    public void clickSave()
    {
        if(FormValidator.validate(this, new SimpleErrorPopupCallback(this)))
        {
            try
            {
                switch (action)
                {
                    case CREATE:
                        Insert();
                        break;

                    case UPDATE:
                        Update();
                        this.finish();
                        break;
                }

            }
            catch (SQLException ex)
            {
                MainActivity.db.close();
            }
        }
    }

    @OnClick(R.id.btnCancel)
    public void clickCancel()
    {
        finish();
    }

    public static void newRegister(AppCompatActivity activity)
    {
        Intent intent = new Intent(activity, EditActivity.class);
        intent.putExtra(OPERATION, CREATE);
        activity.startActivity(intent);
    }

    public static void editRegister(AppCompatActivity activity, String id)
    {
        Intent intent = new Intent(activity, EditActivity.class);
        intent.putExtra(OPERATION, UPDATE);
        intent.putExtra(REG_ID, id);
        activity.startActivity(intent);
    }

    private void Insert()
    {
        MainActivity.db = dbHelper.getWritableDatabase();
        ToDo toDo = new ToDo();
        toDo.titulo = title.getText().toString();
        toDo.descripcion = description.getText().toString();
        toDo.fecha = Calendar.getInstance().getTime().toString();

        cupboard().withDatabase(MainActivity.db).put(toDo);
        MainActivity.db.close();

        Toast.makeText(EditActivity.this, "Inserción", Toast.LENGTH_SHORT).show();
        title.setText("");
        description.setText("");
    }

    private void Update()
    {
        MainActivity.db = dbHelper.getWritableDatabase();
        ToDo item = new ToDo();

        item._id = Long.valueOf(this.registerId);
        item.titulo = title.getText().toString();
        item.descripcion = description.getText().toString();

        cupboard().withDatabase(MainActivity.db).put(item);
        MainActivity.db.close();
    }
}
