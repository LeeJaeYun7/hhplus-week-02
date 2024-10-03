package com.example.apply.course.repository;

import com.example.apply.course.domain.Course;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByStartDate(LocalDate date);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Course c WHERE c.id = :courseId")
    Optional<Course> findByIdWithLock(@Param("courseId") Long courseId);
}
