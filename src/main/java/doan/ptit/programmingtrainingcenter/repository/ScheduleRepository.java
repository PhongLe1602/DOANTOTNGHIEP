package doan.ptit.programmingtrainingcenter.repository;

import doan.ptit.programmingtrainingcenter.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    List<Schedule> findByCourseClassId(String courseId);
    @Query("SELECT s FROM Schedule s " +
            "JOIN s.courseClass cc " +
            "JOIN ClassStudent cs ON cc.id = cs.courseClass.id " +
            "WHERE cs.student.id = :userId")
    List<Schedule> findSchedulesByUserId(@Param("userId") String userId);

    @Query("SELECT s FROM Schedule s " +
            "JOIN s.courseClass cc " +
            "WHERE cc.instructor.id = :instructorId")
    List<Schedule> findSchedulesByInstructorId(@Param("instructorId") String instructorId);

    @Query("SELECT COUNT(s) FROM Schedule s " +
            "WHERE s.courseClass.instructor.id = :instructorId " +
            "AND s.sessionDate BETWEEN :startDate AND :endDate")
    long countByInstructorAndDateRange(
            @Param("instructorId") String instructorId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
}
