package com.company.manager.services;

import com.company.manager.domain.archive.CourseResult;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface StudentCourseResultService {

    void deleteStudentCourseResult(StudentCourseResult scr);
    void updateStudentCourseResults(Collection<StudentCourseResult> scrCollection);
    List<StudentCourseResult> getStudentsResultsByCourseId(long courseId);
    List<StudentCourseResult> getCompletedCoursesWithResultsByStudentId(long studentId);

}
