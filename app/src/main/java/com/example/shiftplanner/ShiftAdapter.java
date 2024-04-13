package com.example.shiftplanner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftplanner.models.Shift;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ShiftAdapter extends FirebaseRecyclerAdapter<Shift, ShiftAdapter.ShiftViewHolder> {

    public ShiftAdapter(@NonNull FirebaseRecyclerOptions<Shift> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ShiftViewHolder holder, int position, @NonNull Shift model) {
        holder.textViewHoursDescription.setText(String.valueOf(model.getHoursDescription()));
        holder.textViewEmployeeName.setText(model.getFirstName() + " " + model.getLastName());
        holder.textViewDate.setText(model.getDate());

        holder.itemView.setOnClickListener(view -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(model);
            }
        });
    }

    @NonNull
    @Override
    public ShiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ShiftViewHolder(view);
    }

    static class ShiftViewHolder extends RecyclerView.ViewHolder {
        TextView textViewHoursDescription;
        TextView textViewEmployeeName;
        TextView textViewDate;

        public ShiftViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHoursDescription = itemView.findViewById(R.id.textViewHoursDescription);
            Log.w("myApp", "Test");
            textViewEmployeeName = itemView.findViewById(R.id.textViewEmployeeName);
            textViewDate = itemView.findViewById(R.id.textViewShiftDate);
        }


    }

    public interface OnItemClickListener {
        void onItemClick(Shift shift);
    }

    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
