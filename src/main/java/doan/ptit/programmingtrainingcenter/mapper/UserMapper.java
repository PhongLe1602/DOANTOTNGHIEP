package doan.ptit.programmingtrainingcenter.mapper;

import doan.ptit.programmingtrainingcenter.dto.request.ProfileUserRequest;
import doan.ptit.programmingtrainingcenter.dto.request.UserRequest;
import doan.ptit.programmingtrainingcenter.dto.response.InstructorResponse;
import doan.ptit.programmingtrainingcenter.dto.response.ProfileUserResponse;
import doan.ptit.programmingtrainingcenter.dto.response.UserResponse;
import doan.ptit.programmingtrainingcenter.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "phoneNumber", expression = "java(String.valueOf(userRequest.getPhoneNumber()))")
    @Mapping(target = "profilePicture", ignore = true)
    User toUser(UserRequest userRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "profilePicture", ignore = true)
    void updateUser(@MappingTarget User user, UserRequest userRequest);




    ProfileUserResponse toProfileUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "fullName", source = "profileUserRequest.fullName")
    @Mapping(target = "email", source = "profileUserRequest.email")
    @Mapping(target = "phoneNumber", source = "profileUserRequest.phoneNumber")
    @Mapping(target = "gender", source = "profileUserRequest.gender")
    @Mapping(target = "birthDate", source = "profileUserRequest.birthDate")
    @Mapping(target = "address", source = "profileUserRequest.address")
    @Mapping(target = "bio", source = "profileUserRequest.bio")
    @Mapping(target = "profilePicture", ignore = true)
    void updateProfileUser(@MappingTarget User user , ProfileUserRequest profileUserRequest);


    @Mapping(target = "name", source = "fullName")
    UserResponse toUserResponse(User user);


    List<InstructorResponse> toListInstructorResponse(List<User> listUser);
}