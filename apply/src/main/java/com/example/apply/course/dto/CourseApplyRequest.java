package com.example.apply.course.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CourseApplyRequest {

    private long memberId;
    private long courseId;
}
