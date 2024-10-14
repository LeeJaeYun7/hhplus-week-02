package com.example.apply.course.service;

import com.example.apply.course.domain.Course;
import com.example.apply.course.domain.CourseRegistration;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CourseServiceConcurrencyTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    CourseRepository courseRepository;

    @Mock
    CourseRegistrationRepository courseRegistrationRepository;
    @InjectMocks
    CourseService courseService;

    @Nested
    @DisplayName("특강을 신청할때")
    class 특강을_신청할때 {

        @Test
        void 특강_수강_요청이_40명이_동시에_올때_30명만_성공한다() throws InterruptedException {

            int requestCount = 40;
            ExecutorService executorService = Executors.newFixedThreadPool(10);

            Course course = Course.of(1L, "Java 101", 0);
            given(courseRepository.findByIdWithLock(course.getId())).willReturn(Optional.of(course));

            List<Member> members = new ArrayList<>();

            for(long i = 0; i < requestCount; i++){
                Member member = new Member(i+1, "abc"+ Long.toString(i+1));
                members.add(member);
                given(courseRegistrationRepository.findCourseRegistrationByMemberIdAndCourseId(i+1, 1L)).willReturn(Optional.empty());
                given(memberRepository.findById(i+1)).willReturn(Optional.of(member));
            }

            CountDownLatch latch = new CountDownLatch(requestCount);
            AtomicInteger successCount = new AtomicInteger(0);

            for(int i = 0; i < requestCount; i++) {
                Member member = members.get(i);
                executorService.submit(() -> {
                    try{
                        courseService.applyCourse(member.getId(), course.getId());
                        successCount.incrementAndGet();
                    }catch (Exception e) {
                        throw new RuntimeException(e);
                    }finally{
                        latch.countDown();
                    }
                });
            }

            latch.await();
            executorService.shutdown();

            assertEquals(30, successCount.get());
            assertEquals(30, course.getTotalCount());
        }
    }

    @Nested
    @DisplayName("멤버 한 명이 같은 특강을 여러 번 신청할때")
    class 멤버_한_명이_같은_특강을_여러_번_신청할때 {

        @Test
        void 같은_특강을_5번_신청하면_1번만_성공한다() throws InterruptedException {

            int requestCount = 5;
            ExecutorService executorService = Executors.newFixedThreadPool(10);

            Member member = Member.of(1L, "Tom Cruise");
            Course course = Course.of(1L, "Java 101", 0);
            CourseRegistration courseRegistration = CourseRegistration.of(member, course);
            given(memberRepository.findById(1L)).willReturn(Optional.of(member));
            given(courseRepository.findByIdWithLock(1L)).willReturn(Optional.of(course));

            AtomicInteger counter = new AtomicInteger(0);
            given(courseRegistrationRepository.findCourseRegistrationByMemberIdAndCourseId(1L, 1L)).willAnswer(invocation -> {
                if (counter.getAndIncrement() == 0) {
                    return Optional.empty(); // 첫 번째 요청에 대해 Optional.empty() 반환
                } else {
                    return Optional.of(courseRegistration); // 이후 요청에 대해 CourseRegistration 반환
                }
            });

            CountDownLatch latch = new CountDownLatch(requestCount);
            AtomicInteger successCount = new AtomicInteger(0);

            for(int i = 0; i < requestCount; i++) {
                executorService.submit(() -> {
                    try{
                        courseService.applyCourse(1L, 1L);
                        successCount.incrementAndGet();
                    }catch (Exception e) {
                        throw new RuntimeException(e);
                    }finally{
                        latch.countDown();
                    }
                });
            }

            latch.await();
            executorService.shutdown();

            assertEquals(1, successCount.get());
            assertEquals(1, course.getTotalCount());
        }
    }
}
