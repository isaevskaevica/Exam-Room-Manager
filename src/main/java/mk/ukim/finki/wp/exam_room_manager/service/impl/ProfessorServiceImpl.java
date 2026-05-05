package mk.ukim.finki.wp.exam_room_manager.service.impl;

import mk.ukim.finki.wp.exam_room_manager.model.Professor;
import mk.ukim.finki.wp.exam_room_manager.repository.ProfessorRepository;
import mk.ukim.finki.wp.exam_room_manager.service.ProfessorService;

public class ProfessorServiceImpl implements ProfessorService {

    private ProfessorRepository professorRepository;

    public ProfessorServiceImpl(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Override
    public Professor findByUsername(String username) {
        return professorRepository.findByUsername(username).orElseThrow();
    }
}
