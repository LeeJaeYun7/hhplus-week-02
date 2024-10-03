package com.example.apply.course.service;

import com.example.apply.course.domain.Course;
import com.example.apply.course.domain.CourseRegistration;
import com.example.apply.course.dto.AppliedCoursesResponse;
import com.example.apply.course.dto.AvailableCoursesResponse;
import com.example.apply.course.repository.CourseRegistrationRepository;
import com.example.apply.course.repository.CourseRepository;
import com.example.apply.member.domain.Member;
import com.example.apply.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseRegistrationRepository courseRegistrationRepository;

    @InjectMocks
    private CourseService courseService;

    @Mock
    private Member member;

    @Mock
    private Course course;

    @Nested
    @DisplayName("특강을 신청할때")
    class 특강을_신청할때 {

        @Test
        void 멤버와_강의가_존재하는_경우_성공한다() throws Exception {
            given(courseRegistrationRepository.findCourseRegistrationByMemberIdAndCourseId(1L, 1L)).willReturn(Optional.empty());
            given(memberRepository.findById(1L)).willReturn(Optional.of(member));
            given(courseRepository.findById(1L)).willReturn(Optional.of(course));

            boolean result = courseService.applyCourse(1L, 1L);
            assertTrue(result);
        }

        @Test
        void 멤버가_존재하지_않는_경우_실패한다() throws Exception {
            given(courseRegistrationRepository.findCourseRegistrationByMemberIdAndCourseId(1L, 1L)).willReturn(Optional.empty());
            given(memberRepository.findById(1L)).willReturn(Optional.empty());

            assertThrows(Exception.class, () -> courseService.applyCourse(1L, 1L));
        }

        @Test
        void 강의가_존재하지_않는_경우_실패한다() throws Exception {
            given(courseRegistrationRepository.findCourseRegistrationByMemberIdAndCourseId(1L, 1L)).willReturn(Optional.empty());
            given(memberRepository.findById(1L)).willReturn(Optional.of(member));
            given(courseRepository.findById(1L)).willReturn(Optional.empty());

            assertThrows(Exception.class, () -> courseService.applyCourse(1L, 1L));
        }
    }

    @Nested
    @DisplayName("특강을 날짜로 조회할때")
    class 특강을_날짜로_조회할때 {
        @Test
        void 해당_날짜에_특강이_존재하는_경우_성공한다() throws Exception {
            String dateStr = "2024-10-03";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateStr, formatter);

            Course course1 = new Course(1L, "Java Beginner", "Tom Cruise", 10, date);
            Course course2 = new Course(2L, "JavaScript Beginner", "Brad Pitt", 10, date);

            List<Course> courses = List.of(course1, course2);
            given(courseRepository.findByStartDate(date)).willReturn(courses);

            AvailableCoursesResponse availableCoursesResponse = courseService.searchCoursesByDate(date);
            assertEquals(availableCoursesResponse.getAvailableCourses().size(), 2);
        }

        @Test
        void 해당_날짜에_특강이_존재하지_않는_경우_빈_리스트를_반환한다() throws Exception {
            String dateStr = "2024-10-03";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateStr, formatter);

            given(courseRepository.findByStartDate(date)).willReturn(new ArrayList<>());

            AvailableCoursesResponse availableCoursesResponse = courseService.searchCoursesByDate(date);
            assertEquals(availableCoursesResponse.getAvailableCourses().size(), 0);
        }
    }

    @Nested
    @DisplayName("멤버가 등록한 특강을 ID로 조회할때")
    class 멤버가_등록한_특강을_ID로_조회할때 {

        @Test
        void 멤버가_등록한_특강이_존재하는_경우_성공한다() throws Exception {

            Member member = Member.of(1L, "Tom Cruise");
            Course course1 = Course.of(1L, "Java 101", 0);
            Course course2 = Course.of(2L, "Javascript Basic", 0);
            CourseRegistration courseRegistration1 = CourseRegistration.of(member, course1);
            CourseRegistration courseRegistration2 = CourseRegistration.of(member, course2);

            List<CourseRegistration> courseRegistrations = List.of(courseRegistration1, courseRegistration2);
            List<Long> courseIds = List.of(1L, 2L);
            List<Course> courses = List.of(course1, course2);

            given(courseRegistrationRepository.findAllByMemberId(member.getId())).willReturn(courseRegistrations);
            given(courseRepository.findAllById(courseIds)).willReturn(courses);

            AppliedCoursesResponse appliedCoursesResponse = courseService.getAppliedCoursesByMemberId(member.getId());
            assertEquals(appliedCoursesResponse.getAppliedCourses().size(), 2);
        }
    }
}
