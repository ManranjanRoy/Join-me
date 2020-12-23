package com.manoranjan.wolwou.Model;

public class MeetingModel {
    String meetingcode,currenttime,keyid;

    public MeetingModel() {
    }

    public MeetingModel(String meetingcode, String currenttime, String keyid) {
        this.meetingcode = meetingcode;
        this.currenttime = currenttime;
        this.keyid = keyid;
    }

    public String getMeetingcode() {
        return meetingcode;
    }

    public void setMeetingcode(String meetingcode) {
        this.meetingcode = meetingcode;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }

    public String getKeyid() {
        return keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }
}
