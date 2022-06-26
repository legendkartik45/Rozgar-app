package com.example.rozgarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class PostJobActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener{

    EditText JobTitle,newCategory,address,contactInfo,salary,workingHrs,WorkersNumber,Description;
    LinearLayout newCategoryLayout;
    Button PostJob;
    String category = "";
    private FirebaseAuth mAuth;
    private DatabaseReference mJobPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);
        JobTitle = (EditText) findViewById(R.id.titleEditText);
        newCategoryLayout = (LinearLayout) findViewById(R.id.newCategoryLayout);
        newCategoryLayout.setVisibility(View.GONE);
        newCategory = (EditText) findViewById(R.id.newCategoryEditText);
        address = (EditText) findViewById(R.id.locationEditText);
        salary = (EditText)findViewById(R.id.salaryEditText);
        workingHrs = (EditText) findViewById(R.id.HrsEditText);
        WorkersNumber = (EditText) findViewById(R.id.workersEditText);
        Description = (EditText) findViewById(R.id.jobDescriptionEditText);
        contactInfo = (EditText) findViewById(R.id.contactInfoEditText);
        PostJob = (Button) findViewById(R.id.PostJob);
        PostJob.setOnClickListener(PostJobActivity.this);
        Spinner spinner = (Spinner) findViewById(R.id.JobCategorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.JobCategories,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid =mUser.getUid();
        mJobPost = FirebaseDatabase.getInstance().getReference().child("Job Posts").child(uid);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = adapterView.getItemAtPosition(i).toString().trim();
        if(category.equals("Custom")){
            newCategoryLayout.setVisibility(View.VISIBLE);
            return;
        }
        else
        {
            newCategoryLayout.setVisibility(View.GONE);
            return;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        if(category.equals("Custom"))
            category = newCategory.getText().toString().trim();
        String categorySelected = category;
        String title = JobTitle.getText().toString().trim();
        String location = address.getText().toString().trim();
        String wages = salary.getText().toString().trim();
        String workingTime = workingHrs.getText().toString().trim();
        String numberofWorkers = WorkersNumber.getText().toString().trim();
        String details = Description.getText().toString().trim();
        String contact = contactInfo.getText().toString().trim();
        if(title.isEmpty())
        {
            JobTitle.setError(getResources().getString(R.string.RequestJobTitle));
            JobTitle.requestFocus();
            return;
        }
        if(category.equals("Custom"))
        {
            Toast.makeText(this,categorySelected,Toast.LENGTH_SHORT).show();
            newCategory.setError(getResources().getString(R.string.RequestJobCategory));
            newCategory.requestFocus();
            return;
        }
        if(location.isEmpty())
        {
            address.setError(getResources().getString(R.string.RequestAddress));
            address.requestFocus();
            return;
        }
        if(contact.isEmpty())
        {
            contactInfo.setError(getResources().getString(R.string.RequestContact));
            contactInfo.requestFocus();
            return;
        }
        if(contact.charAt(0)!='+')
        {
            contact = "+91" + contact;
        }
        if(contact.length()!=13)
        {
            contactInfo.setError(getResources().getString(R.string.ValidContact));
            contactInfo.requestFocus();
            return;
        }
        if(wages.isEmpty())
        {
            salary.setError(getResources().getString(R.string.RequestWages));
            salary.requestFocus();
            return;
        }
        if(workingTime.isEmpty())
        {
            workingHrs.setError(getResources().getString(R.string.RequestHrs));
            workingHrs.requestFocus();
            return;
        }
        if(numberofWorkers.isEmpty())
        {
            WorkersNumber.setError(getResources().getString(R.string.WorkersReq));
            WorkersNumber.requestFocus();
            return;
        }
        String PhoneNumber = contact;
        String id = mJobPost.push().getKey();
        String date = DateFormat.getDateInstance().format(new Date());
        NewJobs job = new NewJobs(title,categorySelected,location,wages,workingTime,numberofWorkers,details,mAuth.getCurrentUser().getUid(),date,PhoneNumber,id);
        mJobPost.child(id).setValue(job);
        Toast.makeText(PostJobActivity.this,getResources().getString(R.string.JobCreated),Toast.LENGTH_SHORT).show();
    }
}