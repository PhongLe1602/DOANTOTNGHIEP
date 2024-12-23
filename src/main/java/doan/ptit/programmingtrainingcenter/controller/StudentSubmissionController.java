package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.StudentSubmissionInstructorRequest;
import doan.ptit.programmingtrainingcenter.dto.request.StudentSubmissionRequest;
import doan.ptit.programmingtrainingcenter.dto.response.ApiResponse;
import doan.ptit.programmingtrainingcenter.dto.response.ListSubmissionResponse;
import doan.ptit.programmingtrainingcenter.dto.response.StudentSubmissionResponse;
import doan.ptit.programmingtrainingcenter.entity.StudentSubmission;
import doan.ptit.programmingtrainingcenter.mapper.StudentSubmissionMapper;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.service.StudentSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/student-submissions")
public class StudentSubmissionController {

    @Autowired
    private StudentSubmissionService studentSubmissionService;

    @Autowired
    private StudentSubmissionMapper studentSubmissionMapper;

    @PostMapping
    public ApiResponse<StudentSubmission> createStudentSubmission(@ModelAttribute StudentSubmissionRequest request) throws IOException {

        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        StudentSubmission submission = studentSubmissionService.createStudentSubmission(currentUser.getId(),request);
        return ApiResponse.success("Nộp bài thành công", submission);
    }

    @GetMapping("/assignments/{id}")
    public ListSubmissionResponse getStudentSubmissions(@PathVariable String id)  {
        return studentSubmissionService.getStudentSubmissionsOfAssignment(id);
    }


    @PutMapping()
    public ApiResponse<StudentSubmission> updateStudentSubmission(
            @ModelAttribute StudentSubmissionRequest studentSubmissionRequest) throws IOException {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        StudentSubmission updatedSubmission = studentSubmissionService.updateStudentSubmission(currentUser.getId(), studentSubmissionRequest);
        return ApiResponse.success("Cập nhật thành công", updatedSubmission);
    }

    @PutMapping("/instructor")
    public ApiResponse<StudentSubmission> updateStudentSubmissionByInstructor(
            @RequestBody StudentSubmissionInstructorRequest studentSubmissionInstructorRequest) {
        StudentSubmission updatedSubmission = studentSubmissionService.updateStudentSubmissionByInstructor(studentSubmissionInstructorRequest);
        return ApiResponse.success("Cập nhật thành công", updatedSubmission);
    }

    @GetMapping("/assignments/{id}/student")
    public StudentSubmissionResponse getStudentSubmissionsOfStudent(@PathVariable String id)  {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        StudentSubmission studentSubmission = studentSubmissionService.getStudentSubmissionsOfStudent(currentUser.getId(),id);
        return studentSubmissionMapper.toStudentSubmissionResponse(studentSubmission);
    }

}
