package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.RoleRequest;
import doan.ptit.programmingtrainingcenter.entity.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    List<Role> getRoles();
    Role getRoleById(String id);
    Role addRole(RoleRequest roleRequest);
    Role updateRole(RoleRequest roleRequest);
    boolean deleteRole(String id);
}
