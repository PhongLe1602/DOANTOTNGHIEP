package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.dto.request.RolePermissionRequest;
import doan.ptit.programmingtrainingcenter.entity.Permission;
import doan.ptit.programmingtrainingcenter.entity.Role;
import doan.ptit.programmingtrainingcenter.entity.RolePermission;
import doan.ptit.programmingtrainingcenter.repository.PermissionRepository;
import doan.ptit.programmingtrainingcenter.repository.RolePermissionRepository;
import doan.ptit.programmingtrainingcenter.repository.RoleRepository;
import doan.ptit.programmingtrainingcenter.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;


    @Override
    public List<RolePermission> getRolePermissions() {
        return rolePermissionRepository.findAll();
    }

    @Override
    public RolePermission getRolePermissionById(String id) {
        return rolePermissionRepository.findById(id).orElseThrow(() -> new RuntimeException("RolePermission not found"));
    }

    @Override
    public RolePermission updateRolePermission(RolePermissionRequest rolePermissionRequest) {
        return null;
    }

    @Override
    public RolePermission addRolePermission(RolePermissionRequest rolePermissionRequest) {
        RolePermission rolePermission = new RolePermission();
        Role role = roleRepository.findById(rolePermissionRequest.getRoleId()).orElseThrow(() -> new RuntimeException("Role not found"));
        Permission permission = permissionRepository.findById(rolePermissionRequest.getPermissionId()).orElseThrow(() -> new RuntimeException("Role not found"));
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);
        rolePermissionRepository.save(rolePermission);
        return rolePermission;
    }

    @Override
    public boolean deleteRolePermission(String id) {
        return false;
    }
}
