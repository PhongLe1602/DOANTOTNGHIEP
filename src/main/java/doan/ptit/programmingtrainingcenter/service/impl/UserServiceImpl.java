package doan.ptit.programmingtrainingcenter.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import doan.ptit.programmingtrainingcenter.dto.request.BlockUserRequest;
import doan.ptit.programmingtrainingcenter.dto.request.ProfileUserRequest;
import doan.ptit.programmingtrainingcenter.dto.request.UserRequest;
import doan.ptit.programmingtrainingcenter.dto.request.UserRoleRequest;
import doan.ptit.programmingtrainingcenter.dto.response.InstructorResponse;
import doan.ptit.programmingtrainingcenter.dto.response.ProfileUserResponse;
import doan.ptit.programmingtrainingcenter.entity.Role;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.mapper.UserMapper;
import doan.ptit.programmingtrainingcenter.repository.RoleRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.EmailService;
import doan.ptit.programmingtrainingcenter.service.JwtService;
import doan.ptit.programmingtrainingcenter.service.UserService;
import doan.ptit.programmingtrainingcenter.specification.SearchCriteria;
import doan.ptit.programmingtrainingcenter.specification.SpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@Transactional
//@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

    @Value("${app.frontend.url}")
    private String fondEndUrl;

    public Page<User> getAllUsers(List<SearchCriteria> criteriaList, Pageable pageable) {
        SpecificationBuilder<User> builder = new SpecificationBuilder<>();
        for (SearchCriteria criteria : criteriaList) {
            builder.with(criteria.getKey(), criteria.getOperation(), criteria.getValue());
        }
        Specification<User> specification = builder.build();

        return userRepository.findAll(specification, pageable);
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    @Override
    public User createUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Email Already Exists");
        }

        // Random password
        String randomPassword = UUID.randomUUID().toString().substring(0, 8);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Generate activation token
        String token = jwtService.generateActivationToken(userRequest.getEmail());
        String activationLink = fondEndUrl + "/admin/activate-account?token=" + token;

        // Map UserRequest to User
        User user = userMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(randomPassword)); // Set random password
        user.setIsLocked(false);
        user.setIsEnabled(false);

//        // Upload profile picture to Cloudinary
//        if (userRequest.getProfilePicture() != null && !userRequest.getProfilePicture().isEmpty()) {
//            try {
//                Map<?, ?> uploadResult = cloudinary.uploader().upload(userRequest.getProfilePicture().getBytes(), ObjectUtils.emptyMap());
//                user.setProfilePicture((String) uploadResult.get("url"));
//            } catch (Exception e) {
//                throw new RuntimeException("Error uploading profile picture to Cloudinary", e);
//            }
//        }

        // Assign roles
        List<Role> roles = roleRepository.findAllByIdIn(userRequest.getRoleIds());
        System.out.println(userRequest.getRoleIds());
        if (roles.isEmpty()) {
            throw new RuntimeException("Invalid role IDs provided");
        }
        user.setRoles(roles);

        // Save user
        User savedUser = userRepository.save(user);

        // Send activation email with temporary password
        String emailTemplate = """
        Kính gửi %s,
            
        Chúc mừng bạn đã đăng ký thành công tài khoản tại hệ thống của chúng tôi!
            
        🔐 THÔNG TIN ĐĂNG NHẬP
        ───────────────────────
        • Email: %s
        • Mật khẩu tạm thời: %s
            
        ✨ KÍCH HOẠT TÀI KHOẢN
        ───────────────────────
        Vui lòng nhấp vào liên kết dưới đây để kích hoạt tài khoản của bạn:
        %s
            
        Lưu ý:
        • Vui lòng đổi mật khẩu ngay sau khi đăng nhập lần đầu
            
        Nếu bạn cần hỗ trợ thêm, vui lòng liên hệ với chúng tôi qua:
        • Email: support@traincenter.com
        • Hotline: 1900 1007
            
        Trân trọng,
        Đội ngũ hỗ trợ
        """;

        emailService.sendEmail(
                user.getEmail(),
                "Kích hoạt tài khoản - Thông tin đăng nhập",
                String.format(
                        emailTemplate,
                        user.getFullName() != null ? user.getFullName() : "Quý khách",
                        user.getEmail(),
                        randomPassword,
                        activationLink
                )
        );

        return savedUser;
    }


    @Override
    public User updateUser(String id , UserRequest userRequest) {
        User userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        userMapper.updateUser(userEntity,userRequest);
        userEntity.setIsEnabled(true);
        userEntity.setIsLocked(false);

//        if (userRequest.getProfilePicture() != null && !userRequest.getProfilePicture().isEmpty()) {
//            try {
//                Map<?, ?> uploadResult = cloudinary.uploader().upload(userRequest.getProfilePicture().getBytes(), ObjectUtils.emptyMap());
//                userEntity.setProfilePicture((String) uploadResult.get("url")); // Cập nhật URL của profilePicture
//            } catch (Exception e) {
//                throw new RuntimeException("Lỗi khi upload ảnh lên Cloudinary", e);
//            }
//        }

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

    @Override
    public List<User> getAllStudents() {
        return userRepository.findAllStudents();
    }

    @Override
    public List<User> getAllTeachers() {
        return userRepository.findAllTeachers();
    }

    @Override
    public void assignRole(UserRoleRequest userRoleRequest) {
        User user = userRepository.findById(userRoleRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userRoleRequest.getUserId()));

        // Khởi tạo danh sách roles nếu null
        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }

        // Kiểm tra xem role đã tồn tại chưa
        boolean roleExists = user.getRoles().stream()
                .anyMatch(r -> r.getId().equals(userRoleRequest.getRoleId()));

        if (!roleExists) {
            Role newRole = roleRepository.findById(userRoleRequest.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            user.getRoles().add(newRole);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User already has role");
        }
    }

    @Override
    public void deleteRole(String userId,String roleId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));


        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            throw new RuntimeException("User does not have any roles");
        }


        Role roleToRemove = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));


        boolean roleExists = user.getRoles().removeIf(role -> role.getId().equals(roleToRemove.getId()));

        if (!roleExists) {
            throw new RuntimeException("User does not have the specified role");
        }


        userRepository.save(user);
    }

    @Override
    public Boolean blockUser(BlockUserRequest blockUserRequest) {
        User user = userRepository.findById(blockUserRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsLocked(blockUserRequest.isBlocked());
        userRepository.save(user);
        return true;
    }

    @Override
    public ProfileUserResponse getProfile(String userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toProfileUserResponse(user);

    }

    @Override
    public ProfileUserResponse updateProfile(String userId , ProfileUserRequest profileUserRequest) {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateProfileUser(user,profileUserRequest);

        userRepository.save(user);

        return userMapper.toProfileUserResponse(user);
    }

    @Override
    public List<InstructorResponse> getAllInstructors() {
       List<User> listUser = userRepository.findAllTeachers();
       return userMapper.toListInstructorResponse(listUser);
    }


}
