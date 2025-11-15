package ru.zenbt.educationplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.zenbt.educationplatform.entity.Assignment;
import ru.zenbt.educationplatform.entity.Submission;
import ru.zenbt.educationplatform.service.AssignmentService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<Assignment> createAssignment(
            @RequestParam Long lessonId,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
            @RequestParam Integer maxScore) {
        Assignment assignment = assignmentService.createAssignment(lessonId, title, description, dueDate, maxScore);
        return ResponseEntity.ok(assignment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignment(@PathVariable Long id) {
        Assignment assignment = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(assignment);
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByLesson(@PathVariable Long lessonId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByLesson(lessonId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByCourse(@PathVariable Long courseId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByCourse(courseId);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/{assignmentId}/submit")
    public ResponseEntity<Submission> submitAssignment(
            @PathVariable Long assignmentId,
            @RequestParam Long studentId,
            @RequestParam String content) {
        Submission submission = assignmentService.submitAssignment(assignmentId, studentId, content);
        return ResponseEntity.ok(submission);
    }

    @PutMapping("/submissions/{submissionId}/grade")
    public ResponseEntity<Void> gradeSubmission(
            @PathVariable Long submissionId,
            @RequestParam Integer score,
            @RequestParam(required = false) String feedback) {
        assignmentService.gradeSubmission(submissionId, score, feedback);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{assignmentId}/submissions")
    public ResponseEntity<List<Submission>> getSubmissionsByAssignment(@PathVariable Long assignmentId) {
        List<Submission> submissions = assignmentService.getSubmissionsByAssignment(assignmentId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/submissions/student/{studentId}")
    public ResponseEntity<List<Submission>> getSubmissionsByStudent(@PathVariable Long studentId) {
        List<Submission> submissions = assignmentService.getSubmissionsByStudent(studentId);
        return ResponseEntity.ok(submissions);
    }
}
