package utils;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dev.jhonyrg.todoapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<ToDo> toDoList;
    private int idLayout;
    private OnItemClickListener itemClickListener;
    private OnItemLogClickListener itemLongListener;

    public RecyclerViewAdapter(List<ToDo> toDo, int idLayout, OnItemClickListener itemClickListener, OnItemLogClickListener itemLongListener) {
        this.toDoList = toDo;
        this.idLayout = idLayout;
        this.itemClickListener = itemClickListener;
        this.itemLongListener = itemLongListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflar layout item_view
        View view = LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false);

        //Crear view Holder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setLongClickable(true);
        holder.bind(this.toDoList.get(position), itemClickListener, itemLongListener);
    }

    @Override
    public int getItemCount() {
        return this.toDoList.size();
    }

    //Listeners Interfaces
    public interface OnItemClickListener
    {
        void onItemClick(ToDo itemToDo, int position, ImageButton button);
    }

    public interface OnItemLogClickListener
    {
        void onItemLongClick(ToDo itemToDo, int position, ImageButton button);
    }

    //View Holder Class
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.txtvId)  TextView id;
        @BindView(R.id.txtvTitle)  TextView titulo;
        @BindView(R.id.txtvDescription)  TextView descripcion;
        @BindView(R.id.txtvDate)  TextView fecha;
        @BindView(R.id.ibtnDelete) ImageButton delete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final ToDo itemToDo, final OnItemClickListener listener, final OnItemLogClickListener longListener)
        {
            this.id.setText(String.valueOf(itemToDo._id.toString()));
            this.titulo.setText(itemToDo.titulo);
            this.descripcion.setText(itemToDo.descripcion);
            this.fecha.setText(itemToDo.fecha);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(itemToDo, getAdapterPosition(), delete);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longListener.onItemLongClick(itemToDo, getAdapterPosition(), delete);
                    return true;
                }
            });
        }
    }
}
/*MainActivity.db = dbHelper.getReadableDatabase();
        cupboard().withDatabase(MainActivity.db).delete(itemToDo);
        MainActivity.db.close();*/