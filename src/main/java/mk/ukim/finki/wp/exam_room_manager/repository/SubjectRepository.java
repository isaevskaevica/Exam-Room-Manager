package mk.ukim.finki.wp.exam_room_manager.repository;

import mk.ukim.finki.wp.exam_room_manager.model.Professor;
import mk.ukim.finki.wp.exam_room_manager.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByProfessor(Professor professor);

    @Query("SELECT DISTINCT s.name " +
            "FROM Subject s " +
            "ORDER BY s.name")
    List<String> findDistinctNames();
}