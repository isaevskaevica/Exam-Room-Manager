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
import java.time.LocalTime;
import java.util.List;

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
                                   Model model,
                                   @AuthenticationPrincipal UserDetails userDetails) {
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

        model.addAttribute("reservations", reservations);
        model.addAttribute("subjects", subjects);
        model.addAttribute("selectedSubjectName", subjectName);
        model.addAttribute("loggedInProfessor", professor);
        return "reservations";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetails userDetails) {
        Professor loggedIn = professorService.findByUsername(userDetails.getUsername());
        Reservation reservation = reservationService.findById(id);

        if (!reservation.getExam().getSubject().getProfessor().getId().equals(loggedIn.getId())) {
            return "redirect:/reservations";
        }

        reservationService.deleteById(id);
        return "redirect:/reservations";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id,
                           Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {
        Professor loggedIn = professorService.findByUsername(userDetails.getUsername());
        Reservation reservation = reservationService.findById(id);

        if (!reservation.getExam().getSubject().getProfessor().getId().equals(loggedIn.getId())) {
            return "redirect:/reservations";
        }

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
}
