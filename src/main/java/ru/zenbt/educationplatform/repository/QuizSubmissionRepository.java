package ru.zenbt.educationplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.zenbt.educationplatform.entity.QuizSubmission;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {
    List<QuizSubmission> findByQuizId(Long quizId);

    List<QuizSubmission> findByStudentId(Long studentId);

    Optional<QuizSubmission> findByQuizIdAndStudentId(Long quizId, Long studentId);

    boolean existsByQuizIdAndStudentId(Long quizId, Long studentId);
}
