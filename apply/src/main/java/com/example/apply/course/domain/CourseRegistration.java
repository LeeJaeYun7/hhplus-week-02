package com.example.apply.course.domain;

import com.example.apply.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class CourseRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    Course course;

    @Builder
    public CourseRegistration(Member member, Course course){
        this.member = member;
        this.course = course;
    }

    public static CourseRegistration of(Member member, Course course){
        return CourseRegistration.builder()
                                 .member(member)
                                 .course(course)
                                 .build();
    }
}
