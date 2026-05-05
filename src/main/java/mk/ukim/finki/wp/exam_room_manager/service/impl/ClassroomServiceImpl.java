package mk.ukim.finki.wp.exam_room_manager.service.impl;

import mk.ukim.finki.wp.exam_room_manager.model.Classroom;
import mk.ukim.finki.wp.exam_room_manager.model.enums.ComputerAvailability;
import mk.ukim.finki.wp.exam_room_manager.service.ClassroomService;

import java.util.List;

public class ClassroomServiceImpl implements ClassroomService {
    @Override
    public List<Classroom> findAll() {
        return List.of();
    }

    @Override
    public List<Classroom> findByComputerAvailability(ComputerAvailability computerAvailability) {
        return List.of();
    }
}
