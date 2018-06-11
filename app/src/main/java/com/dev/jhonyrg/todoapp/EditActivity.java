package com.dev.jhonyrg.todoapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import com.dev.jhonyrg.todoapp.utils.DB;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import com.dev.jhonyrg.todoapp.utils.ToDo;

public class EditActivity extends AppCompatActivity {
    public static final int CREATE = 1;
    public static final int UPDATE = 2;
    public static final String REG_ID = "id";
    public static final String OPERATION = "operation";
    static final int TODO_REQUEST = 11;

    @NotEmpty(messageId = R.string.error, order = 1)
    @MinLength(value = 3, messageId = R.string.error, order = 2)
    @BindView(R.id.etxtTitle) public MaterialEditText title;

    @NotEmpty(messageId = R.string.error, order = 1)
    @MinLength(value = 4, messageId = R.string.error, order = 2)
    @BindView(R.id.etxtDescription) public MaterialEditText description;

    @NotEmpty(messageId = R.string.error, order = 1)
    @MinLength(value = 4, messageId = R.string.error, order = 2)
    @BindView(R.id.etxtDate) public MaterialEditText date;

    int action;
    String registerId;
    Boolean insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ButterKnife.bind(this);

        this.insert = false;

        this.action = getIntent().getIntExtra(OPERATION, 0);
        if(this.action == UPDATE)
        {
            this.registerId = getIntent().getStringExtra(REG_ID);

            DB db = new DB(this);
            ToDo toDo = db.cb().get(ToDo.class, Long.valueOf(registerId));
            db.close();

            this.title.setText(toDo.titulo);
            this.description.setText(toDo.descripcion);
            this.date.setText(toDo.fecha);
        }

    }

    @OnClick(R.id.ibtnDate)
    public void clickPicker()
    {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int actualMonth = month +1;
                String dayFormat = (dayOfMonth < 10)? 0 + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String monthFormat = (actualMonth < 10)? 0 + String.valueOf(actualMonth):String.valueOf(actualMonth);
                date.setText(String.valueOf(dayFormat +"-"+monthFormat +"-"+ year));
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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

            }
        }
    }

    @OnClick(R.id.btnCancel)
    public void clickCancel()
    {
        if(insert)
        {
            finish();
        }
        else
        {
            Intent intent = getIntent();
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
        }
    }

    public static void newRegister(AppCompatActivity activity)
    {
        Intent intent = new Intent(activity, EditActivity.class);
        intent.putExtra(OPERATION, CREATE);
        activity.startActivityForResult(intent, TODO_REQUEST);
    }

    public static void editRegister(AppCompatActivity activity, String id)
    {
        Intent intent = new Intent(activity, EditActivity.class);
        intent.putExtra(OPERATION, UPDATE);
        intent.putExtra(REG_ID, id);
        activity.startActivityForResult(intent, TODO_REQUEST);
    }

    private void Insert()
    {
        ToDo toDo = new ToDo();
        toDo.titulo = title.getText().toString();
        toDo.descripcion = description.getText().toString();
        toDo.fecha = date.getText().toString();

        DB db = new DB(this);
        db.cb().put(toDo);
        db.close();

        Toast.makeText(EditActivity.this, "Inserción", Toast.LENGTH_SHORT).show();
        title.setText("");
        description.setText("");
        date.setText("");

        insert = true;
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
    }

    private void Update()
    {
        ToDo toDo = new ToDo();

        toDo._id = Long.valueOf(this.registerId);
        toDo.titulo = title.getText().toString();
        toDo.descripcion = description.getText().toString();
        toDo.fecha = date.getText().toString();

        DB db = new DB(this);
        db.cb().put(toDo);
        db.close();

        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
    }
}
