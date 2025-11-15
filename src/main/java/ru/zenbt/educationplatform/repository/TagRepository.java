package ru.zenbt.educationplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.zenbt.educationplatform.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
