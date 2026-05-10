package mk.ukim.finki.wp.exam_room_manager.repository;

import mk.ukim.finki.wp.exam_room_manager.model.Exam;
import mk.ukim.finki.wp.exam_room_manager.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findAllBySubject(Subject subject);
}