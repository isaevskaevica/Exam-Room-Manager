package mk.ukim.finki.wp.exam_room_manager.service.impl;

import mk.ukim.finki.wp.exam_room_manager.model.Professor;
import mk.ukim.finki.wp.exam_room_manager.model.Subject;
import mk.ukim.finki.wp.exam_room_manager.service.SubjectService;

import java.util.List;

public class SubjectServiceImpl implements SubjectService {
    @Override
    public List<Subject> findByProfessor(Professor professor) {
        return List.of();
    }

    @Override
    public List<Subject> findByLoggedInProfessor() {
        return List.of();
    }
}
