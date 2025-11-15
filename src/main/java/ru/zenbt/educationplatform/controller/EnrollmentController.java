package ru.zenbt.educationplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.zenbt.educationplatform.entity.Course;
import ru.zenbt.educationplatform.entity.Enrollment;
import ru.zenbt.educationplatform.service.EnrollmentService;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<Enrollment> enrollStudent(
            @RequestParam Long courseId,
            @RequestParam Long studentId) {
        Enrollment enrollment = enrollmentService.enrollStudent(courseId, studentId);
        return ResponseEntity.ok(enrollment);
    }

    @DeleteMapping
    public ResponseEntity<Void> unenrollStudent(
            @RequestParam Long courseId,
            @RequestParam Long studentId) {
        enrollmentService.unenrollStudent(courseId, studentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getStudentEnrollments(@PathVariable Long studentId) {
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(studentId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getCourseEnrollments(@PathVariable Long courseId) {
        List<Enrollment> enrollments = enrollmentService.getCourseEnrollments(courseId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/student/{studentId}/courses")
    public ResponseEntity<List<Course>> getStudentCourses(@PathVariable Long studentId) {
        List<Course> courses = enrollmentService.getStudentCourses(studentId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isStudentEnrolled(
            @RequestParam Long courseId,
            @RequestParam Long studentId) {
        boolean enrolled = enrollmentService.isStudentEnrolled(courseId, studentId);
        return ResponseEntity.ok(enrolled);
    }
}
