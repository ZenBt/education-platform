package ru.zenbt.educationplatform.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.zenbt.educationplatform.entity.*;
import ru.zenbt.educationplatform.repository.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModuleRepository moduleRepository;

    @InjectMocks
    private CourseService courseService;

    private Category category;
    private User teacher;
    private Course course;

    @BeforeEach
    void setUp() {
        category = new Category("Programming");
        category.setId(1L);

        teacher = new User("Teacher", "teacher@test.com", "TEACHER");
        teacher.setId(1L);

        course = new Course("Java Basics", "Learn Java", category, teacher);
        course.setId(1L);
    }

    @Test
    void shouldCreateCourse() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(userRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course created = courseService.createCourse("Java Basics", "Learn Java", 1L, 1L);

        assertThat(created).isNotNull();
        assertThat(created.getTitle()).isEqualTo("Java Basics");
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.createCourse("Java", "Learn", 999L, 1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Категория не найдена");
    }

    @Test
    void shouldThrowExceptionWhenTeacherNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.createCourse("Java", "Learn", 1L, 999L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Преподаватель не найден");
    }

    @Test
    void shouldGetCourseById() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course found = courseService.getCourseById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
        assertThat(found.getTitle()).isEqualTo("Java Basics");
    }

    @Test
    void shouldThrowExceptionWhenCourseNotFound() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.getCourseById(999L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Курс не найден");
    }

    @Test
    void shouldDeleteCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.deleteCourse(1L);

        verify(courseRepository).delete(course);
    }

    @Test
    void shouldUpdateCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course updated = courseService.updateCourse(1L, "New Title", "New Description");

        assertThat(updated.getTitle()).isEqualTo("New Title");
        assertThat(updated.getDescription()).isEqualTo("New Description");
        verify(courseRepository).save(course);
    }
}

