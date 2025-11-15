package ru.zenbt.educationplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.zenbt.educationplatform.entity.AnswerOption;
import ru.zenbt.educationplatform.entity.Question;
import ru.zenbt.educationplatform.entity.Quiz;
import ru.zenbt.educationplatform.entity.QuizSubmission;
import ru.zenbt.educationplatform.service.QuizService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(
            @RequestParam Long courseModuleId,
            @RequestParam String title,
            @RequestParam(required = false) Integer timeLimit) {
        Quiz quiz = quizService.createQuiz(courseModuleId, title, timeLimit);
        return ResponseEntity.ok(quiz);
    }

    @PostMapping("/{quizId}/questions")
    public ResponseEntity<Question> addQuestion(
            @PathVariable Long quizId,
            @RequestParam String text,
            @RequestParam String type) {
        Question question = quizService.addQuestionToQuiz(quizId, text, type);
        return ResponseEntity.ok(question);
    }

    @PostMapping("/questions/{questionId}/options")
    public ResponseEntity<AnswerOption> addAnswerOption(
            @PathVariable Long questionId,
            @RequestParam String text,
            @RequestParam boolean isCorrect) {
        AnswerOption option = quizService.addAnswerOption(questionId, text, isCorrect);
        return ResponseEntity.ok(option);
    }

    @PostMapping("/{quizId}/take")
    public ResponseEntity<QuizSubmission> takeQuiz(
            @PathVariable Long quizId,
            @RequestParam Long studentId,
            @RequestBody Map<Long, Long> answers) {
        QuizSubmission submission = quizService.takeQuiz(quizId, studentId, answers);
        return ResponseEntity.ok(submission);
    }

    @GetMapping("/module/{courseModuleId}")
    public ResponseEntity<Quiz> getQuizByModule(@PathVariable Long courseModuleId) {
        Quiz quiz = quizService.getQuizByModuleId(courseModuleId);
        return ResponseEntity.ok(quiz);
    }

    @GetMapping("/{quizId}/submissions")
    public ResponseEntity<List<QuizSubmission>> getQuizSubmissions(@PathVariable Long quizId) {
        List<QuizSubmission> submissions = quizService.getQuizSubmissions(quizId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/submissions/student/{studentId}")
    public ResponseEntity<List<QuizSubmission>> getStudentQuizSubmissions(@PathVariable Long studentId) {
        List<QuizSubmission> submissions = quizService.getStudentQuizSubmissions(studentId);
        return ResponseEntity.ok(submissions);
    }
}
