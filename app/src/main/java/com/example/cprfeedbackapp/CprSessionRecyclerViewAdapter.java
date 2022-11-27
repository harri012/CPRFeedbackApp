package com.example.cprfeedbackapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cprfeedbackapp.database.AppDatabase;
import com.example.cprfeedbackapp.database.entity.CprSessionDatapoint;

import java.util.List;

public class CprSessionRecyclerViewAdapter extends RecyclerView.Adapter<CprSessionRecyclerViewAdapter.ViewHolder> {

    private List<String> localDataSet;

    protected AppDatabase db;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView datetimeTextView;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            datetimeTextView = itemView.findViewById(R.id.datetimeTextView);
        }

        public TextView getDatetimeTextView() {return datetimeTextView;};
    }

    public CprSessionRecyclerViewAdapter(List<String> localDataSet) {
        this.localDataSet = localDataSet;
    }

    @NonNull
    @Override
    public CprSessionRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cpr_session_recycler_view_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CprSessionRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.getDatetimeTextView().setText(localDataSet.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = holder.getLayoutPosition();

                db = AppDatabase.getInstance(view.getContext());

                // Returns a list of CprSessionDatapoint of the session specified by the datetime
                List<CprSessionDatapoint> sessionDatapoints = db.cprSessionDao().getSessionDatapoints(localDataSet.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
