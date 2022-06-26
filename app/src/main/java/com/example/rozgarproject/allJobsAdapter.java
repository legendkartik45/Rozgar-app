package com.example.rozgarproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class allJobsAdapter extends RecyclerView.Adapter<allJobsAdapter.allJobsViewAdapter>implements Filterable {
    List<allJobs> list,listAll;
    Context context;

    public allJobsAdapter(List<allJobs>list,Context context){
        this.list = list;
        this.listAll = new ArrayList<>(list);
        this.context = context;
    }
    @NonNull
    @Override
    public allJobsViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_jobs_list,parent,false);
        return new allJobsViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull allJobsViewAdapter holder, int position) {
        allJobs currItem = list.get(position);
        holder.JobTitle.setText(currItem.getJobTitle());
        holder.JobDate.setText(currItem.getJobDate());
        holder.JobId.setText(currItem.getJobId());
        holder.JobLocation.setText(currItem.getJobLocation());
        holder.RecruiterId.setText(currItem.getRecruiterId());
        holder.JobSalary.setText(currItem.getSalary());
        holder.JobWorkers.setText(currItem.getNumberofWorkers());
        holder.Apply.setText(currItem.getApply());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<allJobs> filteredJobs = new ArrayList<>();
            if(charSequence.toString().isEmpty())
            {
                filteredJobs.addAll(listAll);
            }
            else if(charSequence.toString().length() == 1)
            {
                filteredJobs.addAll(listAll);
            }
            else{
                String location = "";
                String salary = "";
                boolean flag = false;
                for(int i = 0;i<charSequence.toString().length();i++)
                {
                    if(charSequence.toString().charAt(i) == '$')
                    {
                        flag = true;
                        continue;
                    }
                    if(flag == false)
                    {
                        location += charSequence.toString().charAt(i);
                    }
                    else{
                        salary += charSequence.toString().charAt(i);
                    }
                }
                for(allJobs job: listAll){
                    if(!location.isEmpty() && !salary.isEmpty())
                    {
                        if(job.JobLocation.toLowerCase().contains(location) && job.salary.toLowerCase().equals(salary)){
                            filteredJobs.add(job);
                        }
                    }
                    else if(!location.isEmpty()){
                        if(job.JobLocation.toLowerCase().contains(location)){
                            filteredJobs.add(job);
                        }
                    }
                    else if(!salary.isEmpty()){
                        if(job.salary.toLowerCase().equals(salary)){
                            filteredJobs.add(job);
                        }
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredJobs;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((Collection<? extends allJobs>) filterResults.values);
            notifyDataSetChanged();
        }
    };
    public class allJobsViewAdapter extends RecyclerView.ViewHolder {
        TextView JobTitle,JobDate,JobSalary,JobLocation,RecruiterId,JobId,JobWorkers;
        Button Apply;
        public allJobsViewAdapter(@NonNull View itemView) {
            super(itemView);
            JobTitle = itemView.findViewById(R.id.allJobTitle);
            JobDate = itemView.findViewById(R.id.allJobDate);
            JobSalary = itemView.findViewById(R.id.allJobSalary);
            JobLocation = itemView.findViewById(R.id.allJobLocation);
            RecruiterId = itemView.findViewById(R.id.allJobRecruiterId);
            JobId = itemView.findViewById(R.id.allJobId);
            JobWorkers = itemView.findViewById(R.id.allJobWorkers);
            Apply = itemView.findViewById(R.id.ApplyAllJob);
            Apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String rid = RecruiterId.getText().toString();
                    String jid = JobId.getText().toString();
                    FirebaseAuth mAuth;
                    DatabaseReference reference;
                    mAuth = FirebaseAuth.getInstance();
                    String uid = mAuth.getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference("AppliedWorkers").child(jid).child(uid).setValue("1");
                    Toast.makeText(context,"Applied SuccessFully",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
