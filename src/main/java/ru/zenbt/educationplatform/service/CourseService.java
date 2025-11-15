package ru.zenbt.educationplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.zenbt.educationplatform.entity.*;
import ru.zenbt.educationplatform.repository.*;

import java.util.List;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModuleRepository courseModuleRepository;

    @Autowired
    private TagRepository tagRepository;

    public Course createCourse(String title, String description, Long categoryId, Long teacherId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Преподаватель не найден"));

        Course course = new Course(title, description, category, teacher);
        return courseRepository.save(course);
    }

    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Курс не найден"));
    }

    public Course getCourseWithModules(Long courseId) {
        return getCourseById(courseId);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getCoursesByCategory(Long categoryId) {
        return courseRepository.findByCategoryId(categoryId);
    }

    public List<Course> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    public Course updateCourse(Long courseId, String title, String description) {
        Course course = getCourseById(courseId);
        course.setTitle(title);
        course.setDescription(description);
        return courseRepository.save(course);
    }

    public void deleteCourse(Long courseId) {
        Course course = getCourseById(courseId);
        courseRepository.delete(course);
    }

    public CourseModule addModuleToCourse(Long courseId, String title, Integer orderIndex) {
        Course course = getCourseById(courseId);
        CourseModule courseModule = new CourseModule(course, title, orderIndex);
        return courseModuleRepository.save(courseModule);
    }

    public List<CourseModule> getCourseModules(Long courseId) {
        return courseModuleRepository.findByCourseIdOrderByOrderIndex(courseId);
    }

    public void addTagToCourse(Long courseId, Long tagId) {
        Course course = getCourseById(courseId);
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Тег не найден"));
        course.getTags().add(tag);
        courseRepository.save(course);
    }
}
