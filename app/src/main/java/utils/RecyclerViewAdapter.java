package utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.jhonyrg.todoapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<ToDo> toDo;
    private int idLayout;
    private OnItemClickListener itemClickListener;

    public RecyclerViewAdapter(List<ToDo> toDo, int idLayout, OnItemClickListener itemClickListener) {
        this.toDo = toDo;
        this.idLayout = idLayout;
        this.itemClickListener = itemClickListener;
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
        holder.bind(this.toDo.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return this.toDo.size();
    }

    public interface OnItemClickListener
    {
        void onItemClick(ToDo itemToDo, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.txtvTitle) public TextView titulo;
        @BindView(R.id.txtvDescription) public TextView descripcion;
        @BindView(R.id.txtvDate) public TextView fecha;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }

        public void bind(final ToDo itemToDo, final OnItemClickListener listener)
        {
            this.titulo.setText(itemToDo.titulo);
            this.descripcion.setText(itemToDo.descripcion);
            this.fecha.setText(itemToDo.fecha);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(itemToDo, getAdapterPosition());
                }
            });
        }
    }
}
