package mk.ukim.finki.wp.exam_room_manager.web;


import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wp.exam_room_manager.model.Classroom;
import mk.ukim.finki.wp.exam_room_manager.model.Exam;
import mk.ukim.finki.wp.exam_room_manager.model.Reservation;
import mk.ukim.finki.wp.exam_room_manager.model.Subject;
import mk.ukim.finki.wp.exam_room_manager.model.enums.ComputerAvailability;
import mk.ukim.finki.wp.exam_room_manager.model.enums.ExamType;
import mk.ukim.finki.wp.exam_room_manager.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/exam")
public class ExamController {

    private final SubjectService subjectService;
    private final ClassroomService classroomService;
    private final ExamService examService;
    private final ReservationService reservationService;

    @GetMapping("/calendar")
    public String calendar(@RequestParam Long subjectId, Model model) {
        model.addAttribute("subject", subjectService.findById(subjectId));
        return "calendar";
    }

    @GetMapping("/classrooms")
    public String classrooms(@RequestParam Long subjectId,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             @RequestParam(required = false) ComputerAvailability computerAvailability,
                             Model model) {

        List<Classroom> classrooms;

        if (computerAvailability == null)
        {
            classrooms = classroomService.findAll();
        }
        else
            classrooms = classroomService.findByComputerAvailability(computerAvailability);

        model.addAttribute("classrooms", classrooms);
        model.addAttribute("examTypes", ExamType.values());
        model.addAttribute("subject", subjectService.findById(subjectId));
        model.addAttribute("date", date);
        model.addAttribute("computerAvailabilities", ComputerAvailability.values());
        model.addAttribute("selectedFilter", computerAvailability);

        return "classrooms";
    }

    @PostMapping("/reserve")
    public String reserve(@RequestParam Long subjectId,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                          @RequestParam ExamType examType,
                          @RequestParam int duration,
                          @RequestParam int numberOfStudents,
                          @RequestParam List<Long> classroomIds) {

        Subject subject = subjectService.findById(subjectId);
        Exam exam = examService.createExam(date, startTime, examType, duration, numberOfStudents, subject);
        List<Reservation> reservations = classroomIds.stream().map(id -> new Reservation(classroomService.findById(id), exam)).toList();

        reservationService.saveAll(reservations);
        return "redirect:/dashboard";
    }
}