package mk.ukim.finki.wp.exam_room_manager.service;

import mk.ukim.finki.wp.exam_room_manager.model.Professor;
import mk.ukim.finki.wp.exam_room_manager.model.Subject;
import java.util.List;

public interface SubjectService {

    List<Subject> findByProfessor(Professor professor);
    List<Subject> findByLoggedInProfessor();
}
