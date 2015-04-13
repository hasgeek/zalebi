package com.hasgeek.zalebi.api.model;

import com.orm.SugarRecord;

/**
 * Created by karthikbalakrishnan on 31/03/15.
 */
public class SyncQueueContact extends SugarRecord<SyncQueueContact> {
    public String userPuk;
    public String userKey;
    public String spaceId;

    public SyncQueueContact() {
    }

    public SyncQueueContact(String userPuk, String userKey, String spaceId) {
        this.userPuk = userPuk;
        this.userKey = userKey;
        this.spaceId = spaceId;
    }

    public String getUserPuk() {
        return userPuk;
    }

    public void setUserPuk(String userPuk) {
        this.userPuk = userPuk;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }
}
