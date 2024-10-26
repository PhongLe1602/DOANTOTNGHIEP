package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.UserRequest;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.service.CloudinaryService;
import doan.ptit.programmingtrainingcenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping
    List<User> getAllUsers() {
        return userService.getAllUsers();
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
    User updateUser(@PathVariable String id, @RequestBody UserRequest userRequest) {
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

}
