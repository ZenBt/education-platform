package ru.zenbt.educationplatform.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.zenbt.educationplatform.entity.AnswerOption;
import ru.zenbt.educationplatform.entity.CourseModule;
import ru.zenbt.educationplatform.entity.Question;
import ru.zenbt.educationplatform.entity.Quiz;
import ru.zenbt.educationplatform.entity.QuizSubmission;
import ru.zenbt.educationplatform.entity.User;
import ru.zenbt.educationplatform.repository.AnswerOptionRepository;
import ru.zenbt.educationplatform.repository.ModuleRepository;
import ru.zenbt.educationplatform.repository.QuestionRepository;
import ru.zenbt.educationplatform.repository.QuizRepository;
import ru.zenbt.educationplatform.repository.QuizSubmissionRepository;
import ru.zenbt.educationplatform.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerOptionRepository answerOptionRepository;

    @Mock
    private QuizSubmissionRepository quizSubmissionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModuleRepository moduleRepository;

    @InjectMocks
    private QuizService quizService;

    private CourseModule module;
    private Quiz quiz;
    private User student;
    private Question q1, q2;
    private AnswerOption opt1Correct, opt2Incorrect;

    @BeforeEach
    void setUp() {
        module = new CourseModule();
        module.setId(1L);

        quiz = new Quiz(module, "Test Quiz");
        quiz.setId(1L);

        student = new User("Student", "student@test.com", "STUDENT");
        student.setId(1L);

        q1 = new Question(quiz, "Q1?", "single");
        q1.setId(1L);

        q2 = new Question(quiz, "Q2?", "single");
        q2.setId(2L);

        quiz.setQuestions(List.of(q1, q2));

        opt1Correct = new AnswerOption(q1, "A1", true);
        opt1Correct.setId(10L);

        opt2Incorrect = new AnswerOption(q2, "A2", false);
        opt2Incorrect.setId(20L);
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    void shouldCreateQuiz() {
        when(moduleRepository.findById(1L)).thenReturn(Optional.of(module));
        when(quizRepository.save(any(Quiz.class))).thenReturn(quiz);

        Quiz created = quizService.createQuiz(1L, "Quiz", 30);

        assertThat(created).isNotNull();
        verify(quizRepository).save(any(Quiz.class));
    }

    @Test
    void shouldAddQuestionToQuiz() {
        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));
        when(questionRepository.save(any(Question.class))).thenReturn(q1);

        Question saved = quizService.addQuestionToQuiz(1L, "New Q", "single");

        assertThat(saved).isNotNull();
        verify(questionRepository).save(any(Question.class));
    }

    @Test
    void shouldAddAnswerOption() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(q1));
        when(answerOptionRepository.save(any(AnswerOption.class))).thenReturn(opt1Correct);

        AnswerOption saved = quizService.addAnswerOption(1L, "Option", true);

        assertThat(saved).isNotNull();
        verify(answerOptionRepository).save(any(AnswerOption.class));
    }

    @Test
    void shouldThrowWhenQuizAlreadyTaken() {
        when(quizSubmissionRepository.existsByQuizIdAndStudentId(1L, 1L))
                .thenReturn(true);

        assertThatThrownBy(() ->
                quizService.takeQuiz(1L, 1L, Map.of())
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Студент уже прошел этот тест");
    }

    @Test
    void shouldTakeQuizAndCalculateScore() {
        Map<Long, Long> answers = Map.of(
                1L, 10L,  // correct
                2L, 20L   // incorrect
        );

        when(quizSubmissionRepository.existsByQuizIdAndStudentId(1L, 1L))
                .thenReturn(false);
        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(answerOptionRepository.findById(10L)).thenReturn(Optional.of(opt1Correct));
        when(answerOptionRepository.findById(20L)).thenReturn(Optional.of(opt2Incorrect));

        QuizSubmission submission = new QuizSubmission(quiz, student, 50);
        when(quizSubmissionRepository.save(any(QuizSubmission.class)))
                .thenReturn(submission);

        QuizSubmission result = quizService.takeQuiz(1L, 1L, answers);

        assertThat(result).isNotNull();
        assertThat(result.getScore()).isEqualTo(50);
        verify(quizSubmissionRepository).save(any(QuizSubmission.class));
    }

    @Test
    void shouldThrowIfAnswerOptionNotFound() {
        Map<Long, Long> answers = Map.of(1L, 999L);

        when(quizSubmissionRepository.existsByQuizIdAndStudentId(1L, 1L))
                .thenReturn(false);
        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(answerOptionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                quizService.takeQuiz(1L, 1L, answers)
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Вариант ответа не найден");
    }

    @Test
    void shouldReturnQuizSubmissions() {
        when(quizSubmissionRepository.findByQuizId(1L))
                .thenReturn(List.of(new QuizSubmission()));

        List<QuizSubmission> list = quizService.getQuizSubmissions(1L);

        assertThat(list).hasSize(1);
    }

    @Test
    void shouldReturnStudentQuizSubmissions() {
        when(quizSubmissionRepository.findByStudentId(1L))
                .thenReturn(List.of(new QuizSubmission()));

        List<QuizSubmission> list = quizService.getStudentQuizSubmissions(1L);

        assertThat(list).hasSize(1);
    }

    @Test
    void shouldGetQuizByModuleId() {
        when(quizRepository.findByCourseModuleId(1L))
                .thenReturn(Optional.of(quiz));

        Quiz found = quizService.getQuizByModuleId(1L);

        assertThat(found).isNotNull();
    }

    @Test
    void shouldThrowWhenQuizForModuleNotFound() {
        when(quizRepository.findByCourseModuleId(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                quizService.getQuizByModuleId(1L)
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Тест не найден для этого модуля");
    }
}
