package com.encentral.staffsync.entity;

import com.encentral.staffsync.entity.enums.Action;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;


/**
 * The persistent class for the permission database table.
 * 
 */
@Entity
@Table(name="permission")
public class JpaPermission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="permission_id", unique=true, nullable=false, length=64)
	private String permissionId;

	@Column(name="created_by", nullable=false)
	private String createdBy;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="permission_action", nullable=false, length=2147483647)
	@Enumerated(EnumType.STRING)
	private Action permissionAction;

	@Column(name="permission_description", length=2147483647)
	private String permissionDescription;

	@Column(name="permission_name", nullable=false, length=64)
	private String permissionName;

	//bidirectional many-to-one association to JpaRoleHasPermission
	@OneToMany(mappedBy="permission")
	private Set<JpaRoleHasPermission> roleHasPermissions;

	//bidirectional many-to-many association to JpaRole
	@ManyToMany(mappedBy="permissions")
	private Set<JpaRole> roles;

	public JpaPermission() {
	}

	public String getPermissionId() {
		return this.permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
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

	public Action getPermissionAction() {
		return this.permissionAction;
	}

	public void setPermissionAction(Action permissionAction) {
		this.permissionAction = permissionAction;
	}

	public String getPermissionDescription() {
		return this.permissionDescription;
	}

	public void setPermissionDescription(String permissionDescription) {
		this.permissionDescription = permissionDescription;
	}

	public String getPermissionName() {
		return this.permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public Set<JpaRoleHasPermission> getRoleHasPermissions() {
		return this.roleHasPermissions;
	}

	public void setRoleHasPermissions(Set<JpaRoleHasPermission> roleHasPermissions) {
		this.roleHasPermissions = roleHasPermissions;
	}

	public JpaRoleHasPermission addRoleHasPermission(JpaRoleHasPermission roleHasPermission) {
		getRoleHasPermissions().add(roleHasPermission);
		roleHasPermission.setPermission(this);

		return roleHasPermission;
	}

	public JpaRoleHasPermission removeRoleHasPermission(JpaRoleHasPermission roleHasPermission) {
		getRoleHasPermissions().remove(roleHasPermission);
		roleHasPermission.setPermission(null);

		return roleHasPermission;
	}

	public Set<JpaRole> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<JpaRole> roles) {
		this.roles = roles;
	}

	public JpaRole addRole(JpaRole role) {
		getRoles().add(role);

		return role;
	}

	/**
	 *  Add a new JpaRole, and update the role to list this permission instance
	 * @param role A JpaRole instance.
	 * @return role
	 */
	public JpaRole addRoleAndUpdate(JpaRole role) {
		getRoles().add(role);
		role.addPermission(this);

		return role;
	}

	public JpaRole removeRole(JpaRole role) {
		getRoles().remove(role);

		return role;
	}

	/**
	 *  Removes an existing JpaRole,
	 *  and removes this instance from the role's permissions set
	 * @param role A JpaRole instance.
	 * @return role
	 */
	public JpaRole removeRoleAndUpdate(JpaRole role) {
		getRoles().remove(role);
		role.removePermission(this);

		return role;
	}

	@Override
	public int hashCode() {
		if (this.getPermissionId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getPermissionId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || this.getClass() != obj.getClass()) return false;
		JpaPermission permission = (JpaPermission) obj;
		return Objects.equals(this.getPermissionId(), permission.getPermissionId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("Name", this.getPermissionName())
				.add("Action", this.getPermissionAction())
				.toString();
	}

}