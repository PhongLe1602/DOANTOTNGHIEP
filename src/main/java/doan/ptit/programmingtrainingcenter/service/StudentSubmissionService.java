package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.StudentSubmissionInstructorRequest;
import doan.ptit.programmingtrainingcenter.dto.request.StudentSubmissionRequest;
import doan.ptit.programmingtrainingcenter.dto.response.ListSubmissionResponse;
import doan.ptit.programmingtrainingcenter.entity.StudentSubmission;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface StudentSubmissionService {
    StudentSubmission createStudentSubmission(String studentId, StudentSubmissionRequest studentSubmissionRequest) throws IOException;
    ListSubmissionResponse getStudentSubmissionsOfAssignment(String assignmentId);
    StudentSubmission updateStudentSubmission(String studentId, StudentSubmissionRequest studentSubmissionRequest) throws IOException;
    StudentSubmission updateStudentSubmissionByInstructor(StudentSubmissionInstructorRequest studentSubmissionInstructorRequest);
    StudentSubmission getStudentSubmissionsOfStudent(String studentId ,String assignmentId );
}
