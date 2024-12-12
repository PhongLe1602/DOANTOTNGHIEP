package doan.ptit.programmingtrainingcenter.service;

import doan.ptit.programmingtrainingcenter.dto.request.BlockUserRequest;
import doan.ptit.programmingtrainingcenter.dto.request.ProfileUserRequest;
import doan.ptit.programmingtrainingcenter.dto.request.UserRequest;
import doan.ptit.programmingtrainingcenter.dto.request.UserRoleRequest;
import doan.ptit.programmingtrainingcenter.dto.response.ProfileUserResponse;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.specification.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {


    Page<User> getAllUsers(List<SearchCriteria> criteriaList, Pageable pageable);
    User getUserById(String id);
    User createUser(UserRequest userRequest);
    User updateUser(String id ,UserRequest userRequest);
    Boolean deleteUser(String id);
    void updateUserAvatar(String id, String avatarUrl);
    List<User> getAllStudents();
    List<User> getAllTeachers();
    void addRole(UserRoleRequest userRoleRequest);
    void deleteRole(String userId,String roleId);
    Boolean blockUser(BlockUserRequest blockUserRequest);

    ProfileUserResponse getProfile(String userId);

    ProfileUserResponse updateProfile(String userid , ProfileUserRequest profileUserRequest);
    



}
