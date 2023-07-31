package com.encentral.staffsync.entity;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;


/**
 * The persistent class for the role database table.
 * 
 */
@Entity
@Table(name="role")
@NamedQuery(name="JpaRole.findAll", query="SELECT j FROM JpaRole j")
public class JpaRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="role_id", unique=true, nullable=false, length=64)
	private String roleId;

	@Column(name="created_by", nullable=false)
	private String createdBy;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="role_description", length=2147483647)
	private String roleDescription;

	@Column(name="role_name", nullable=false, length=64)
	private String roleName;

	//bidirectional many-to-one association to JpaEmployee
	@OneToMany(mappedBy="role")
	private Set<JpaEmployee> employees;

	//bidirectional many-to-one association to JpaRoleHasPermission
	@OneToMany(mappedBy="role")
	private Set<JpaRoleHasPermission> roleHasPermissions;

	//bidirectional many-to-many association to JpaPermission
	@ManyToMany
	@JoinTable(
		name="role_has_permission"
		, joinColumns={
			@JoinColumn(name="role_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="permission_id", nullable=false)
			}
		)
	private Set<JpaPermission> permissions;

	public JpaRole() {
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
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

	public String getRoleDescription() {
		return this.roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<JpaEmployee> getEmployees() {
		return this.employees;
	}

	public void setEmployees(Set<JpaEmployee> employees) {
		this.employees = employees;
	}

	public JpaEmployee addEmployee(JpaEmployee employee) {
		getEmployees().add(employee);
		employee.setRole(this);

		return employee;
	}

	public JpaEmployee removeEmployee(JpaEmployee employee) {
		getEmployees().remove(employee);
		employee.setRole(null);

		return employee;
	}

	public Set<JpaRoleHasPermission> getRoleHasPermissions() {
		return this.roleHasPermissions;
	}

	public void setRoleHasPermissions(Set<JpaRoleHasPermission> roleHasPermissions) {
		this.roleHasPermissions = roleHasPermissions;
	}

	public JpaRoleHasPermission addRoleHasPermission(JpaRoleHasPermission roleHasPermission) {
		getRoleHasPermissions().add(roleHasPermission);
		roleHasPermission.setRole(this);

		return roleHasPermission;
	}

	public JpaRoleHasPermission removeRoleHasPermission(JpaRoleHasPermission roleHasPermission) {
		getRoleHasPermissions().remove(roleHasPermission);
		roleHasPermission.setRole(null);

		return roleHasPermission;
	}

	public Set<JpaPermission> getPermissions() {
		return this.permissions;
	}

	public void setPermissions(Set<JpaPermission> permissions) {
		this.permissions = permissions;
	}

	public JpaPermission addPermission(JpaPermission permission) {
		getPermissions().add(permission);

		return permission;
	}

	/**
	 *  Adds a new JpaPermission,
	 *  and update the permission to list this role instance
	 * @param permission A JpaPermission instance.
	 * @return permission
	 */
	public JpaPermission addPermissionAndUpdate(JpaPermission permission) {
		getPermissions().add(permission);
		permission.addRole(this);

		return permission;
	}

	public JpaPermission removePermission(JpaPermission permission) {
		getPermissions().remove(permission);

		return permission;
	}

	/**
	 *  Removes an existing JpaPermission,
	 *  and remove this instance from the permission's role set
	 * @param permission A JpaPermission instance.
	 * @return permission
	 */
	public JpaPermission removePermissionAndUpdate(JpaPermission permission) {
		getPermissions().remove(permission);
		permission.removeRole(this);

		return permission;
	}

	@Override
	public int hashCode() {
		if (this.getRoleId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getRoleId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || this.getClass() != obj.getClass()) return false;
		JpaRole role = (JpaRole) obj;
		return Objects.equals(this.getRoleId(), role.getRoleId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("Name", this.getRoleName())
				.toString();
	}

}