package doan.ptit.programmingtrainingcenter.service.impl;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import doan.ptit.programmingtrainingcenter.dto.request.StudentSubmissionInstructorRequest;
import doan.ptit.programmingtrainingcenter.dto.request.StudentSubmissionRequest;
import doan.ptit.programmingtrainingcenter.dto.response.ListSubmissionResponse;
import doan.ptit.programmingtrainingcenter.entity.Assignment;
import doan.ptit.programmingtrainingcenter.entity.StudentSubmission;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.mapper.StudentSubmissionMapper;
import doan.ptit.programmingtrainingcenter.repository.AssignmentRepository;
import doan.ptit.programmingtrainingcenter.repository.StudentSubmissionRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.StudentSubmissionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class StudentSubmissionServiceImpl implements StudentSubmissionService {
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private StudentSubmissionRepository studentSubmissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentSubmissionMapper studentSubmissionMapper;

    @Override
    public StudentSubmission createStudentSubmission(String studentId , StudentSubmissionRequest studentSubmissionRequest) throws IOException {

        // Lấy thông tin Assignment từ assignmentId
        Assignment assignment = assignmentRepository.findById(studentSubmissionRequest.getAssignmentId())
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tải file bài nộp của học viên lên Cloudinary
        String fileUrl = uploadFileToCloudinary(studentSubmissionRequest.getFile());

        // Tạo đối tượng StudentSubmission
        StudentSubmission submission = StudentSubmission.builder()
                .assignment(assignment)
                .fileUrl(fileUrl)
                .submissionDate(new java.util.Date())
                .score(BigDecimal.valueOf(0))
                .status(StudentSubmission.Status.valueOf("PENDING"))
                .student(student)
                .build();

        // Lưu bài nộp vào cơ sở dữ liệu
        return studentSubmissionRepository.save(submission);
    }

    @Override
    public ListSubmissionResponse getStudentSubmissionsOfAssignment(String assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found"));
        List<StudentSubmission> submissions = studentSubmissionRepository.findByAssignmentId(assignmentId);
        return studentSubmissionMapper.toListSubmissionResponse(assignment, submissions);
    }

    @Override
    public StudentSubmission updateStudentSubmission(String studentId, StudentSubmissionRequest studentSubmissionRequest) throws IOException {
        // Tìm bài nộp của học viên theo assignmentId và studentId
        StudentSubmission existingSubmission = studentSubmissionRepository.findByAssignmentIdAndStudentId(studentSubmissionRequest.getAssignmentId(), studentId);

        // Tải file bài nộp mới lên Cloudinary nếu có file mới
        String fileUrl = null;
        if (studentSubmissionRequest.getFile() != null) {
            fileUrl = uploadFileToCloudinary(studentSubmissionRequest.getFile());
        }

        // Cập nhật thông tin bài nộp
        existingSubmission.setFileUrl(fileUrl != null ? fileUrl : existingSubmission.getFileUrl());  // Nếu không có file mới, giữ nguyên URL cũ
        existingSubmission.setSubmissionDate(new java.util.Date());  // Cập nhật lại ngày nộp

        // Lưu lại bài nộp đã cập nhật
        return studentSubmissionRepository.save(existingSubmission);
    }

    @Override
    public StudentSubmission updateStudentSubmissionByInstructor(StudentSubmissionInstructorRequest studentSubmissionInstructorRequest) {
        // Tìm bài nộp của học viên theo assignmentId và studentId
        StudentSubmission existingSubmission = studentSubmissionRepository.
                findById(studentSubmissionInstructorRequest.getSubmissionId()).orElseThrow(() -> new RuntimeException("Assignment not found"));;


        // Cập nhật điểm số, phản hồi và trạng thái của bài nộp
        existingSubmission.setScore(studentSubmissionInstructorRequest.getScore());
        existingSubmission.setFeedback(studentSubmissionInstructorRequest.getFeedBack());
        existingSubmission.setStatus(studentSubmissionInstructorRequest.getStatus());

        // Lưu lại bài nộp đã cập nhật
        return studentSubmissionRepository.save(existingSubmission);
    }

    @Override
    public StudentSubmission getStudentSubmissionsOfStudent(String studentId, String assignmentId) {
        return studentSubmissionRepository.findByAssignmentIdAndStudentId(assignmentId,studentId);
    }

    // Hàm upload file lên Cloudinary
    private String uploadFileToCloudinary(MultipartFile file) throws IOException {
        String folderName = "programming-training-center-project/submissions"; // Thư mục lưu trữ file trên Cloudinary

        // Upload file lên Cloudinary
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", folderName));

        // Lấy URL file tải lên
        if (uploadResult.containsKey("url")) {
            return (String) uploadResult.get("url");
        } else {
            throw new RuntimeException("Error uploading file to Cloudinary");
        }
    }
}
