package ru.zenbt.educationplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.zenbt.educationplatform.entity.Course;
import ru.zenbt.educationplatform.entity.CourseModule;
import ru.zenbt.educationplatform.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<Course> createCourse(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Long categoryId,
            @RequestParam Long teacherId) {
        Course course = courseService.createCourse(title, description, categoryId, teacherId);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/{id}/with-modules")
    public ResponseEntity<Course> getCourseWithModules(@PathVariable Long id) {
        Course course = courseService.getCourseWithModules(id);
        return ResponseEntity.ok(course);
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Course>> getCoursesByCategory(@PathVariable Long categoryId) {
        List<Course> courses = courseService.getCoursesByCategory(categoryId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Course>> getCoursesByTeacher(@PathVariable Long teacherId) {
        List<Course> courses = courseService.getCoursesByTeacher(teacherId);
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String description) {
        Course course = courseService.updateCourse(id, title, description);
        return ResponseEntity.ok(course);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{courseId}/modules")
    public ResponseEntity<CourseModule> addModule(
            @PathVariable Long courseId,
            @RequestParam String title,
            @RequestParam Integer orderIndex) {
        CourseModule courseModule = courseService.addModuleToCourse(courseId, title, orderIndex);
        return ResponseEntity.ok(courseModule);
    }

    @GetMapping("/{courseId}/modules")
    public ResponseEntity<List<CourseModule>> getCourseModules(@PathVariable Long courseId) {
        List<CourseModule> modules = courseService.getCourseModules(courseId);
        return ResponseEntity.ok(modules);
    }

    @PostMapping("/{courseId}/tags/{tagId}")
    public ResponseEntity<Void> addTagToCourse(@PathVariable Long courseId, @PathVariable Long tagId) {
        courseService.addTagToCourse(courseId, tagId);
        return ResponseEntity.ok().build();
    }
}
