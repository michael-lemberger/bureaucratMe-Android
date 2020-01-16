package com.example.bureaucratme;

public class FilesData {

    private String urlLink;
    private String filename;

    public FilesData() {}

    public FilesData( String urlLink, String filename) {
        this.urlLink = urlLink;
        this.filename = filename;
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
}
