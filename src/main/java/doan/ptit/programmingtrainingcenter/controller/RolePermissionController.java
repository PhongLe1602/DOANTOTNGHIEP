package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.RolePermissionRequest;
import doan.ptit.programmingtrainingcenter.entity.RolePermission;
import doan.ptit.programmingtrainingcenter.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role-permission")
public class RolePermissionController {
    @Autowired
    private RolePermissionService rolePermissionService;

    @GetMapping
    List<RolePermission> getRolePermissions() {
        return rolePermissionService.getRolePermissions();
    }
    @GetMapping("/{id}")
    RolePermission getRolePermissionById(@PathVariable String id) {
        return rolePermissionService.getRolePermissionById(id);
    }
    @PostMapping
    RolePermission addRolePermission(@RequestBody RolePermissionRequest rolePermissionRequest) {
        return rolePermissionService.addRolePermission(rolePermissionRequest);
    }
}
