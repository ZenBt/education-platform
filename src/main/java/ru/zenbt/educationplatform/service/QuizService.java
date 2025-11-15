package ru.zenbt.educationplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.zenbt.educationplatform.entity.*;
import ru.zenbt.educationplatform.repository.*;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Autowired
    private QuizSubmissionRepository quizSubmissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    public Quiz createQuiz(Long courseModuleId, String title, Integer timeLimit) {
        CourseModule courseModule = moduleRepository.findById(courseModuleId)
                .orElseThrow(() -> new RuntimeException("Модуль не найден"));

        Quiz quiz = new Quiz(courseModule, title);
        quiz.setTimeLimit(timeLimit);
        return quizRepository.save(quiz);
    }

    public Question addQuestionToQuiz(Long quizId, String text, String type) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Тест не найден"));

        Question question = new Question(quiz, text, type);
        return questionRepository.save(question);
    }

    public AnswerOption addAnswerOption(Long questionId, String text, boolean isCorrect) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Вопрос не найден"));

        AnswerOption option = new AnswerOption(question, text, isCorrect);
        return answerOptionRepository.save(option);
    }

    public QuizSubmission takeQuiz(Long quizId, Long studentId, Map<Long, Long> answers) {
        if (quizSubmissionRepository.existsByQuizIdAndStudentId(quizId, studentId)) {
            throw new RuntimeException("Студент уже прошел этот тест");
        }

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Тест не найден"));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Студент не найден"));

        int correctAnswers = 0;
        for (Question question : quiz.getQuestions()) {
            Long selectedOptionId = answers.get(question.getId());
            if (selectedOptionId != null) {
                AnswerOption selectedOption = answerOptionRepository.findById(selectedOptionId)
                        .orElseThrow(() -> new RuntimeException("Вариант ответа не найден"));
                if (selectedOption.isCorrect()) {
                    correctAnswers++;
                }
            }
        }

        int score = !quiz.getQuestions().isEmpty() ?
                (correctAnswers * 100) / quiz.getQuestions().size() : 0;

        QuizSubmission submission = new QuizSubmission(quiz, student, score);
        return quizSubmissionRepository.save(submission);
    }

    public List<QuizSubmission> getQuizSubmissions(Long quizId) {
        return quizSubmissionRepository.findByQuizId(quizId);
    }

    public List<QuizSubmission> getStudentQuizSubmissions(Long studentId) {
        return quizSubmissionRepository.findByStudentId(studentId);
    }

    public Quiz getQuizByModuleId(Long courseModuleId) {
        return quizRepository.findByCourseModuleId(courseModuleId)
                .orElseThrow(() -> new RuntimeException("Тест не найден для этого модуля"));
    }
}
