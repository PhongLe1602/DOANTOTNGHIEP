package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.RolePermissionRequest;
import doan.ptit.programmingtrainingcenter.entity.RolePermission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RolePermissionService {
    List<RolePermission> getRolePermissions();
    RolePermission getRolePermissionById(String id);
    RolePermission addRolePermission(RolePermissionRequest rolePermissionRequest);
    RolePermission updateRolePermission(RolePermissionRequest rolePermissionRequest);
    boolean deleteRolePermission(String id);
}
