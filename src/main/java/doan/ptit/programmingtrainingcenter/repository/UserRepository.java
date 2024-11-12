package doan.ptit.programmingtrainingcenter.repository;

import doan.ptit.programmingtrainingcenter.entity.Role;
import doan.ptit.programmingtrainingcenter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    // Tìm tất cả người dùng có vai trò là học sinh
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'STUDENT'")
    List<User> findAllStudents();

    // Tìm tất cả người dùng có vai trò là giảng viên
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'INSTRUCTOR'")
    List<User> findAllTeachers();
}
