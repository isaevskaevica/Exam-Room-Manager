package mk.ukim.finki.wp.exam_room_manager.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wp.exam_room_manager.model.Classroom;
import mk.ukim.finki.wp.exam_room_manager.model.Exam;
import mk.ukim.finki.wp.exam_room_manager.model.Reservation;
import mk.ukim.finki.wp.exam_room_manager.model.Subject;
import mk.ukim.finki.wp.exam_room_manager.model.enums.ComputerAvailability;
import mk.ukim.finki.wp.exam_room_manager.repository.ReservationRepository;
import mk.ukim.finki.wp.exam_room_manager.repository.SubjectRepository;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/exam")
public class ExamController {

    private final SubjectService subjectService;
    private final ClassroomService classroomService;
    private final ExamService examService;
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    @GetMapping("/options")
    public String options(@RequestParam Long subjectId, Model model) {
        model.addAttribute("subject", subjectService.findById(subjectId));
        return "options";
    }

    @GetMapping("/classrooms")
    public String classrooms(@RequestParam Long subjectId,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                             @RequestParam int duration,
                             @RequestParam int numberOfStudents,
                             @RequestParam(required = false) ComputerAvailability computerAvailability,
                             Model model) {

        List<Classroom> classrooms;

        if (computerAvailability == null)
        {
            classrooms = classroomService.findAll();
        }
        else
            classrooms = classroomService.findByComputerAvailability(computerAvailability);

        LocalTime endTime = startTime.plusMinutes(duration);
        Set<Long> unavailableIds = classrooms.stream()
                .filter(classroom -> {
                    List<Reservation> existing = reservationRepository
                            .findAllByClassroomAndExam_ExamDate(classroom, date);
                    return existing.stream().anyMatch(r -> {
                        LocalTime existingStart = r.getExam().getStartTime();
                        LocalTime existingEnd = existingStart.plusMinutes(r.getExam().getDuration());
                        return existingStart.isBefore(endTime) && existingEnd.isAfter(startTime);
                    });
                })
                .map(Classroom::getId)
                .collect(Collectors.toSet());

        model.addAttribute("classrooms", classrooms);
        model.addAttribute("unavailableIds", unavailableIds);
        model.addAttribute("subject", subjectService.findById(subjectId));
        model.addAttribute("date", date);
        model.addAttribute("startTime", startTime);
        model.addAttribute("duration", duration);
        model.addAttribute("numberOfStudents", numberOfStudents);
        model.addAttribute("computerAvailabilities", ComputerAvailability.values());
        model.addAttribute("selectedFilter", computerAvailability);

        return "classrooms";
    }

    @PostMapping("/reservations")
    public String reservations(@RequestParam Long subjectId,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                          @RequestParam int duration,
                          @RequestParam int numberOfStudents,
                          @RequestParam List<Long> classroomIds) {

        Subject subject = subjectService.findById(subjectId);

        List<Classroom> selectedClassrooms = classroomIds.stream()
                .map(classroomService::findById)
                .sorted(Comparator.comparing(Classroom::getCapacity).reversed())
                .toList();

        List<Reservation> reservations = new ArrayList<>();

        int remainingStudents = numberOfStudents;

        for (Classroom classroom : selectedClassrooms){
            int assignedStudents = Math.min(remainingStudents, classroom.getCapacity());
            Exam exam = examService.createExam(date, startTime, duration, numberOfStudents, subject);
            reservations.add(new Reservation(classroom, exam, assignedStudents));
            remainingStudents -= assignedStudents;

            if (remainingStudents <= 0){
                break;
            }
        }

        reservationService.saveAll(reservations);
        return "redirect:/reservations";
    }
}