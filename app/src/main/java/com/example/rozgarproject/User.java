package com.example.rozgarproject;

public class User {
    public String fullName,age,email,category,phoneNumber;
    public User(){
        // default constructor
    }
    public User(String fullName,String age, String email,String category,String phoneNumber) {
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.category = category;
        this.phoneNumber = phoneNumber;
    }
}
