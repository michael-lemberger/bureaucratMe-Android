package com.example.bureaucratme;

import org.openqa.selenium.By;

public class Users {
    private String userID;

    /*
    אלה הפרטים שצריך בשביל הטופס הנסיוני, הוצאנו אותם מדף המקור
     */
    private String cn;
    private String givenName;
    private String sn;
    private String mail;
    private String mail_confirm;
    private String mobile;
    private String mobile_confirm;
    private String idIssueDateYear;
    private String idIssueDateMonth;
    private String idIssueDateDay;
    private String password1;
    private String password2;

    private String _id, _privateName, _familyName, _phoneNumber, _email, _dbId;

    public Users(String id, String privateName, String familyName, String email, String phoneNumber, String dbId){
        _id = id;
        _privateName = privateName;
        _familyName = familyName;
        _email = email;
        _phoneNumber = phoneNumber;
        _dbId = dbId;
    }

    /*
    כאן צריך להוסיף שליפה של הuid מהפיירבייס
     */
    public Users(String userId){
        this.userID = userId;
    }

    public String getCn() {
        return cn;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getSn() {
        return sn;
    }

    public String getMail() {
        return mail;
    }

    public String getMail_confirm() {
        return mail_confirm;
    }

    public String getMobile() {
        return mobile;
    }

    public String getMobile_confirm() {
        return mobile_confirm;
    }

    public String getIdIssueDateYear() {
        return idIssueDateYear;
    }

    public String getIdIssueDateMonth() {
        return idIssueDateMonth;
    }

    public String getIdIssueDateDay() {
        return idIssueDateDay;
    }

    public String getPassword1() {
        return password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setMail_confirm(String mail_confirm) {
        this.mail_confirm = mail_confirm;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setMobile_confirm(String mobile_confirm) {
        this.mobile_confirm = mobile_confirm;
    }

    public void setIdIssueDateYear(String idIssueDateYear) {
        this.idIssueDateYear = idIssueDateYear;
    }

    public void setIdIssueDateMonth(String idIssueDateMonth) {
        this.idIssueDateMonth = idIssueDateMonth;
    }

    public void setIdIssueDateDay(String idIssueDateDay) {
        this.idIssueDateDay = idIssueDateDay;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}

