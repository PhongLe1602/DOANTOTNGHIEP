package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.dto.request.RoleRequest;
import doan.ptit.programmingtrainingcenter.entity.Role;
import doan.ptit.programmingtrainingcenter.repository.RoleRepository;
import doan.ptit.programmingtrainingcenter.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;



    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(String id) {
        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Override
    public Role addRole(RoleRequest roleRequest) {
        Role role = new Role();
        role.setName(roleRequest.getName());
        role.setDescription(roleRequest.getDescription());
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(RoleRequest roleRequest) {
        return null;
    }

    @Override
    public boolean deleteRole(String id) {
        return false;
    }
}
