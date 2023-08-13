package com.esl.internship.staffsync.document.management.dto;

public class SaveInfo {

    private  String saveDirectory;

    private String fileName;

    private String newFileName;

    private boolean rename;

    public SaveInfo(String fileName) {
        this.saveDirectory = "";
        this.fileName = fileName;
        this.rename = false;
    }

    public SaveInfo(String saveDirectory, String fileName) {
        this.saveDirectory = saveDirectory;
        this.fileName = fileName;
        this.rename = false;
    }

    public SaveInfo(String saveDirectory, String fileName, String newFileName) {
        this.saveDirectory = saveDirectory;
        this.fileName = fileName;
        this.newFileName = newFileName;
        this.rename = true;
    }

    public SaveInfo(String saveDirectory, String fileName, String newFileName, boolean rename) {
        this.saveDirectory = saveDirectory;
        this.fileName = fileName;
        this.newFileName = newFileName;
        this.rename = rename;
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

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    public boolean renameFile() {
        return rename;
    }

    public void setRename(boolean rename) {
        this.rename = rename;
    }
}
