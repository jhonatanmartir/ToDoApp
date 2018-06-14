package com.dev.jhonyrg.todoapp.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.jhonyrg.todoapp.R;
import com.dev.jhonyrg.todoapp.items.ToDo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public static final int WAIT = 0;
    public static final int DONE = 1;
    public static final int CRITICAL = 2;

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
        void onItemClick(ToDo itemToDo, int position, View view);
    }

    public interface OnItemLogClickListener
    {
        void onItemLongClick(ToDo itemToDo, int position, View view);
    }

    //View Holder Class
    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txtvId)  TextView id;
        @BindView(R.id.txtvTitle)  TextView title;
        @BindView(R.id.txtvDescription)  TextView description;
        @BindView(R.id.txtvDate)  TextView rename;
        @BindView(R.id.ibtnDelete) ImageButton delete;
        @BindView(R.id.imgStatus) ImageView status;
        @BindView(R.id.cvItem) CardView cardView;
        @BindView(R.id.lytDelete) LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final ToDo itemToDo, final OnItemClickListener listener, final OnItemLogClickListener longListener)
        {
            this.id.setText(String.valueOf(itemToDo._id.toString()));
            this.title.setText(itemToDo.titulo);
            this.description.setText(itemToDo.descripcion);
            this.rename.setText(itemToDo.fecha);
            switch (itemToDo.status)
            {
                case WAIT:
                    this.status.setImageResource(R.drawable.calendar_clock);
                    this.cardView.setCardBackgroundColor(itemView.getResources().getColor(R.color.backgroundWaitColor));
                    this.layout.setBackgroundResource(R.color.backgroundDeleteWaitColor);
                    this.delete.setBackgroundResource(R.color.backgroundDeleteWaitColor);
                    break;

                case DONE:
                    this.status.setImageResource(R.drawable.calendar_check);
                    this.cardView.setCardBackgroundColor(itemView.getResources().getColor(R.color.backgroundDoneColor));
                    this.layout.setBackgroundResource(R.color.backgroundDeleteDoneColor);
                    this.delete.setBackgroundResource(R.color.backgroundDeleteDoneColor);
                    break;

                case CRITICAL:
                    this.status.setImageResource(R.drawable.calendar_late);
                    this.cardView.setCardBackgroundColor(itemView.getResources().getColor(R.color.backgroundCriticalColor));
                    this.layout.setBackgroundResource(R.color.backgroundDeleteCriticalColor);
                    this.delete.setBackgroundResource(R.color.backgroundDeleteCriticalColor);
                    break;
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(itemToDo, getAdapterPosition(), itemView);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longListener.onItemLongClick(itemToDo, getAdapterPosition(), itemView);
                    return true;
                }
            });
        }
    }
}