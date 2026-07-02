package mk.ukim.finki.wp.exam_room_manager.repository;

import mk.ukim.finki.wp.exam_room_manager.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
}