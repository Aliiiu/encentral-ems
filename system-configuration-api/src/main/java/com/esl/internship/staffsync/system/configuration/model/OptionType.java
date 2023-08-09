package com.esl.internship.staffsync.system.configuration.model;

import java.sql.Timestamp;

public class OptionType {

	private String optionTypeId;

	private String optionTypeName;

	private String optionTypeDescription;

	private String createdBy;

	private Timestamp dateCreated;

	private Timestamp dateModified;

	private String modifiedBy;

	public String getOptionTypeId() {
		return optionTypeId;
	}

	public void setOptionTypeId(String optionTypeId) {
		this.optionTypeId = optionTypeId;
	}

	public String getOptionTypeName() {
		return optionTypeName;
	}

	public void setOptionTypeName(String optionTypeName) {
		this.optionTypeName = optionTypeName;
	}

	public String getOptionTypeDescription() {
		return optionTypeDescription;
	}

	public void setOptionTypeDescription(String optionTypeDescription) {
		this.optionTypeDescription = optionTypeDescription;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Timestamp getDateModified() {
		return dateModified;
	}

	public void setDateModified(Timestamp dateModified) {
		this.dateModified = dateModified;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}