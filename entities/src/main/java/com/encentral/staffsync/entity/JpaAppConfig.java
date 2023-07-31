package com.encentral.staffsync.entity;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * The persistent class for the app_config database table.
 * 
 */
@Entity
@Table(name="app_config")
@NamedQuery(name="JpaAppConfig.findAll", query="SELECT j FROM JpaAppConfig j")
public class JpaAppConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="app_config_id", unique=true, nullable=false, length=64)
	private String appConfigId;

	@Column(name="configuration_key", nullable=false, length=60)
	private String configurationKey;

	@Column(name="configuration_value", nullable=false, length=100)
	private String configurationValue;

	@Column(name="created_by", nullable=false)
	private String createdBy;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(name="modified_by")
	private String modifiedBy;

	public JpaAppConfig() {
	}

	public String getAppConfigId() {
		return this.appConfigId;
	}

	public void setAppConfigId(String appConfigId) {
		this.appConfigId = appConfigId;
	}

	public String getConfigurationKey() {
		return this.configurationKey;
	}

	public void setConfigurationKey(String configurationKey) {
		this.configurationKey = configurationKey;
	}

	public String getConfigurationValue() {
		return this.configurationValue;
	}

	public void setConfigurationValue(String configurationValue) {
		this.configurationValue = configurationValue;
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

	@Override
	public int hashCode() {
		if (this.getAppConfigId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getAppConfigId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || this.getClass() != obj.getClass()) return false;
		JpaAppConfig appConfig = (JpaAppConfig) obj;
		return Objects.equals(this.getAppConfigId(), appConfig.getAppConfigId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("ConfigurationKey", this.getConfigurationKey())
				.add("ConfigurationValue", this.getConfigurationValue())
				.toString();
	}

}