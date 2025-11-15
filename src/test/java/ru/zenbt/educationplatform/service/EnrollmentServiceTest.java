package ru.zenbt.educationplatform.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.zenbt.educationplatform.entity.*;
import ru.zenbt.educationplatform.repository.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private User student;
    private Course course;
    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        student = new User("Student", "student@test.com", "STUDENT");
        student.setId(1L);

        course = new Course();
        course.setId(1L);
        course.setTitle("Test Course");

        enrollment = new Enrollment(student, course, LocalDate.now(), "Active");
        enrollment.setId(1L);
    }

    @Test
    void shouldEnrollStudent() {
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        Enrollment result = enrollmentService.enrollStudent(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getStudent()).isEqualTo(student);
        assertThat(result.getCourse()).isEqualTo(course);
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void shouldThrowExceptionWhenAlreadyEnrolled() {
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> enrollmentService.enrollStudent(1L, 1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Студент уже записан на этот курс");

        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void shouldUnenrollStudent() {
        when(enrollmentRepository.findByStudentIdAndCourseId(1L, 1L))
            .thenReturn(Optional.of(enrollment));

        enrollmentService.unenrollStudent(1L, 1L);

        verify(enrollmentRepository).delete(enrollment);
    }

    @Test
    void shouldCheckIfStudentEnrolled() {
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(true);

        boolean enrolled = enrollmentService.isStudentEnrolled(1L, 1L);

        assertThat(enrolled).isTrue();
    }
}

