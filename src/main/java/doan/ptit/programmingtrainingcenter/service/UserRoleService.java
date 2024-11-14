package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.UserRoleRequest;
import doan.ptit.programmingtrainingcenter.entity.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserRoleService {
    List<UserRole> getUserRoles();
    UserRole getUserRole(String id);
    UserRole addUserRole(UserRoleRequest userRoleRequest);
    UserRole updateUserRole(UserRoleRequest userRoleRequest);
    boolean deleteUserRole(String userId, String roleId);
}
