package com.company.manager.util;

import com.company.manager.domain.archive.CourseResult;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StudentCourseResultConverter {

    public static Set<StudentCourseResult> convertCourseStudentsResultsToSCR(Course course, Map<User, CourseResult> studentResults) {
        Set<StudentCourseResult> scrSet = new HashSet<>();
        studentResults.forEach((key, value) -> scrSet.add(
                convertToStudentCourseResult(course, key, value)));
        return scrSet;
    }

    public static Set<StudentCourseResult> convertStudentCoursesToSCR(User student, Collection<Course> courses) {
        Set<StudentCourseResult> scrSet = new HashSet<>();
        courses.forEach(course -> scrSet.add(
                convertToStudentCourseResult(course, student, null)));
        return scrSet;
    }

    public static StudentCourseResult convertToStudentCourseResult(Course course, User student, CourseResult result) {
        return StudentCourseResult.builder()
                .student(student).course(course).result(result).build();
    }

}
