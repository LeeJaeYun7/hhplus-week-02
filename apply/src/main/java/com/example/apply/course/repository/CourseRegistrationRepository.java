package com.example.apply.course.repository;

import com.example.apply.course.domain.CourseRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Long> {
    Optional<CourseRegistration> findCourseRegistrationByMemberIdAndCourseId(long memberId, long courseId);
    List<CourseRegistration> findAllByMemberId(long memberId);
}
