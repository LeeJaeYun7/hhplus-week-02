package com.example.apply.course.dto;

import lombok.Builder;
import lombok.Getter;
@Getter
public class AppliedCourseResponse {

    long id;
    String title;
    String instructor;

    @Builder
    public AppliedCourseResponse(long id, String title, String instructor){
        this.id = id;
        this.title = title;
        this.instructor = instructor;
    }
    public static AppliedCourseResponse of(long id, String title, String instructor){
        return AppliedCourseResponse.builder()
                                    .id(id)
                                    .title(title)
                                    .instructor(instructor)
                                    .build();
    }
}
