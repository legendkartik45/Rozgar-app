package com.example.rozgarproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WorkSeeker extends AppCompatActivity implements View.OnClickListener{
    Button filter,viewAll,applyFilter;
    EditText location,salary;
    RecyclerView recyclerView;
    List<allJobs> list;
    allJobsAdapter adapter;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_seeker);
        recyclerView = findViewById(R.id.recycler_all_job_id);
        filter = findViewById(R.id.Filter);
        viewAll = findViewById(R.id.ViewAllJobs);
        applyFilter = findViewById(R.id.ApplyFilter);
        location = findViewById(R.id.SearchLocation);
        salary = findViewById(R.id.SearchSalary);
        filter.setOnClickListener(WorkSeeker.this);
        viewAll.setOnClickListener(WorkSeeker.this);
        applyFilter.setOnClickListener(WorkSeeker.this);
        reference = FirebaseDatabase.getInstance().getReference().child("Job Posts");
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    for(DataSnapshot ds1: ds.getChildren()){
                        String JobTitle = ds1.child("JobTitle").getValue(String.class);
                        String JobDate = ds1.child("date").getValue(String.class);
                        String JobWorkers = ds1.child("workersNumber").getValue(String.class);
                        String JobLocation = ds1.child("address").getValue(String.class);
                        String recruiterId = ds1.child("id").getValue(String.class);
                        String jobId = ds1.child("jobId").getValue(String.class);
                        String salary = ds1.child("salary").getValue(String.class);
                        String apply = "Apply";
                        allJobs job = new allJobs(JobTitle, JobDate,JobWorkers,JobLocation,recruiterId,jobId,salary,apply);
                        list.add(job);
                    }
                }
                adapter = new allJobsAdapter(list,WorkSeeker.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WorkSeeker.this,"Error occured",Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Filter:
                location.setVisibility(View.VISIBLE);
                salary.setVisibility(View.VISIBLE);
                applyFilter.setVisibility(View.VISIBLE);
                break;
            case R.id.ViewAllJobs:
                location.setVisibility(View.GONE);
                salary.setVisibility(View.GONE);
                applyFilter.setVisibility(View.GONE);
                adapter.getFilter().filter("");
                break;
            case R.id.ApplyFilter:
                location.setVisibility(View.GONE);
                salary.setVisibility(View.GONE);
                applyFilter.setVisibility(View.GONE);
                String charSequence = "";
                charSequence += location.getText().toString();
                charSequence += '$';
                charSequence += salary.getText().toString();
                adapter.getFilter().filter(charSequence);
                break;
        }
    }
}