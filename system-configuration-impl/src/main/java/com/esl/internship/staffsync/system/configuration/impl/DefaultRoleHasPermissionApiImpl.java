package com.esl.internship.staffsync.system.configuration.impl;


import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.entities.*;
import com.esl.internship.staffsync.system.configuration.api.IRoleHasPermissionApi;
import com.esl.internship.staffsync.system.configuration.model.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.esl.internship.staffsync.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.system.configuration.model.SystemConfigurationMapper.INSTANCE;


public class DefaultRoleHasPermissionApiImpl implements IRoleHasPermissionApi {

    @Inject
    JPAApi jpaApi;

    private static final QJpaRoleHasPermission qJpaRoleHasPermission = QJpaRoleHasPermission.jpaRoleHasPermission;
    private static final QJpaPermission qJpaPermission = QJpaPermission.jpaPermission;
    private static final QJpaRole qJpaRole = QJpaRole.jpaRole;

    /**
     * @author WARITH
     * @dateCreated 03/08/2023
     * @description Assigns a permission to a role
     *
     * @param roleId Id of the role to assign permission to
     * @param permissionId Id the of the permission to assign
     * @param employee Employee creating the assignment
     *
     * @return boolean
     */
    @Override
    public boolean givePermissionToRole(String roleId, String permissionId, Employee employee) {

        JpaRole jpaRole = new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaRole)
                .where(qJpaRole.roleId.eq(roleId))
                .fetchOne();

        JpaPermission jpaPermission = new JPAQueryFactory(this.jpaApi.em())
                .selectFrom(qJpaPermission)
                .where(qJpaPermission.permissionId.eq(permissionId))
                .fetchOne();

        if (jpaPermission == null || jpaRole == null)
            return false;

        final JpaRoleHasPermission jpaRoleHasPermission = new JpaRoleHasPermission();
        jpaRoleHasPermission.setRoleHasPermissionId(UUID.randomUUID().toString());
        jpaRoleHasPermission.setPermission(jpaPermission);
        jpaRoleHasPermission.setRole(jpaRole);
        jpaRoleHasPermission.setCreatedBy(stringifyEmployee(employee, "Assigned Permission to Role"));
        jpaRoleHasPermission.setDateCreated(Timestamp.from(Instant.now()));

        jpaApi.em().persist(jpaRoleHasPermission);

        return true;
    }

    /**
     * @author WARITH
     * @dateCreated 03/08/2023
     * @description Removes a permission from a role
     *
     * @param roleId Id of the role to permission to
     * @param permissionId Id the of the permission to assign
     *
     * @return boolean
     */
    @Override
    public boolean removePermissionFromRole(String roleId, String permissionId) {
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaRoleHasPermission)
                .where(qJpaRoleHasPermission.role.roleId.eq(roleId))
                .where(qJpaRoleHasPermission.permission.permissionId.eq(permissionId))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 05/08/2023
     * @description Gets a RoleHasPermission
     *
     * @param roleHasPermissionId Id of the roleHasPermission
     *
     * @return Optional<RoleHasPermission>
     */
    @Override
    public Optional<RoleHasPermission> getRoleHasPermission(String roleHasPermissionId) {
        JpaRoleHasPermission jpaRoleHasPermission = new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaRoleHasPermission)
                .where(qJpaRoleHasPermission.roleHasPermissionId.eq(roleHasPermissionId))
                .fetchOne();
        return Optional.ofNullable(INSTANCE.mapRoleHasPermission(jpaRoleHasPermission));
    }

    /**
     * @author WARITH
     * @dateCreated 03/08/2023
     * @description Removes a RoleHasPermission record directly
     *
     * @param roleHasPermissionId Id of the RoleHasPermission to remove
     *
     * @return boolean
     */
    @Override
    public boolean removeRoleHasPermission(String roleHasPermissionId) {
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaRoleHasPermission)
                .where(qJpaRoleHasPermission.roleHasPermissionId.eq(roleHasPermissionId))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 03/08/2023
     * @description Gets all Roles having a permission instance
     *
     * @param permissionId Id of the permission to get roles for
     *
     * @return List<Role>
     */
    @Override
    public List<Role> getRolesForPermissionApi(String permissionId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.jpaApi.em());

        return queryFactory.selectFrom(qJpaRoleHasPermission)
                .select(qJpaRoleHasPermission.role)
                .where(qJpaRoleHasPermission.permission.permissionId.eq(permissionId))
                .fetch()
                .stream()
                .map(INSTANCE::mapRole)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 03/08/2023
     * @description Gets all the Permissions a role instance have
     *
     * @param roleId Id of the role to get permissions for
     *
     * @return List<Permission>
     */
    @Override
    public List<Permission> getPermissionForRole(String roleId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.jpaApi.em());

        return queryFactory.selectFrom(qJpaRoleHasPermission)
                .select(qJpaRoleHasPermission.permission)
                .where(qJpaRoleHasPermission.role.roleId.eq(roleId))
                .fetch()
                .stream()
                .map(INSTANCE::mapPermission)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 03/08/2023
     * @description Gets all Role-Permission Mapping
     *
     * @return List<RoleHasPermission>
     */
    @Override
    public List<RoleHasPermission> getAllRoleHasPermission() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.jpaApi.em());

        return queryFactory.selectFrom(qJpaRoleHasPermission)
                .fetch()
                .stream()
                .map(INSTANCE::mapRoleHasPermission)
                .collect(Collectors.toList());
    }

}
