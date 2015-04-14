package com.hasgeek.zalebi.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by karthikbalakrishnan on 30/03/15.
 */
public class Attendee extends SugarRecord<Attendee> {

    @Expose
    private String company;
    @Expose
    private String fullname;

    @SerializedName("_id")
    @Expose
    private String userId;

    @SerializedName("puk")
    @Expose
    private String puk;

    @SerializedName("job_title")
    @Expose
    private String jobTitle;

    @Expose
    private String key;

    private String spaceId;

    public Attendee() {
    }

    public boolean diff(Attendee a) {
        boolean flag = false;
        if(this.getCompany()!=a.getCompany() || this.getFullname()!=a.getFullname() || this.getJobTitle()!=a.getJobTitle() || this.getKey()!=a.getKey() || this.getSpaceId()!=a.getSpaceId())
            flag=true;
        return flag;
    }

    public void update(Attendee a) {
        this.setCompany(a.getCompany());
        this.setFullname(a.getFullname());
        this.setJobTitle(a.getJobTitle());
        this.setKey(a.getKey());
        this.setSpaceId(a.getSpaceId());
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPuk() {
        return puk;
    }

    public void setPuk(String puk) {
        this.puk = puk;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }
}
