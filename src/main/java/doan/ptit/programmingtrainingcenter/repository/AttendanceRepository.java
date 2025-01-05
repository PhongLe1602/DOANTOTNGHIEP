package doan.ptit.programmingtrainingcenter.repository;


import doan.ptit.programmingtrainingcenter.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, String> {
    Attendance findBySession_IdAndStudent_Id(String sessionId, String studentId);
    List<Attendance> findBySessionId(String sessionId);
    List<Attendance> findByStudentId(String studentId);
    boolean existsBySession_IdAndStudent_Id(String sessionId, String studentId);
}
