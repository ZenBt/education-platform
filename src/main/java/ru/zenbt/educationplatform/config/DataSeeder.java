package ru.zenbt.educationplatform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ru.zenbt.educationplatform.entity.*;
import ru.zenbt.educationplatform.repository.*;
import ru.zenbt.educationplatform.service.AssignmentService;
import ru.zenbt.educationplatform.service.CourseService;
import ru.zenbt.educationplatform.service.EnrollmentService;
import ru.zenbt.educationplatform.service.QuizService;

import java.time.LocalDate;

@Component
@Profile("!test")
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CourseReviewRepository courseReviewRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public void run(String... args) {
        seedData();
    }

    private void seedData() {
        User teacher = userRepository.save(new User("Dr. Smith", "smith@university.edu", "TEACHER"));
        User student1 = userRepository.save(new User("Alice Johnson", "alice@student.edu", "STUDENT"));
        User student2 = userRepository.save(new User("Bob Wilson", "bob@student.edu", "STUDENT"));

        UserProfile teacherProfile = new UserProfile(teacher, "Опытный разработчик Java", "avatar1.jpg");
        UserProfile student1Profile = new UserProfile(student1, "Студент информатики", "avatar2.jpg");
        profileRepository.save(teacherProfile);
        profileRepository.save(student1Profile);

        Category programming = categoryRepository.save(new Category("Программирование"));
        Category databases = categoryRepository.save(new Category("Базы данных"));

        Tag javaTag = tagRepository.save(new Tag("Java"));
        Tag springTag = tagRepository.save(new Tag("Spring"));
        Tag hibernateTag = tagRepository.save(new Tag("Hibernate"));

        Course javaCourse = courseService.createCourse(
                "Основы Java программирования",
                "Комплексное введение в Java",
                programming.getId(),
                teacher.getId()
        );
        javaCourse.setStartDate(LocalDate.now().minusDays(30));

        Course hibernateCourse = courseService.createCourse(
                "Мастерство Hibernate ORM",
                "Продвинутые концепции Hibernate и JPA",
                databases.getId(),
                teacher.getId()
        );
        hibernateCourse.setStartDate(LocalDate.now().minusDays(15));

        courseService.addTagToCourse(javaCourse.getId(), javaTag.getId());
        courseService.addTagToCourse(hibernateCourse.getId(), hibernateTag.getId());
        courseService.addTagToCourse(hibernateCourse.getId(), springTag.getId());

        CourseModule javaBasics = courseService.addModuleToCourse(javaCourse.getId(), "Основы Java", 1);
        CourseModule javaOop = courseService.addModuleToCourse(javaCourse.getId(), "ООП", 2);

        CourseModule hibernateIntro = courseService.addModuleToCourse(hibernateCourse.getId(), "Введение в Hibernate", 1);
        CourseModule jpaMappings = courseService.addModuleToCourse(hibernateCourse.getId(), "Маппинги JPA", 2);

        Lesson lesson1 = new Lesson(javaBasics, "Переменные и типы данных", "Изучение переменных Java...");
        lesson1.setVideoUrl("https://example.com/video1");
        lessonRepository.save(lesson1);

        enrollmentService.enrollStudent(javaCourse.getId(), student1.getId());
        enrollmentService.enrollStudent(javaCourse.getId(), student2.getId());
        enrollmentService.enrollStudent(hibernateCourse.getId(), student1.getId());

        Assignment assignment1 = assignmentService.createAssignment(
                lesson1.getId(),
                "Реализовать Hello World",
                "Создать программу, выводящую 'Hello World'",
                LocalDate.now().plusDays(7),
                100
        );

        Submission submission1 = assignmentService.submitAssignment(assignment1.getId(), student1.getId(),
                "public class Main { public static void main(String[] args) { System.out.println(\"Hello World\"); } }");

        assignmentService.gradeSubmission(submission1.getId(), 95, "Хорошо!");

        Quiz quiz = quizService.createQuiz(hibernateIntro.getId(), "Тест по основам Hibernate", 30);

        Question question1 = quizService.addQuestionToQuiz(quiz.getId(),
                "Что такое ORM?", "SINGLE_CHOICE");

        quizService.addAnswerOption(question1.getId(), "Object-Relational Mapping", true);
        quizService.addAnswerOption(question1.getId(), "Object-Relative Model", false);
        quizService.addAnswerOption(question1.getId(), "Online Resource Manager", false);

        CourseReview review1 = new CourseReview(javaCourse, student1, 5,
                "Отличный курс!");
        courseReviewRepository.save(review1);

        CourseReview review2 = new CourseReview(hibernateCourse, student1, 4,
                "Хороший курс, но нужны больше практических примеров.");
        courseReviewRepository.save(review2);

    }
}
