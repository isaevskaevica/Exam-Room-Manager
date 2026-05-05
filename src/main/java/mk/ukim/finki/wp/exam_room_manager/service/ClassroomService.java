package mk.ukim.finki.wp.exam_room_manager.service;

import mk.ukim.finki.wp.exam_room_manager.model.Classroom;
import mk.ukim.finki.wp.exam_room_manager.model.enums.ComputerAvailability;
import java.util.List;

public interface ClassroomService {

    List<Classroom> findAll();
    List<Classroom> findByComputerAvailability(ComputerAvailability computerAvailability);
}
