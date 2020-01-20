package com.example.bureaucratme;

import androidx.annotation.NonNull;

public class FilesData {

    private String urlLink;
    private String fileName;
    private String institutionName;
    private String nameInStorage;

    public FilesData() {}

    public FilesData( String urlLink, String fileName, String institutionName, String nameInStorage) {
        this.urlLink = urlLink;
        this.fileName = fileName;
        this.institutionName = institutionName;
        this.nameInStorage = nameInStorage;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String filename) {
        this.fileName = filename;
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
