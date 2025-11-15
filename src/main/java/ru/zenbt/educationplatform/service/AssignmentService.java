package ru.zenbt.educationplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.zenbt.educationplatform.entity.Assignment;
import ru.zenbt.educationplatform.entity.Lesson;
import ru.zenbt.educationplatform.entity.Submission;
import ru.zenbt.educationplatform.entity.User;
import ru.zenbt.educationplatform.repository.AssignmentRepository;
import ru.zenbt.educationplatform.repository.LessonRepository;
import ru.zenbt.educationplatform.repository.SubmissionRepository;
import ru.zenbt.educationplatform.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    public Assignment createAssignment(Long lessonId, String title, String description,
                                       LocalDate dueDate, Integer maxScore) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Урок не найден"));

        Assignment assignment = new Assignment(lesson, title, description, dueDate, maxScore);
        return assignmentRepository.save(assignment);
    }

    public Assignment getAssignmentById(Long assignmentId) {
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Задание не найдено"));
    }

    public List<Assignment> getAssignmentsByLesson(Long lessonId) {
        return assignmentRepository.findByLessonId(lessonId);
    }

    public List<Assignment> getAssignmentsByCourse(Long courseId) {
        return assignmentRepository.findByCourseId(courseId);
    }

    public Submission submitAssignment(Long assignmentId, Long studentId, String content) {
        if (submissionRepository.existsByAssignmentIdAndStudentId(assignmentId, studentId)) {
            throw new RuntimeException("Студент уже отправил это задание");
        }

        Assignment assignment = getAssignmentById(assignmentId);
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Студент не найден"));

        Submission submission = new Submission(assignment, student, content);
        return submissionRepository.save(submission);
    }

    public void gradeSubmission(Long submissionId, Integer score, String feedback) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Решение не найдено"));
        submission.setScore(score);
        submission.setFeedback(feedback);
        submissionRepository.save(submission);
    }

    public List<Submission> getSubmissionsByAssignment(Long assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId);
    }

    public List<Submission> getSubmissionsByStudent(Long studentId) {
        return submissionRepository.findByStudentId(studentId);
    }
}
