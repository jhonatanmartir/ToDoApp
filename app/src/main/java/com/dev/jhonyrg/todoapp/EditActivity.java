package com.dev.jhonyrg.todoapp;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

    @NotEmpty(messageId = R.string.error, order = 1)
    @MinLength(value = 3, messageId = R.string.error, order = 2)
    @BindView(R.id.etxtTitle) public MaterialEditText title;

    @NotEmpty(messageId = R.string.error, order = 1)
    @MinLength(value = 3, messageId = R.string.error, order = 2)
    @BindView(R.id.etxtDescription) public MaterialEditText description;

    //static SQLiteDatabase db;
    ToDoHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ButterKnife.bind(this);

        dbHelper = new ToDoHelper(this);
        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 1, 2);

    }

    @OnClick(R.id.btnSave)
    public void clickSave()
    {
        if(FormValidator.validate(this, new SimpleErrorPopupCallback(this)))
        {
            try
            {
                MainActivity.db = dbHelper.getWritableDatabase();
                ToDo toDo = new ToDo();
                toDo.titulo = title.getText().toString();
                toDo.descripcion = description.getText().toString();
                toDo.fecha = Calendar.getInstance().getTime().toString();
                cupboard().withDatabase(MainActivity.db).put(toDo);

                title.setText("");
                description.setText("");
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
}
