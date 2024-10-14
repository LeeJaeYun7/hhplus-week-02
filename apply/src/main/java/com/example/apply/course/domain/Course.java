package com.example.apply.course.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String instructor;

    @Column(name = "total_count")
    private long totalCount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Builder
    public Course(long id, String title, String instructor, long totalCount, LocalDate startDate){
        this.id = id;
        this.title = title;
        this.instructor = instructor;
        this.totalCount = totalCount;
        this.startDate = startDate;
    }

    public static Course of(long id, String title, long totalCount){
        return Course.builder()
                     .id(id)
                     .title(title)
                     .totalCount(totalCount)
                     .build();
    }

    public synchronized void increaseTotalCount() {
        if (this.totalCount >= 30) {
            throw new IllegalStateException("Course exceeded limitation");
        }
        this.totalCount++;
    }
}
