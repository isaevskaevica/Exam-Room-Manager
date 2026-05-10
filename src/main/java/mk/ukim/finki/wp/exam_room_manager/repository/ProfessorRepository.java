package mk.ukim.finki.wp.exam_room_manager.repository;

import mk.ukim.finki.wp.exam_room_manager.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByUsername(String username);
}