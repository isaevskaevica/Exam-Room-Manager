package mk.ukim.finki.wp.exam_room_manager.service;

import mk.ukim.finki.wp.exam_room_manager.model.Reservation;
import java.util.List;

public interface ReservationService {

    boolean hasConflict(Reservation reservation);

    List<Reservation> saveAll(List<Reservation> reservations);
}
