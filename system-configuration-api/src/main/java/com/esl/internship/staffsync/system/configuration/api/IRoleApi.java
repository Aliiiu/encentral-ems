package com.esl.internship.staffsync.system.configuration.api;

import com.encentral.scaffold.commons.model.Employee;
import com.esl.internship.staffsync.system.configuration.dto.RoleDTO;
import com.esl.internship.staffsync.system.configuration.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleApi {
    Role addRole(Role role, Employee employee);
    Role addRole(RoleDTO roleDto, Employee employee);

    boolean updateRole(String roleId, Role role, Employee employee);
    boolean updateRole(String roleId, RoleDTO roleDto, Employee employee);

    Optional<Role> getRole(String roleId);

    List<Role> getRoles();

    boolean deleteRole(String roleId);
}
