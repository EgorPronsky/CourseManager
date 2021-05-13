package services;

import domain.archive.CourseResult;
import domain.archive.StudentCourseResult;
import domain.course.Course;
import domain.user.User;

import java.util.List;
import java.util.Map;

public interface StudentCourseResultService {

    void saveStudentCourseResults(Course course, Map<User, CourseResult> studentsResults);
    List<StudentCourseResult> getStudentsResultsByCourseId(long courseId);
    List<StudentCourseResult> getCoursesResultsByStudentId(long studentId);

}
