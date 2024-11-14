package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.PermissionRequest;
import doan.ptit.programmingtrainingcenter.entity.Permission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissionService {
    List<Permission> getAllPermissions();
    Permission getPermissionById(String id);
    Permission addPermission(PermissionRequest permissionRequest);
    Permission updatePermission(PermissionRequest permissionRequest);
    boolean deletePermissionById(String id);
}
