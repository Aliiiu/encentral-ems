package com.esl.internship.staffsync.document.management.dto;

public class SaveInfo {

    private String saveDirectory;

    private String fileName;

    private boolean rename = false;

    public SaveInfo(String saveDirectory, String fileName, boolean rename) {
        this.saveDirectory = saveDirectory;
        this.fileName = fileName;
        this.rename = rename;
    }

    public SaveInfo(String saveDirectory, String fileName) {
        this.saveDirectory = saveDirectory;
        this.fileName = fileName;
        this.rename = true;
    }

    public SaveInfo() {
        this.fileName = "";
    }

    public String getSaveDirectory() {
        return saveDirectory;
    }

    public void setSaveDirectory(String saveDirectory) {
        this.saveDirectory = saveDirectory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean renameFile() {
        return rename;
    }

    public void setRename(boolean rename) {
        this.rename = rename;
    }

}
