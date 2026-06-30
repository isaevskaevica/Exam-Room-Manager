CREATE TABLE professors (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    professor_role VARCHAR(100) NOT NULL DEFAULT 'ROLE_PROFESSOR'
);

CREATE TABLE subjects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(100) NOT NULL,
    professor_id BIGINT REFERENCES professors(id)
);

CREATE TABLE classrooms (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    computer_availability VARCHAR(100) NOT NULL
);

CREATE TABLE exams (
    id BIGSERIAL PRIMARY KEY,
    exam_date DATE NOT NULL,
    start_time TIME NOT NULL,
    duration INT NOT NULL,
    number_of_students INT NOT NULL,
    subject_id BIGINT REFERENCES subjects(id)
);

CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    classroom_id BIGINT REFERENCES classrooms(id),
    exam_id BIGINT REFERENCES exams(id),
    students_assigned INT NOT NULL
);