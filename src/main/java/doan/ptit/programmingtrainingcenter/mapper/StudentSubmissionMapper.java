package doan.ptit.programmingtrainingcenter.mapper;

import doan.ptit.programmingtrainingcenter.dto.response.AssignmentResponse;
import doan.ptit.programmingtrainingcenter.dto.response.ListSubmissionResponse;
import doan.ptit.programmingtrainingcenter.dto.response.StudentSubmissionResponse;
import doan.ptit.programmingtrainingcenter.entity.Assignment;
import doan.ptit.programmingtrainingcenter.entity.StudentSubmission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentSubmissionMapper {

    @Mapping(source = "student", target = "student")
    @Mapping(source = "student.fullName", target = "student.name")
    StudentSubmissionResponse toStudentSubmissionResponse(StudentSubmission studentSubmission);

    List<StudentSubmissionResponse> toStudentSubmissionResponseList(List<StudentSubmission> studentSubmissions);

    default ListSubmissionResponse toListSubmissionResponse(Assignment assignment, List<StudentSubmission> studentSubmissions) {
        return ListSubmissionResponse.builder()
                .assignment(toAssignmentResponse(assignment))
                .studentSubmissions(toStudentSubmissionResponseList(studentSubmissions))
                .build();
    }

    @Mapping(source = "courseClass", target = "courseClass")
    @Mapping(source = "courseClass.course.title", target = "courseClass.courseName")
    @Mapping(source = "courseClass.instructor.fullName", target = "courseClass.instructorName")
    AssignmentResponse toAssignmentResponse(Assignment assignment);
}