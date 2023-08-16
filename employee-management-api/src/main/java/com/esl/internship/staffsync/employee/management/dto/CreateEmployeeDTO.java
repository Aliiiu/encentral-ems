package com.esl.internship.staffsync.employee.management.dto;

import java.io.File;

public class CreateEmployeeDTO extends EmployeeDTO {
    private File profilePicture;

    public File getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(File profilePicture) {
        this.profilePicture = profilePicture;
    }
}
