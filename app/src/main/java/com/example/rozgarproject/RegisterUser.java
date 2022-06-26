package com.example.rozgarproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rozgarproject.MainActivity;
import com.example.rozgarproject.R;
import com.example.rozgarproject.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    static String categorySelected = "0";
    private TextView banner,registerUser,CategoryBtn;
    private EditText editTextFullName,editTextAge,editTextEmail,editTextPassword,editTextNumber;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth = FirebaseAuth.getInstance();
        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);
        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextAge = (EditText) findViewById(R.id.age);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        CategoryBtn = (Button) findViewById(R.id.Category);
        CategoryBtn.setOnClickListener(RegisterUser.this);
        editTextNumber = (EditText) findViewById(R.id.Number);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerUser:
                registerUser();
                break;
            case R.id.Category:
                selectCategory();
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String phoneNumber = editTextNumber.getText().toString().trim();
        if(fullName.isEmpty()){
            editTextFullName.setError(getResources().getString(R.string.fullname));
            editTextFullName.requestFocus();
            return;
        }
        if(phoneNumber.isEmpty())
        {
            editTextNumber.setError(getResources().getString(R.string.reqPhone));
            editTextNumber.requestFocus();
            return;
        }
        if(phoneNumber.charAt(0)!='+')
        {
            phoneNumber = "+91" + phoneNumber;
        }
        String number = phoneNumber;
        if(number.length()!=13)
        {
            editTextNumber.setError(getResources().getString(R.string.validPhone));
            editTextNumber.requestFocus();
            return;
        }
        if(age.isEmpty())
        {
            editTextAge.setError(getResources().getString(R.string.reqAge));
            editTextAge.requestFocus();
            return;
        }
        if(email.isEmpty())
        {
            editTextEmail.setError(getResources().getString(R.string.reqEmail));
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError(getResources().getString(R.string.validEmail));
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError(getResources().getString(R.string.reqPassword));
            editTextPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            editTextPassword.setError(getResources().getString(R.string.validPass));
            editTextPassword.requestFocus();
            return;
        }
        if(categorySelected.equals("0"))
        {
            Toast.makeText(RegisterUser.this,getResources().getString(R.string.CategorySelection),Toast.LENGTH_LONG).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullName,age,email,categorySelected,number);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this,getResources().getString(R.string.SuccRegister),Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }else
                                    {
                                        Toast.makeText(RegisterUser.this,getResources().getString(R.string.failRegister),Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterUser.this,getResources().getString(R.string.failRegister),Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
    private void selectCategory(){
        final String[] category = {getResources().getString(R.string.Provider),getResources().getString(R.string.seeker)};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegisterUser.this);
        mBuilder.setTitle(getResources().getString(R.string.Category));
        mBuilder.setSingleChoiceItems(category, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0)
                {
                    // intent.putExtra("category","0");
                    dialogInterface.dismiss();
                    categorySelected = "1";
                }
                else if(i == 1)
                {
                    // intent.putExtra("category","1");
                    dialogInterface.dismiss();
                    categorySelected = "2";
                }
                dialogInterface.dismiss();
            }

        });
        AlertDialog mdialog = mBuilder.create();
        mdialog.show();
    }
}