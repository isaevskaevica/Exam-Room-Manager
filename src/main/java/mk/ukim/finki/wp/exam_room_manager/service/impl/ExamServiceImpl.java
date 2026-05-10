package mk.ukim.finki.wp.exam_room_manager.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wp.exam_room_manager.model.Exam;
import mk.ukim.finki.wp.exam_room_manager.model.Subject;
import mk.ukim.finki.wp.exam_room_manager.repository.ExamRepository;
import mk.ukim.finki.wp.exam_room_manager.service.ExamService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    @Override
    public Exam createExam(LocalDate exam_date, LocalTime start_time, int duration, int number_of_students, Subject subject) {
        Exam exam = new Exam(exam_date, start_time, duration, number_of_students, subject);
        return examRepository.save(exam);
    }

}
