package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.ClassStudentRequest;
import doan.ptit.programmingtrainingcenter.entity.ClassStudent;
import org.springframework.stereotype.Service;

@Service
public interface ClassStudentService {

    ClassStudent addUsertoClassStudent(String  userId ,ClassStudentRequest classStudentRequest);
}
