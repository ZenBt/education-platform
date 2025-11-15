package ru.zenbt.educationplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.zenbt.educationplatform.entity.Course;
import ru.zenbt.educationplatform.entity.Enrollment;
import ru.zenbt.educationplatform.entity.User;
import ru.zenbt.educationplatform.repository.CourseRepository;
import ru.zenbt.educationplatform.repository.EnrollmentRepository;
import ru.zenbt.educationplatform.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Enrollment enrollStudent(Long courseId, Long studentId) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new RuntimeException("Студент уже записан на этот курс");
        }

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Студент не найден"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Курс не найден"));

        Enrollment enrollment = new Enrollment(student, course, LocalDate.now(), "Активен");
        return enrollmentRepository.save(enrollment);
    }

    public void unenrollStudent(Long courseId, Long studentId) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Запись не найдена"));
        enrollmentRepository.delete(enrollment);
    }

    public List<Enrollment> getStudentEnrollments(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getCourseEnrollments(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    public List<Course> getStudentCourses(Long studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return enrollments.stream()
                .map(Enrollment::getCourse)
                .toList();
    }

    public boolean isStudentEnrolled(Long courseId, Long studentId) {
        return enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }
}
