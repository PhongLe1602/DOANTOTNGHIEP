package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.PermissionRequest;
import doan.ptit.programmingtrainingcenter.entity.Permission;
import doan.ptit.programmingtrainingcenter.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;


    @GetMapping
    List<Permission> getPermissions() {
        return permissionService.getAllPermissions();
    }

    @GetMapping("/{id}")
    Permission getPermissionById(@PathVariable String id) {
        return permissionService.getPermissionById(id);
    }

    @PostMapping
    Permission addPermission(@RequestBody PermissionRequest permissionRequest) {
        return permissionService.addPermission(permissionRequest);
    }


}
