package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.dto.request.RolePermissionRequest;
import doan.ptit.programmingtrainingcenter.dto.request.RoleRequest;
import doan.ptit.programmingtrainingcenter.entity.Permission;
import doan.ptit.programmingtrainingcenter.entity.Role;
import doan.ptit.programmingtrainingcenter.repository.PermissionRepository;
import doan.ptit.programmingtrainingcenter.repository.RoleRepository;
import doan.ptit.programmingtrainingcenter.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;


    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(String id) {
        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Override
    public Role addRole(RoleRequest roleRequest) {
        Role role = new Role();
        role.setName(roleRequest.getName());
        role.setDescription(roleRequest.getDescription());
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(RoleRequest roleRequest) {
        return null;
    }

    @Override
    public boolean deleteRole(String id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        roleRepository.delete(role);
        return true;
    }

    @Override
    public void addPermission(RolePermissionRequest rolePermissionRequest) {
        // Tìm role
        Role role = roleRepository.findById(rolePermissionRequest.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));


        // Tìm permission
        Permission permission = permissionRepository.findById(rolePermissionRequest.getPermissionId())
                .orElseThrow(() -> new RuntimeException(
                        "Permission not found"));

        if (role.getPermissions() == null) {
            role.setPermissions(new ArrayList<>());
        }
        boolean permissionExists = role.getPermissions().stream()
                .anyMatch(p -> p.getId().equals(rolePermissionRequest.getPermissionId()));

        if (!permissionExists) {
            role.getPermissions().add(permission);
            roleRepository.save(role);
        }
        else {
            throw new RuntimeException("Role already has permission");
        }
    }

    @Override
    public void deletePermission(String roleId, String permissionId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));


        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));


        if (role.getPermissions() != null && role.getPermissions().contains(permission)) {

            role.getPermissions().remove(permission);

            roleRepository.save(role);
        } else {
            throw new RuntimeException("Permission does not exist for this role");
        }
    }



}
