package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.UserRoleRequest;
import doan.ptit.programmingtrainingcenter.entity.Role;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.entity.UserRole;
import doan.ptit.programmingtrainingcenter.repository.RoleRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRoleRepository;
import doan.ptit.programmingtrainingcenter.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<UserRole> getUserRoles() {
        return userRoleRepository.findAll();
    }

    @Override
    public UserRole getUserRole(String id) {
        return userRoleRepository.findById(id).orElseThrow(() -> new RuntimeException("UserRole not found"));
    }

    @Override
    public UserRole addUserRole(UserRoleRequest userRoleRequest) {
        UserRole userRole = new UserRole();
        User user = userRepository.findById(userRoleRequest.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(userRoleRequest.getRoleId()).orElseThrow(() -> new RuntimeException("Role not found"));
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleRepository.save(userRole);
        return userRole;
    }

    @Override
    public UserRole updateUserRole(UserRoleRequest userRoleRequest) {
        return null;
    }

    @Override
    public boolean deleteUserRole(String userId, String roleId) {
        UserRole userRole = userRoleRepository.findByUserIdAndRoleId(userId, roleId)
                .orElseThrow(() -> new RuntimeException("UserRole not found"));
        userRoleRepository.delete(userRole);
        return true;
    }
}
