package com.example.rozgarproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    static int count = 0;
    String category = "0";
    public TextView register,forgotPassword;
    private EditText editTextEmail,editTextPassword;
    private Button signIn,changelang;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    String uid;
    ListView l1;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        if(count == 0)
        {
            showChangeLanguageDialog();
            count++;
        }
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(getResources().getString(R.string.app_name));
        setContentView(R.layout.activity_main);
        changelang = (Button) findViewById(R.id.language);
        changelang.setOnClickListener(MainActivity.this);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(MainActivity.this);
        signIn = (Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(MainActivity.this);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(MainActivity.this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.language:
                showChangeLanguageDialog();
                break;
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.signIn:
                userLogin();
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(this,forgotPassword.class));
                break;
        }
    }
    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
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
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    uid = user.getUid();
                    if(user.isEmailVerified()) {
                        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                category = snapshot.child(uid).child("category").getValue(String.class);
                                if(category.equals("1")){
                                    startActivity(new Intent(MainActivity.this,WorkProvider.class));
                                }
                                else{
                                    startActivity(new Intent(MainActivity.this,WorkSeeker.class));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this,getResources().getString(R.string.verifymail),Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this,getResources().getString(R.string.failedLogin),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void showChangeLanguageDialog(){
        final String[] listItems = {"English","हिंदी","मराठी"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Choose Language...");
        mBuilder.setSingleChoiceItems(listItems,-1,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface,int i){
                if(i == 0)
                {
                    setLocale("values");
                    recreate();
                }
                else if(i == 1) {
                    setLocale("hi");
                    recreate();
                }
                else if(i == 2){
                    setLocale("mr");
                    recreate();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog mdialog = mBuilder.create();
        mdialog.show();
    }
    private void setLocale(String lan){
        Locale locale = new Locale(lan);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_lang",lan);
        editor.apply();
    }
    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings",MODE_PRIVATE);
        String language = prefs.getString("My_lang","");
        setLocale(language);
    }
}