package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.AssignmentRequest;
import doan.ptit.programmingtrainingcenter.entity.Assignment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface AssignmentService {
    Assignment getAssignmentById(String id);
    List<Assignment> getAssignments();
    Assignment createAssignment(AssignmentRequest assignmentRequest) throws IOException;
    Assignment updateAssignment(String assignmentId ,AssignmentRequest assignmentRequest) throws IOException;
    void deleteAssignment(String assignmentId);
    List<Assignment> getAssignmentsOfStudent(String studentId);
    List<Assignment> getAssignmentsOfInstructor(String instructorId);
}
