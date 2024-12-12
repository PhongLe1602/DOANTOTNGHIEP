package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.PermissionRequest;
import doan.ptit.programmingtrainingcenter.entity.Permission;
import doan.ptit.programmingtrainingcenter.repository.PermissionRepository;
import doan.ptit.programmingtrainingcenter.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;


    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission getPermissionById(String id) {
        return permissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Permission not found"));
    }

    @Override
    public Permission addPermission(PermissionRequest permissionRequest) {
        Permission permission = new Permission();
        permission.setName(permissionRequest.getName());
        permission.setDescription(permissionRequest.getDescription());
        permissionRepository.save(permission);
        return permission;
    }

    @Override
    public Permission updatePermission(PermissionRequest permissionRequest) {
        return null;
    }

    @Override
    public boolean deletePermissionById(String id) {
        return false;
    }
}
