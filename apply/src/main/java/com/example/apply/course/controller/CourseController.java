package com.example.apply.course.controller;

import com.example.apply.course.dto.AppliedCoursesResponse;
import com.example.apply.course.dto.AvailableCoursesResponse;
import com.example.apply.course.dto.CourseApplyRequest;
import com.example.apply.course.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
public class CourseController {
    private final CourseService courseService;
    public CourseController(CourseService courseService){
        this.courseService = courseService;
    }

    @PostMapping("/course/apply")
    public void applyCourse(@RequestBody CourseApplyRequest courseApplyRequest) throws Exception {
        long memberId = courseApplyRequest.getMemberId();
        long courseId = courseApplyRequest.getCourseId();

        courseService.applyCourse(memberId, courseId);
    }

    @GetMapping("/course/search")
    public ResponseEntity<AvailableCoursesResponse> searchCoursesByDate(@RequestParam("date") String dateStr) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateStr, formatter);

        AvailableCoursesResponse availableCoursesResponse = courseService.searchCoursesByDate(date);
        return ResponseEntity.ok().body(availableCoursesResponse);
    }

    @GetMapping("/course/applied")
    public ResponseEntity<AppliedCoursesResponse> retrieveAppliedCoursesByMemberId(@RequestParam("memberId") long memberId) throws Exception {
        AppliedCoursesResponse appliedCoursesResponse = courseService.getAppliedCoursesByMemberId(memberId);
        return ResponseEntity.ok().body(appliedCoursesResponse);
    }
}
