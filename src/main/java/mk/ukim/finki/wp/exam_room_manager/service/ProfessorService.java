package mk.ukim.finki.wp.exam_room_manager.service;

import mk.ukim.finki.wp.exam_room_manager.model.Professor;

public interface ProfessorService {
    Professor findByUsername(String username);
}
