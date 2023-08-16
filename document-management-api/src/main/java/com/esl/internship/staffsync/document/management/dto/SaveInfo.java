package com.esl.internship.staffsync.document.management.dto;

/**
 * @author WARITH
 * @dateCreated 12/08/2023
 * @description Used to provide saving information for an uploaded file
 */
public class SaveInfo {

    private  String saveDirectory;

    private String fileName;

    private String newFileName;

    private boolean rename;

    private boolean resolveExt;

    public SaveInfo(String fileName) {
        this.saveDirectory = "";
        this.fileName = fileName;
        this.rename = false;
        this.resolveExt = false;
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
        resolveExtension();
    }

    public SaveInfo(String saveDirectory, String fileName, String newFileName, boolean rename) {
        this.saveDirectory = saveDirectory;
        this.fileName = fileName;
        this.newFileName = newFileName;
        this.rename = rename;
        resolveExtension();
    }

    public SaveInfo(String saveDirectory, String fileName, String newFileName, boolean rename, boolean resolveExt) {
        this.saveDirectory = saveDirectory;
        this.fileName = fileName;
        this.newFileName = newFileName;
        this.rename = rename;

        if (resolveExt)
            resolveExtension();
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

    public String getResolvedName() {
        if (rename && newFileName != null)
            return newFileName;
        return fileName;
    }

    /**
     * @autho WARITH
     * @dateCreated 15/08/2023
     * @description Resolve the extension on the new file name
     */
    public void resolveExtension() {
        String extension = "";
        int dotIndex = this.fileName.lastIndexOf(".");
        if (dotIndex >= 0) {
            extension = this.fileName.substring(dotIndex);
        }
        this.newFileName = this.newFileName + extension;
    }
}
