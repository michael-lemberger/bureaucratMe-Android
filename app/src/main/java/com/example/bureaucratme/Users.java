package com.example.bureaucratme;

public class Users {

    private String _id, _privateName, _familyName, _phoneNumber, _email, _dbId;

    public Users(String id, String privateName, String familyName, String email, String phoneNumber, String dbId){
        _id = id;
        _privateName = privateName;
        _familyName = familyName;
        _email = email;
        _phoneNumber = phoneNumber;
        _dbId = dbId;
    }

    public String get_id() {
        return _id;
    }

    public String get_privateName() {
        return _privateName;
    }

    public String get_familyName() {
        return _familyName;
    }

    public String get_phoneNumber() {
        return _phoneNumber;
    }

    public String get_email() {
        return _email;
    }

    public String get_dbId() {
        return _dbId;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_privateName(String _privateName) {
        this._privateName = _privateName;
    }

    public void set_familyName(String _familyName) {
        this._familyName = _familyName;
    }

    public void set_phoneNumber(String _phoneNumber) {
        this._phoneNumber = _phoneNumber;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public void set_dbId(String _dbId) {
        this._dbId = _dbId;
    }
}

