package com.example.rozgarproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PersonalJobsCreated extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private DatabaseReference JobPostDatabase;
    List<JobPost>list;
    JobPostAdapter adapter;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_jobs_created);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();
        recyclerView = findViewById(R.id.recycler_job_post_id);
        reference = FirebaseDatabase.getInstance().getReference().child("Job Posts").child(uid);
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for (DataSnapshot snapshot: datasnapshot.getChildren()){
                    String Jobtitle = snapshot.child("JobTitle").getValue(String.class);
                    String JobDate = snapshot.child("date").getValue(String.class);
                    String numberofWorkers = "Workers Required = " + snapshot.child("workersNumber").getValue(String.class);
                    JobPost post = new JobPost(Jobtitle,JobDate,numberofWorkers);
                    list.add(post);
                }
                adapter = new JobPostAdapter(list,PersonalJobsCreated.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PersonalJobsCreated.this,"Error occured",Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}