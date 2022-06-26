package com.example.rozgarproject;

public class JobPost {
    String JobTitle,JobDate,NumberofWorkers;
    public JobPost(){

    }
    public JobPost(String JobTitle,String JobDate,String NumberofWorkers){
        this.JobTitle = JobTitle;
        this.JobDate = JobDate;
        this.NumberofWorkers = NumberofWorkers;
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

}
