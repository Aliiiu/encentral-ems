package com.encentral.staffsync.entity;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * The persistent class for the role_has_permission database table.
 * 
 */
@Entity
@Table(name="role_has_permission")
public class JpaRoleHasPermission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="role_has_permission_id", unique=true, nullable=false, length=64)
	private String roleHasPermissionId;

	@Column(name="created_by", nullable=false)
	private String createdBy;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	//bidirectional many-to-one association to JpaPermission
	@ManyToOne
	@JoinColumn(name="permission_id", nullable=false)
	private JpaPermission permission;

	//bidirectional many-to-one association to JpaRole
	@ManyToOne
	@JoinColumn(name="role_id", nullable=false)
	private JpaRole role;

	public JpaRoleHasPermission() {
	}

	public String getRoleHasPermissionId() {
		return this.roleHasPermissionId;
	}

	public void setRoleHasPermissionId(String roleHasPermissionId) {
		this.roleHasPermissionId = roleHasPermissionId;
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

	public JpaPermission getPermission() {
		return this.permission;
	}

	public void setPermission(JpaPermission permission) {
		this.permission = permission;
	}

	public JpaRole getRole() {
		return this.role;
	}

	public void setRole(JpaRole role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		if (this.getRoleHasPermissionId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getRoleHasPermissionId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || this.getClass() != obj.getClass()) return false;
		JpaRoleHasPermission roleHasPermission = (JpaRoleHasPermission) obj;
		return Objects.equals(this.getRoleHasPermissionId(), roleHasPermission.getRoleHasPermissionId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("Role", this.getRole())
				.add("Permission", this.getPermission())
				.toString();
	}

}