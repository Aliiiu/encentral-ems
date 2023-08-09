package com.esl.internship.staffsync.system.configuration.impl;


import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.entities.JpaPermission;
import com.esl.internship.staffsync.entities.JpaRole;
import com.esl.internship.staffsync.entities.QJpaPermission;
import com.esl.internship.staffsync.entities.QJpaRole;
import com.esl.internship.staffsync.system.configuration.api.IRoleApi;
import com.esl.internship.staffsync.system.configuration.dto.RoleDTO;
import com.esl.internship.staffsync.system.configuration.model.Role;
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


public class DefaultRoleApiImpl implements IRoleApi {

    @Inject
    JPAApi jpaApi;

    private static final QJpaRole qJpaRole = QJpaRole.jpaRole;

    /**
     * @author WARITH
     * @dateCreated 04/08/2023
     * @description Creates new role (using a dto)
     *
     * @param roleDto The role data to create new Role from
     * @param employee The employee creating this record
     *
     * @return Role
     */
    @Override
    public Role addRole(RoleDTO roleDto, Employee employee) {
        final JpaRole jpaRole = INSTANCE.mapRoleDto(roleDto);
        jpaRole.setRoleId(UUID.randomUUID().toString());
        jpaRole.setCreatedBy(stringifyEmployee(employee));
        jpaRole.setDateCreated(Timestamp.from(Instant.now()));

        jpaApi.em().persist(jpaRole);
        return INSTANCE.mapRole(jpaRole);
    }

    /**
     * @author WARITH
     * @dateCreated 01/08/2023
     * @description Updates a role with a dto
     *
     * @param roleId The id of the role to update
     * @param roleDto The role data to update from
     * @param employee The employee updating this record
     *
     * @return boolean
     */
    @Override
    public boolean updateRole(String roleId, RoleDTO roleDto, Employee employee) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaRole)
                .set(qJpaRole.roleName, roleDto.getRoleName())
                .set(qJpaRole.roleDescription, roleDto.getRoleDescription())
                .set(qJpaRole.modifiedBy, stringifyEmployee(employee, "Updated Role"))
                .set(qJpaRole.dateModified, Timestamp.from(Instant.now()))
                .where(qJpaRole.roleId.eq(roleId))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 01/08/2023
     * @description Gets a Role by Id
     *
     * @param roleId The id of the Role
     *
     * @return Optional<Role>
     */
    @Override
    public Optional<Role> getRole(String roleId) {
        return Optional.ofNullable(INSTANCE.mapRole(getJpaRoleById(roleId)));
    }

    /**
     * @author WARITH
     * @dateCreated 01/08/2023
     * @description Gets all Roles
     *
     * @return List<Role>
     */
    @Override
    public List<Role> getRoles() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaRole)
                .fetch()
                .stream()
                .map(INSTANCE::mapRole)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 03/08/2023
     * @description Deletes a permission
     *
     * @param roleId Id of the role to delete
     *
     * @return boolean
     */
    @Override
    public boolean deleteRole(String roleId) {
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaRole)
                .where(qJpaRole.roleId.eq(roleId))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 01/08/2023
     * @description Gets a JpaRole by ID (a private method)
     *
     * @param roleId Role model ID
     *
     * @return JpaRole
     */
    private JpaRole getJpaRoleById(String roleId) {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaRole)
                .where(qJpaRole.roleId.eq(roleId))
                .fetchOne();
    }

}
