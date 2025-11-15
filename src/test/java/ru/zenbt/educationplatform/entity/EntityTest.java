package ru.zenbt.educationplatform.entity;

import org.junit.jupiter.api.Test;

import ru.zenbt.educationplatform.entity.AnswerOption;
import ru.zenbt.educationplatform.entity.Assignment;
import ru.zenbt.educationplatform.entity.Category;
import ru.zenbt.educationplatform.entity.Course;
import ru.zenbt.educationplatform.entity.CourseModule;
import ru.zenbt.educationplatform.entity.CourseReview;
import ru.zenbt.educationplatform.entity.Enrollment;
import ru.zenbt.educationplatform.entity.Lesson;
import ru.zenbt.educationplatform.entity.Question;
import ru.zenbt.educationplatform.entity.Quiz;
import ru.zenbt.educationplatform.entity.QuizSubmission;
import ru.zenbt.educationplatform.entity.Submission;
import ru.zenbt.educationplatform.entity.Tag;
import ru.zenbt.educationplatform.entity.User;
import ru.zenbt.educationplatform.entity.UserProfile;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class EntityTest {

    @Test
    void shouldCreateUser() {
        User user = new User("John Doe", "john@example.com", "STUDENT");
        
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john@example.com");
        assertThat(user.getRole()).isEqualTo("STUDENT");
    }

    @Test
    void shouldCreateCategory() {
        Category category = new Category("Programming");
        
        assertThat(category.getName()).isEqualTo("Programming");
    }

    @Test
    void shouldCreateCourse() {
        User teacher = new User("Teacher", "teacher@test.com", "TEACHER");
        Category category = new Category("Java");
        Course course = new Course("Java Basics", "Learn Java", category, teacher);
        
        assertThat(course.getTitle()).isEqualTo("Java Basics");
        assertThat(course.getDescription()).isEqualTo("Learn Java");
        assertThat(course.getCategory()).isEqualTo(category);
        assertThat(course.getTeacher()).isEqualTo(teacher);
    }

    @Test
    void shouldCreateEnrollment() {
        User student = new User("Student", "student@test.com", "STUDENT");
        Course course = new Course();
        Enrollment enrollment = new Enrollment(student, course, LocalDate.now(), "Active");
        
        assertThat(enrollment.getStudent()).isEqualTo(student);
        assertThat(enrollment.getCourse()).isEqualTo(course);
        assertThat(enrollment.getStatus()).isEqualTo("Active");
    }

    @Test
    void shouldCreateUserProfile() {
        User user = new User("User", "user@test.com", "STUDENT");
        UserProfile profile = new UserProfile(user, "Bio", "avatar.jpg");
        
        assertThat(profile.getUser()).isEqualTo(user);
        assertThat(profile.getBio()).isEqualTo("Bio");
        assertThat(profile.getAvatarUrl()).isEqualTo("avatar.jpg");
    }

    @Test
    void shouldCreateTag() {
        Tag tag = new Tag("Java");
        
        assertThat(tag.getName()).isEqualTo("Java");
    }

    @Test
    void shouldCreateCourseModule() {
        Course course = new Course();
        CourseModule module = new CourseModule(course, "Module 1", 1);
        
        assertThat(module.getCourse()).isEqualTo(course);
        assertThat(module.getTitle()).isEqualTo("Module 1");
        assertThat(module.getOrderIndex()).isEqualTo(1);
    }

    @Test
    void shouldCreateLesson() {
        CourseModule module = new CourseModule();
        Lesson lesson = new Lesson(module, "Lesson 1", "Content");
        
        assertThat(lesson.getCourseModule()).isEqualTo(module);
        assertThat(lesson.getTitle()).isEqualTo("Lesson 1");
        assertThat(lesson.getContent()).isEqualTo("Content");
    }

    @Test
    void shouldCreateAssignment() {
        Lesson lesson = new Lesson();
        Assignment assignment = new Assignment(lesson, "Assignment 1", "Do this", 
                                             LocalDate.now(), 100);
        
        assertThat(assignment.getLesson()).isEqualTo(lesson);
        assertThat(assignment.getTitle()).isEqualTo("Assignment 1");
        assertThat(assignment.getMaxScore()).isEqualTo(100);
    }

    @Test
    void shouldCreateSubmission() {
        Assignment assignment = new Assignment();
        User student = new User();
        Submission submission = new Submission(assignment, student, "Solution");
        
        assertThat(submission.getAssignment()).isEqualTo(assignment);
        assertThat(submission.getStudent()).isEqualTo(student);
        assertThat(submission.getContent()).isEqualTo("Solution");
    }

    @Test
    void shouldCreateQuiz() {
        CourseModule module = new CourseModule();
        Quiz quiz = new Quiz(module, "Quiz 1");
        
        assertThat(quiz.getCourseModule()).isEqualTo(module);
        assertThat(quiz.getTitle()).isEqualTo("Quiz 1");
    }

    @Test
    void shouldCreateQuestion() {
        Quiz quiz = new Quiz();
        Question question = new Question(quiz, "What is Java?", "SINGLE_CHOICE");
        
        assertThat(question.getQuiz()).isEqualTo(quiz);
        assertThat(question.getText()).isEqualTo("What is Java?");
        assertThat(question.getType()).isEqualTo("SINGLE_CHOICE");
    }

    @Test
    void shouldCreateAnswerOption() {
        Question question = new Question();
        AnswerOption option = new AnswerOption(question, "Programming language", true);
        
        assertThat(option.getQuestion()).isEqualTo(question);
        assertThat(option.getText()).isEqualTo("Programming language");
        assertThat(option.isCorrect()).isTrue();
    }

    @Test
    void shouldCreateQuizSubmission() {
        Quiz quiz = new Quiz();
        User student = new User();
        QuizSubmission submission = new QuizSubmission(quiz, student, 85);
        
        assertThat(submission.getQuiz()).isEqualTo(quiz);
        assertThat(submission.getStudent()).isEqualTo(student);
        assertThat(submission.getScore()).isEqualTo(85);
    }

    @Test
    void shouldCreateCourseReview() {
        Course course = new Course();
        User student = new User();
        CourseReview review = new CourseReview(course, student, 5, "Great course!");
        
        assertThat(review.getCourse()).isEqualTo(course);
        assertThat(review.getStudent()).isEqualTo(student);
        assertThat(review.getRating()).isEqualTo(5);
        assertThat(review.getComment()).isEqualTo("Great course!");
    }
}

