package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.BlockUserRequest;
import doan.ptit.programmingtrainingcenter.dto.request.ProfileUserRequest;
import doan.ptit.programmingtrainingcenter.dto.request.UserRequest;
import doan.ptit.programmingtrainingcenter.dto.request.UserRoleRequest;
import doan.ptit.programmingtrainingcenter.dto.response.*;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.service.CloudinaryService;
import doan.ptit.programmingtrainingcenter.service.UserService;
import doan.ptit.programmingtrainingcenter.specification.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping
    public ApiPageResponse<User> getAllUsers(
            @RequestParam(required = false) String criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        List<Sort.Order> orders = new ArrayList<>();
        try {
            Sort.Direction direction = Sort.Direction.valueOf(sortDirection.trim().toUpperCase());
            orders.add(new Sort.Order(direction, sortBy));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid sort direction: " + sortDirection + ". Use 'asc' or 'desc'.");
        }


        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        List<SearchCriteria> criteriaList = new ArrayList<>();
        if (criteria != null && !criteria.isEmpty()) {
            String[] criteriaArray = criteria.split(";");
            for (String criterion : criteriaArray) {
                String[] parts = criterion.split(",");
                if (parts.length == 3) {
                    criteriaList.add(new SearchCriteria(parts[0], parts[1], parts[2]));
                }
            }
        }

        Page<User> users = userService.getAllUsers(criteriaList, pageable);
        return ApiPageResponse.success(users, "Users retrieved successfully");
    }



    @GetMapping("/{id}")
    User getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }
    @PostMapping
    User createUser(@RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }
    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @ModelAttribute UserRequest userRequest) {
        return userService.updateUser(id, userRequest);
    }
    @DeleteMapping("/{id}")
    Boolean deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }
    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String id = userDetails.getId();
            String url = cloudinaryService.uploadFile(file);
            userService.updateUserAvatar(id, url);
            return ResponseEntity.ok(url);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Upload failed");
        }
    }

    @GetMapping("/students")
    public List<User> getAllStudents() {
        return userService.getAllStudents();
    }

    @GetMapping("/instructors")
    public List<User> getAllTeachers() {
        return userService.getAllTeachers();
    }

    @PostMapping("/role")
    public boolean addRole(@RequestBody UserRoleRequest userRoleRequest) {
        userService.assignRole(userRoleRequest);
        return true;
    }
    @DeleteMapping("/{userId}/role/{roleId}")
    public boolean deleteRole(@PathVariable String userId, @PathVariable String roleId) {
        userService.deleteRole(userId,roleId);
        return true;
    }

    @PostMapping("/block")
    public boolean blockUser(@RequestBody BlockUserRequest blockUserRequest) {
        return userService.blockUser(blockUserRequest);
    }

    @GetMapping("/profile")
    public ProfileUserResponse getUserProfile() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.getProfile(currentUser.getId());
    }
    @PutMapping("/profile")
    public ProfileUserResponse updateUserProfile(@RequestBody ProfileUserRequest profileUserRequest) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.updateProfile(currentUser.getId(), profileUserRequest);
    }

    @GetMapping("/home/instructors")
    public List<InstructorResponse> getInstructors() {
        return userService.getAllInstructors();
    }

}
