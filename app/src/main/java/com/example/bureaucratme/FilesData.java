package com.example.bureaucratme;

import androidx.annotation.NonNull;

public class FilesData {

    private String urlLink;
    private String filename;
    private String institutionName;
    private String nameInStorage;

//    public FilesData() {}

    public FilesData( String urlLink, String filename, String institutionName, String nameInStorage) {
        this.urlLink = urlLink;
        this.filename = filename;
        this.institutionName = institutionName;
        this.nameInStorage = nameInStorage;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public String getNameInStorage() {
        return nameInStorage;
    }

    public void setNameInStorage(String nameInStorage) {
        this.nameInStorage = nameInStorage;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }
}
