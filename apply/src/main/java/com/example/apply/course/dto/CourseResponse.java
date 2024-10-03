package com.example.apply.course.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
public class CourseResponse {

    String title;
    long leftCount;
    LocalDate startDate;

    @Builder
    public CourseResponse(String title, long leftCount, LocalDate startDate){
        this.title = title;
        this.leftCount = leftCount;
        this.startDate = startDate;
    }
    public static CourseResponse of(String title, long leftCount, LocalDate startDate){
        return CourseResponse.builder()
                             .title(title)
                             .leftCount(leftCount)
                             .startDate(startDate)
                             .build();
    }
}
