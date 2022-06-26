package com.example.rozgarproject;

public class allJobs {
    String JobTitle,JobDate,NumberofWorkers,JobLocation,recruiterId,JobId,salary,Apply;
    public allJobs(){

    }
    public allJobs(String JobTitle,String JobDate,String NumberofWorkers,String JobLocation,String recruiterId,String JobId,String salary,String Apply){
        this.JobTitle = JobTitle;
        this.JobDate = JobDate;
        this.NumberofWorkers = NumberofWorkers;
        this.recruiterId=recruiterId;
        this.JobLocation=JobLocation;
        this.JobId = JobId;
        this.salary = salary;
        this.Apply = Apply;
    }
    public String getJobTitle(){
        return JobTitle;
    }
    public String getJobDate(){
        return JobDate;
    }
    public String getNumberofWorkers(){
        return NumberofWorkers;
    }
    public String getJobId(){return JobId;}
    public String getRecruiterId(){return recruiterId;}
    public String getSalary(){return salary;}
    public String getJobLocation(){return JobLocation;}
    public String getApply(){return Apply;}
}
