package doan.ptit.programmingtrainingcenter.repository;

import doan.ptit.programmingtrainingcenter.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    List<Schedule> findByCourseClassId(String courseId);
}
