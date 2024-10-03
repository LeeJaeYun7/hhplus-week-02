package com.example.apply.course.service;

import com.example.apply.course.domain.Course;
import com.example.apply.course.domain.CourseRegistration;
import com.example.apply.course.dto.AppliedCourseResponse;
import com.example.apply.course.dto.AppliedCoursesResponse;
import com.example.apply.course.dto.AvailableCoursesResponse;
import com.example.apply.course.dto.CourseResponse;
import com.example.apply.course.repository.CourseRegistrationRepository;
import com.example.apply.course.repository.CourseRepository;
import com.example.apply.member.domain.Member;
import com.example.apply.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class CourseService {

    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;
    private final CourseRegistrationRepository courseRegistrationRepository;
    public CourseService(MemberRepository memberRepository, CourseRepository courseRepository, CourseRegistrationRepository courseRegistrationRepository){
        this.memberRepository = memberRepository;
        this.courseRepository = courseRepository;
        this.courseRegistrationRepository = courseRegistrationRepository;
    }
    @Transactional
    public boolean applyCourse(long memberId, long courseId) throws Exception {

        Optional<CourseRegistration> courseRegistration = courseRegistrationRepository.findCourseRegistrationByMemberIdAndCourseId(memberId, courseId);

        if(courseRegistration.isPresent()){
            throw new Exception("User Already Registered On Course");
        }

        Member member = memberRepository.findById(memberId)
                                        .orElseThrow(() -> new Exception("Member Not Found"));

        Course course = courseRepository.findById(courseId)
                                        .orElseThrow(() -> new Exception("Member Not Found"));

        course.increaseTotalCount();

        CourseRegistration newCourseRegistration = CourseRegistration.of(member, course);

        courseRegistrationRepository.save(newCourseRegistration);

        return true;
    }

    public AvailableCoursesResponse searchCoursesByDate(LocalDate date) throws Exception {

        List<Course> courses = courseRepository.findByStartDate(date);
        List<CourseResponse> courseResponses = new ArrayList<>();

        for(Course course: courses){
            if(course.getTotalCount() == 30){
                continue;
            }
            CourseResponse courseResponse = CourseResponse.of(course.getTitle(), 30-course.getTotalCount(), course.getStartDate());
            courseResponses.add(courseResponse);
        }

        return new AvailableCoursesResponse(courseResponses);
    }

    public AppliedCoursesResponse getAppliedCoursesByMemberId(long memberId) throws Exception {
        List<Long> courseIds = courseRegistrationRepository.findAllByMemberId(memberId)
                                                           .stream()
                                                           .map(CourseRegistration::getCourse)
                                                           .map(Course::getId)
                                                           .toList();

        List<Course> appliedCourses = courseRepository.findAllById(courseIds);
        List<AppliedCourseResponse> appliedCourseResponses = new ArrayList<>();

        for(Course course: appliedCourses){
            AppliedCourseResponse appliedCourseResponse = AppliedCourseResponse.of(course.getId(), course.getTitle(), course.getInstructor());
            appliedCourseResponses.add(appliedCourseResponse);
        }

        return new AppliedCoursesResponse(appliedCourseResponses);
    }
}
