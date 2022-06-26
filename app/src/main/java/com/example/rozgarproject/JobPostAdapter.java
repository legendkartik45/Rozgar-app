package com.example.rozgarproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JobPostAdapter extends RecyclerView.Adapter<JobPostAdapter.JobsViewAdapter> {
    List<JobPost>list;
    Context context;
    public JobPostAdapter(List<JobPost>list,Context context){
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public JobsViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.personal_job_list,parent,false);
        return new JobsViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobsViewAdapter holder, int position) {
            JobPost currItem = list.get(position);
            holder.JobTitle.setText(currItem.getJobTitle());
            holder.JobDate.setText(currItem.getJobDate());
            holder.NumberofWorkers.setText(currItem.getNumberofWorkers());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class JobsViewAdapter extends RecyclerView.ViewHolder {
        TextView JobTitle,JobDate,NumberofWorkers;
        public JobsViewAdapter(@NonNull View itemView) {
            super(itemView);
            JobTitle = itemView.findViewById(R.id.JobTitle);
            JobDate = itemView.findViewById(R.id.JobDate);
            NumberofWorkers = itemView.findViewById(R.id.WorkersNumber);
        }
    }
}
