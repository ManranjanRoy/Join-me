package com.manoranjan.wolwou.Model;

public class Usermodel {
    String mailid,password,keyid,type;

    public Usermodel(String mailid, String password, String keyid, String type) {
        this.mailid = mailid;
        this.password = password;
        this.keyid = keyid;
        this.type = type;
    }

    public String getMailid() {
        return mailid;
    }

    public void setMailid(String mailid) {
        this.mailid = mailid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKeyid() {
        return keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
