package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.RolePermissionRequest;
import doan.ptit.programmingtrainingcenter.dto.request.RoleRequest;
import doan.ptit.programmingtrainingcenter.entity.Role;
import doan.ptit.programmingtrainingcenter.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    List<Role> getRoles() {
        return roleService.getRoles();
    }
    @GetMapping("/{id}")
    Role getRole(@PathVariable String id) {
        return roleService.getRoleById(id);
    }

    @PostMapping
    Role addRole(@RequestBody RoleRequest roleRequest) {
        return roleService.addRole(roleRequest);
    }

    @PostMapping("/permission")
    Boolean addRolePermission(@RequestBody RolePermissionRequest rolePermissionRequest) {
        roleService.addPermission(rolePermissionRequest);
        return true;
    }
    @DeleteMapping("/{roleId}/permission/{permissionId}")
    Boolean deleteRolePermission(@PathVariable String roleId, @PathVariable String permissionId) {
        roleService.deletePermission(roleId, permissionId);
        return true;
    }
    @DeleteMapping("/{id}")
    Boolean deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
        return true;
    }

}
