package com.esl.internship.staffsync.document.management.dto;

import javax.validation.constraints.NotNull;
import java.io.File;

public class DocumentDTO {

    private File file;

    private String documentDescription;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

}
