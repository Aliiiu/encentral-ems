package com.esl.internship.staffsync.entities;


import com.esl.internship.staffsync.entities.attribute.converter.JsonStringConverter;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;


/**
 * The persistent class for the option_type database table.
 * 
 */
@Entity
@Table(name="option_type")
public class JpaOptionType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="option_type_id", unique=true, nullable=false, length=100)
	private String optionTypeId;

	@Column(name="option_type_name", nullable=false, length=100)
	private String optionTypeName;

	@Column(name="option_type_description", nullable=false, length=2147483647)
	private String optionTypeDescription;

	@Convert(converter = JsonStringConverter.class)
	@Column(name="created_by", nullable=false)
	private String createdBy;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Convert(converter = JsonStringConverter.class)
	@Column(name="modified_by")
	private String modifiedBy;

	//bidirectional many-to-one association to JpaOption
	@OneToMany(mappedBy="optionType")
	private Set<JpaOption> options;

	public JpaOptionType() {
	}

	public String getOptionTypeId() {
		return this.optionTypeId;
	}

	public void setOptionTypeId(String optionTypeId) {
		this.optionTypeId = optionTypeId;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Timestamp getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(Timestamp dateModified) {
		this.dateModified = dateModified;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getOptionTypeDescription() {
		return this.optionTypeDescription;
	}

	public void setOptionTypeDescription(String optionTypeDescription) {
		this.optionTypeDescription = optionTypeDescription;
	}

	public String getOptionTypeName() {
		return this.optionTypeName;
	}

	public void setOptionTypeName(String optionTypeName) {
		this.optionTypeName = optionTypeName;
	}

	public Set<JpaOption> getOptions() {
		return this.options;
	}

	public void setOptions(Set<JpaOption> options) {
		this.options = options;
	}

	public JpaOption addOption(JpaOption option) {
		getOptions().add(option);
		option.setOptionType(this);

		return option;
	}

	public JpaOption removeOption(JpaOption option) {
		getOptions().remove(option);
		option.setOptionType(null);

		return option;
	}

	@Override
	public int hashCode() {
		if (this.getOptionTypeId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getOptionTypeId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		JpaOptionType optionType = (JpaOptionType) obj;
		return Objects.equals(this.getOptionTypeId(), optionType.getOptionTypeName());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("TypeName", this.getOptionTypeName())
				.toString();
	}

}