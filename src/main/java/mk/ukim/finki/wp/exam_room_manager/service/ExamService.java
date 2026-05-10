package mk.ukim.finki.wp.exam_room_manager.service;

import mk.ukim.finki.wp.exam_room_manager.model.Exam;
import mk.ukim.finki.wp.exam_room_manager.model.Subject;
import java.time.LocalDate;
import java.time.LocalTime;

public interface ExamService {
    Exam createExam(LocalDate exam_date, LocalTime start_time, int duration, int number_of_students, Subject subject);
}
