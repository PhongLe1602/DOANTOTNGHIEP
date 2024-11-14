package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.UserRoleRequest;
import doan.ptit.programmingtrainingcenter.entity.UserRole;
import doan.ptit.programmingtrainingcenter.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-role")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @GetMapping
    List<UserRole> getUserRoles() {
        return userRoleService.getUserRoles();
    }
    @GetMapping("/{id}")
    UserRole getUserRole(@PathVariable String id) {
        return userRoleService.getUserRole(id);
    }
    @PostMapping
    UserRole addUserRole(@RequestBody UserRoleRequest userRoleRequest) {
        return userRoleService.addUserRole(userRoleRequest);
    }
    @DeleteMapping("/{userId}/role/{roleId}")
    boolean deleteUserRole(@PathVariable String userId, @PathVariable String roleId) {
        return userRoleService.deleteUserRole(userId, roleId);
    }


}
