package com.company.manager.services;

import com.company.manager.domain.archive.CourseResult;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.Student;

import java.util.List;
import java.util.Map;

public interface StudentCourseResultService {

    void saveStudentCourseResults(Course course, Map<Student, CourseResult> studentsResults);
    List<StudentCourseResult> getStudentsResultsByCourseId(long courseId);
    List<StudentCourseResult> getCoursesResultsByStudentId(long studentId);

}
