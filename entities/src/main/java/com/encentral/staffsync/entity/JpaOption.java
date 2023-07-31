package com.encentral.staffsync.entity;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * The persistent class for the option database table.
 * 
 */
@Entity
@Table(name="option")
public class JpaOption implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="option_id", unique=true, nullable=false, length=64)
	private String optionId;

	@Column(name="created_by", nullable=false)
	private String createdBy;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="option_value", nullable=false, length=100)
	private String optionValue;

	//bidirectional many-to-one association to JpaOptionType
	@ManyToOne
	@JoinColumn(name="option_type_id", nullable=false)
	private JpaOptionType optionType;

	public JpaOption() {
	}

	public String getOptionId() {
		return this.optionId;
	}

	public void setOptionId(String optionId) {
		this.optionId = optionId;
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

	public String getOptionValue() {
		return this.optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public JpaOptionType getOptionType() {
		return this.optionType;
	}

	public void setOptionType(JpaOptionType optionType) {
		this.optionType = optionType;
	}

	@Override
	public int hashCode() {
		if (this.getOptionId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getOptionId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		JpaOption option = (JpaOption) obj;
		return Objects.equals(this.getOptionId(), option.getOptionId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("Type", this.getOptionType().getOptionType())
				.add("Value", this.getOptionValue())
				.toString();
	}

}