package com.example.rozgarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class WorkProvider extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_provider);
        Button CreatedJobs = (Button) findViewById(R.id.CreatedJobs);
        Button PostJob = (Button) findViewById(R.id.PostJob);
        CreatedJobs.setOnClickListener(WorkProvider.this);
        PostJob.setOnClickListener(WorkProvider.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.CreatedJobs:
                startActivity(new Intent(WorkProvider.this,PersonalJobsCreated.class));
                break;
            case R.id.PostJob:
                startActivity(new Intent(WorkProvider.this,PostJobActivity.class));
                break;
        }
    }
}