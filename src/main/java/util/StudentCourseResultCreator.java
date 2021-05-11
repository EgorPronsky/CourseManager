package util;

import domain.archive.CourseResult;
import domain.archive.StudentCourseResult;
import domain.course.Course;
import domain.user.User;

public class StudentCourseResultCreator {

    public static StudentCourseResult createStudentCourseResult(User student, Course course, CourseResult result) {
        return StudentCourseResult.builder()
                .student(student)
                .course(course)
                .result(result)
                .build();
    }

}
