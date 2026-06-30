package mk.ukim.finki.wp.exam_room_manager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "reservations")
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @Column(name = "students_assigned")
    private int studentsAssigned;

    public Reservation(Classroom classroom, Exam exam) {
        this.classroom = classroom;
        this.exam = exam;
    }

    public Reservation(Classroom classroom, Exam exam, int studentsAssigned) {
        this.classroom = classroom;
        this.exam = exam;
        this.studentsAssigned = studentsAssigned;
    }
}