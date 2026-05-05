package mk.ukim.finki.wp.exam_room_manager.service.impl;

import mk.ukim.finki.wp.exam_room_manager.model.Reservation;
import mk.ukim.finki.wp.exam_room_manager.service.ReservationService;

import java.util.List;

public class ReservationServiceImpl implements ReservationService {
    @Override
    public boolean hasConflict(Reservation reservation) {
        return false;
    }

    @Override
    public List<Reservation> saveAll(List<Reservation> reservations) {
        return List.of();
    }
}
