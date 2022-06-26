package com.example.rozgarproject;

public class NewJobs {
    public String JobTitle,jobId,JobCategory,address,salary,workingHrs,workersNumber,details,id,date,contactInfo;
    public NewJobs(){
        //default constructor
    }
    public NewJobs(String JobTitle,String JobCategory,String address,String salary,String workingHrs,String workersNumber,String details,String id,String date,String contactInfo,String jobId){
        this.JobTitle = JobTitle;
        this.JobCategory = JobCategory;
        this.address = address;
        this.salary = salary;
        this.workingHrs = workingHrs;
        this.workersNumber = workersNumber;
        this.details = details;
        this.id = id;
        this.date = date;
        this.contactInfo = contactInfo;
        this.jobId = jobId;
    }
}
