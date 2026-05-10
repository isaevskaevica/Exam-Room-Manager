package mk.ukim.finki.wp.exam_room_manager.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wp.exam_room_manager.model.Classroom;
import mk.ukim.finki.wp.exam_room_manager.model.Exam;
import mk.ukim.finki.wp.exam_room_manager.model.Reservation;
import mk.ukim.finki.wp.exam_room_manager.model.exceptions.ConflictException;
import mk.ukim.finki.wp.exam_room_manager.model.exceptions.ReservationNotFoundException;
import mk.ukim.finki.wp.exam_room_manager.repository.ExamRepository;
import mk.ukim.finki.wp.exam_room_manager.repository.ReservationRepository;
import mk.ukim.finki.wp.exam_room_manager.service.ClassroomService;
import mk.ukim.finki.wp.exam_room_manager.service.ReservationService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClassroomService classroomService;
    private final ExamRepository examRepository;

    @Override
    public boolean hasConflict(Reservation reservation) {
        LocalDate date = reservation.getExam().getExamDate();
        Classroom classroom = reservation.getClassroom();
        List<Reservation> existing = reservationRepository.findAllByClassroomAndExam_ExamDate(classroom, date);

        LocalTime newStart = reservation.getExam().getStartTime();
        LocalTime newEnd = newStart.plusMinutes(reservation.getExam().getDuration());

        for (Reservation r : existing) {
            LocalTime existingStart = r.getExam().getStartTime();
            LocalTime existingEnd = existingStart.plusMinutes(r.getExam().getDuration());

            if (existingStart.isBefore(newEnd) && existingEnd.isAfter(newStart)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Reservation> saveAll(List<Reservation> reservations) {
        for (Reservation reservation : reservations)
        {
            if (hasConflict(reservation))
            {
                throw new ConflictException(reservation.getClassroom().getName());
            }
        }
        return reservationRepository.saveAll(reservations);
    }

    public List<Reservation> findAllBySubjectName(String subjectName) {
        return reservationRepository.findAll().stream()
                .filter(r -> r.getExam().getSubject().getName().equals(subjectName))
                .toList();
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException(id));
    }

    @Override
    public Reservation update(Long id, LocalTime startTime, int duration, int numberOfStudents, Long classroomId) {
        Reservation existingReservation = reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException(id));
        existingReservation.getExam().setStartTime(startTime);
        existingReservation.getExam().setDuration(duration);
        existingReservation.getExam().setNumberOfStudents(numberOfStudents);
        existingReservation.setClassroom(classroomService.findById(classroomId));
        return reservationRepository.save(existingReservation);
    }

    @Override
    public void deleteById(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow();
        Exam exam = reservation.getExam();
        reservationRepository.delete(reservation);
        examRepository.delete(exam);
    }
}
