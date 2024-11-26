package doan.ptit.programmingtrainingcenter.controller;

import doan.ptit.programmingtrainingcenter.dto.request.ClassStudentRequest;
import doan.ptit.programmingtrainingcenter.entity.ClassStudent;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.service.ClassStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/class-students")
public class ClassStudentController {
    @Autowired
    private ClassStudentService classStudentService;

    @PostMapping
    public ClassStudent addUserToClass(@RequestBody  ClassStudentRequest classStudentRequest) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return classStudentService.addUsertoClassStudent(currentUser.getId(), classStudentRequest);

    }


}
