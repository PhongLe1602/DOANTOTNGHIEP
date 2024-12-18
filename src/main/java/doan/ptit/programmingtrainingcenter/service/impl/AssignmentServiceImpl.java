package doan.ptit.programmingtrainingcenter.service.impl;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import doan.ptit.programmingtrainingcenter.dto.request.AssignmentRequest;
import doan.ptit.programmingtrainingcenter.entity.Assignment;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.repository.AssignmentRepository;
import doan.ptit.programmingtrainingcenter.repository.CourseClassRepository;
import doan.ptit.programmingtrainingcenter.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private CourseClassRepository courseClassRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Override
    public Assignment getAssignmentById(String id) {
        return assignmentRepository.findById(id) .orElseThrow(() -> new RuntimeException("CourseClass not found"));
    }

    @Override
    public List<Assignment> getAssignments() {
        return assignmentRepository.findAll();
    }

    @Override
    public Assignment createAssignment(AssignmentRequest assignmentRequest) throws IOException {
        // Lấy đối tượng CourseClass từ classId
        CourseClass courseClass = courseClassRepository.findById(assignmentRequest.getClassId())
                .orElseThrow(() -> new RuntimeException("CourseClass not found"));

        // Kiểm tra xem có file đính kèm không
        String fileUrl = null;
        if (assignmentRequest.getFile() != null && !assignmentRequest.getFile().isEmpty()) {
            fileUrl = uploadFileToCloudinary(assignmentRequest.getFile());
        }

        // Tạo đối tượng Assignment mới từ request
        Assignment assignment = Assignment.builder()
                .courseClass(courseClass)
                .title(assignmentRequest.getTitle())
                .description(assignmentRequest.getDescription())
                .type(assignmentRequest.getType())
                .dueDate(assignmentRequest.getDueDate())
                .fileUrl(fileUrl)  // Đưa URL file tải lên
                .build();

        // Lưu Assignment vào cơ sở dữ liệu
        return assignmentRepository.save(assignment);
    }

    @Override
    public Assignment updateAssignment(String assignmentId ,AssignmentRequest assignmentRequest) throws IOException {
        // Tìm Assignment từ cơ sở dữ liệu
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Cập nhật các thuộc tính nếu có
        if (assignmentRequest.getClassId() != null) {
            assignment.setCourseClass(
                    courseClassRepository.findById(assignmentRequest.getClassId())
                            .orElseThrow(() -> new RuntimeException("CourseClass not found"))
            );
        }
        if (assignmentRequest.getTitle() != null) assignment.setTitle(assignmentRequest.getTitle());
        if (assignmentRequest.getDescription() != null) assignment.setDescription(assignmentRequest.getDescription());
        if (assignmentRequest.getType() != null) assignment.setType(assignmentRequest.getType());
        if (assignmentRequest.getDueDate() != null) assignment.setDueDate(assignmentRequest.getDueDate());

        // Cập nhật file nếu có
        if (assignmentRequest.getFile() != null && !assignmentRequest.getFile().isEmpty()) {
            assignment.setFileUrl(uploadFileToCloudinary(assignmentRequest.getFile()));
        }

        // Lưu thay đổi
        return assignmentRepository.save(assignment);
    }

    @Override
    public void deleteAssignment(String assignmentId) {
        // Tìm bài tập theo ID
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Xóa bài tập
        assignmentRepository.delete(assignment);
    }

    @Override
    public List<Assignment> getAssignmentsOfStudent(String studentId) {
        return assignmentRepository.findAssignmentsByStudentId(studentId);
    }


    private String uploadFileToCloudinary(MultipartFile file) throws IOException {
        String folderName = "programming-training-center-project/assignments"; // Thư mục đích trong Cloudinary

        // Upload file lên Cloudinary
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", folderName));

        // Kiểm tra kết quả upload
        if (uploadResult.containsKey("url")) {
            return (String) uploadResult.get("url");
        } else {
            throw new RuntimeException("Error uploading file to Cloudinary");
        }
    }

}
