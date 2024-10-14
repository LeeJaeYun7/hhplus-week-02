package com.example.apply.course.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AppliedCoursesResponse {

    List<AppliedCourseResponse> appliedCourses;

    @Builder
    public AppliedCoursesResponse(List<AppliedCourseResponse> appliedCourses){
        this.appliedCourses = appliedCourses;
    }
}
