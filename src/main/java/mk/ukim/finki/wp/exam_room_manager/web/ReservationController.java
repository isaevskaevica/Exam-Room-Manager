package mk.ukim.finki.wp.exam_room_manager.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wp.exam_room_manager.model.Professor;
import mk.ukim.finki.wp.exam_room_manager.model.Reservation;
import mk.ukim.finki.wp.exam_room_manager.model.Subject;
import mk.ukim.finki.wp.exam_room_manager.repository.SubjectRepository;
import mk.ukim.finki.wp.exam_room_manager.service.ClassroomService;
import mk.ukim.finki.wp.exam_room_manager.service.ProfessorService;
import mk.ukim.finki.wp.exam_room_manager.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ClassroomService classroomService;
    private final ProfessorService professorService;
    private final SubjectRepository subjectRepository;

    @GetMapping
    public String listReservations(@RequestParam(required = false) String subjectName,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate examDate,
                                   @RequestParam(required = false, defaultValue = "mine") String scope,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   Model model) {
        Professor professor = professorService.findByUsername(userDetails.getUsername());

        List<String> distinctNames = subjectRepository.findDistinctNames();
        List<Subject> subjects = distinctNames.stream()
                .map(name -> subjectRepository.findAll()
                        .stream()
                        .filter(s -> s.getName().equals(name))
                        .findFirst()
                        .orElseThrow())
                .toList();

        List<Reservation> reservations;

        if (subjectName == null || subjectName.isEmpty())
        {
            reservations = reservationService.findAll();
        }
        else
            reservations = reservationService.findAllBySubjectName(subjectName);

        if (!"all".equals(scope)){
            reservations = reservations.stream()
                    .filter(reservation -> reservation.getExam().getSubject().getProfessor().getId().equals(professor.getId()))
                    .toList();
        }

        model.addAttribute("reservations", reservations);
        model.addAttribute("subjects", subjects);
        model.addAttribute("selectedSubjectName", subjectName);
        model.addAttribute("selectedDate", examDate);
        model.addAttribute("selectedScope", scope);
        model.addAttribute("loggedInProfessor", professor);
        return "reservations";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return "redirect:/reservations";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id,
                           Model model) {
        Reservation reservation = reservationService.findById(id);
        model.addAttribute("reservation", reservation);
        model.addAttribute("classrooms", classroomService.findAll());
        return "edit-reservation";
    }

    @PostMapping("/edit/{id}")
    public String editSave(@PathVariable Long id,
                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                           @RequestParam int duration,
                           @RequestParam int numberOfStudents,
                           @RequestParam Long classroomId) {
        reservationService.update(id, startTime, duration, numberOfStudents, classroomId);
        return "redirect:/reservations";
    }

    @GetMapping("/api")
    @ResponseBody
    public List<Map<String, Object>> getReservationsApi(
            @RequestParam(required = false) String subjectName,
            @RequestParam(required = false, defaultValue = "mine") String scope,
            @AuthenticationPrincipal UserDetails userDetails) {

        Professor professor = professorService.findByUsername(userDetails.getUsername());

        List<Reservation> reservations;
        if (subjectName == null || subjectName.isEmpty()) {
            reservations = reservationService.findAll();
        } else {
            reservations = reservationService.findAllBySubjectName(subjectName);
        }

        if (!"all".equals(scope)) {
            reservations = reservations.stream()
                    .filter(r -> r.getExam().getSubject().getProfessor().getId().equals(professor.getId()))
                    .toList();
        }

        return reservations.stream().map(r -> {
            Map<String, Object> event = new HashMap<>();
            event.put("id", r.getId());
            event.put("title", r.getExam().getSubject().getName());
            event.put("start", r.getExam().getExamDate().toString());
            event.put("extendedProps", Map.of(
                    "classroom", r.getClassroom().getName(),
                    "startTime", r.getExam().getStartTime().toString(),
                    "duration", r.getExam().getDuration(),
                    "capacity", r.getClassroom().getCapacity(),
                    "assigned", r.getStudentsAssigned(),
                    "professor", r.getExam().getSubject().getProfessor().getFullName(),
                    "professorId", r.getExam().getSubject().getProfessor().getId()
            ));
            return event;
        }).toList();
    }

}
