package doan.ptit.programmingtrainingcenter.repository;


import doan.ptit.programmingtrainingcenter.entity.Consult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultRepository extends JpaRepository<Consult, String> {
}
