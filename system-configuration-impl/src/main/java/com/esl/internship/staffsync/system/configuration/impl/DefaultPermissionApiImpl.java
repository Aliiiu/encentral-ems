package com.esl.internship.staffsync.system.configuration.impl;

import com.encentral.scaffold.commons.model.Employee;
import com.encentral.staffsync.entity.JpaPermission;
import com.encentral.staffsync.entity.QJpaPermission;
import com.esl.internship.staffsync.system.configuration.api.IPermissionApi;
import com.esl.internship.staffsync.system.configuration.model.Permission;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.encentral.scaffold.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.system.configuration.model.PermissionMapper.INSTANCE;

public class DefaultPermissionApiImpl implements IPermissionApi {
    @Inject
    JPAApi jpaApi;

    private static final QJpaPermission qJpaPermission = QJpaPermission.jpaPermission;

    /**
     * @author WARITH
     * @dateCreated 01/08/2023
     * @description Adds a new Permission into the database;
     *
     * @param permission Permission model filled with the required data
     * @param employee The employee creating this record
     *
     * @return Permission
     */
    @Override
    public Permission addPermission(Permission permission, Employee employee) {
        final JpaPermission jpaPermission = INSTANCE.mapPermission(permission);
        jpaPermission.setPermissionId(UUID.randomUUID().toString());
        jpaPermission.setCreatedBy(stringifyEmployee(employee));
        jpaPermission.setDateCreated(Timestamp.from(Instant.now()));
        return INSTANCE.mapPermission(jpaPermission);
    }

    /**
     * @author WARITH
     * @dateCreated 01/08/2023
     * @description Gets a permission by Id
     *
     * @param permissionId The Id of the permission
     *
     * @return Optional<Permission>
     */
    @Override
    public Optional<Permission> getPermissionById(String permissionId) {
        return Optional.ofNullable(INSTANCE.mapPermission(getJpaPermissionById(permissionId)));
    }

    /**
     * @author WARITH
     * @dateCreated 01/08/2023
     * @description Gets a permission by name
     *
     * @param permissionName The name of the permission
     *
     * @return Optional<Permission>
     */
    @Override
    public Optional<Permission> getPermissionByName(String permissionName) {
        return Optional.ofNullable(INSTANCE.mapPermission(getJpaPermissionByName(permissionName)));
    }

    /**
     * @author WARITH
     * @dateCreated 01/08/2023
     * @description Gets all permissions
     *
     * @return List<Permission>
     */
    @Override
    public List<Permission> getAllPermissions() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaPermission)
                .fetch()
                .stream()
                .map(INSTANCE::mapPermission)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 01/08/2023
     * @description Updates a permission
     *
     * @param permissionId Id of the permission to update
     * @param permission Permission model containing the data update
     * @param employee Employee making the update
     *
     * @return boolean
     */
    @Override
    public boolean updatePermission(String permissionId, Permission permission, Employee employee) {

        return new JPAQueryFactory(jpaApi.em()).update(qJpaPermission)
                .set(qJpaPermission.permissionName, permission.getPermissionName())
                .set(qJpaPermission.permissionAction, permission.getPermissionAction())
                .set(qJpaPermission.permissionDescription, permission.getPermissionDescription())
                .set(qJpaPermission.modifiedBy, stringifyEmployee(employee, "Updated Permission"))
                .set(qJpaPermission.dateModified, Timestamp.from(Instant.now()))
                .where(qJpaPermission.permissionId.eq(permissionId))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 01/08/2023
     * @description Deletes a permission
     *
     * @param permissionId Permission model ID to delete
     *
     * @return boolean
     */
    @Override
    public boolean deletePermission(String permissionId) {
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaPermission)
                .where(qJpaPermission.permissionId.eq(permissionId))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 01/08/2023
     * @description Gets a JpaPermission by ID (a private method)
     * @param permissionId Permission model ID
     * @return JpaPermission
     */
    private JpaPermission getJpaPermissionById(String permissionId) {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaPermission)
                .where(qJpaPermission.permissionId.eq(permissionId))
                .fetchOne();
    }

    /**
     * @author WARITH
     * @dateCreated 01/08/2023
     * @description Gets a JpaPermission by Name (a private method)
     *
     * @param permissionName Permission model Name
     *
     * @return JpaPermission
     */
    private JpaPermission getJpaPermissionByName(String permissionName) {
        QJpaPermission qJpaPermission = QJpaPermission.jpaPermission;

        JPAQueryFactory queryFactory = new JPAQueryFactory(this.jpaApi.em());

        return queryFactory.selectFrom(qJpaPermission)
                .where(qJpaPermission.permissionName.eq(permissionName))
                .fetchOne();
    }

}
