package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.dto.request.UserRequest;
import doan.ptit.programmingtrainingcenter.dto.response.AuthResponse;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.mapper.UserMapper;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;


@Service
@Transactional
//@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserRepository userRepository;



    @Autowired
    private UserMapper userMapper;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    @Override
    public User createUser(@RequestBody UserRequest userRequest) {
        if(userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Email Already Exists");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = userMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String id , UserRequest userRequest) {
        User userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setFullName(userRequest.getFullName());
        userEntity.setPhoneNumber(String.valueOf(userRequest.getPhoneNumber()));
        userEntity.setAddress(userRequest.getAddress());
        userEntity.setBio(userRequest.getBio());
        userEntity.setProfilePicture(userRequest.getProfilePicture());
        userEntity.setEmail(userRequest.getEmail());

        return userRepository.save(userEntity);


    }

    @Override
    public Boolean deleteUser(String id) {
        User userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        userRepository.delete(userEntity);
        return true;
    }

    @Override
    public void updateUserAvatar(String id, String avatarUrl) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setProfilePicture(avatarUrl);
        userRepository.save(user);
    }


}
