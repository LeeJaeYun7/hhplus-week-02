package com.example.apply.course.dto;

import com.example.apply.course.domain.Course;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AvailableCoursesResponse {

    List<CourseResponse> availableCourses;

    @Builder
    public AvailableCoursesResponse(List<CourseResponse> availableCourses){
        this.availableCourses = availableCourses;
    }
}
