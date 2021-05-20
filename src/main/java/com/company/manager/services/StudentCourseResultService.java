package com.company.manager.services;

import com.company.manager.domain.archive.StudentCourseResult;

import java.util.Collection;
import java.util.List;

public interface StudentCourseResultService {

    void deleteStudentCourseResult(StudentCourseResult scr);
    void updateStudentCourseResults(Collection<StudentCourseResult> scrCollection);
    List<StudentCourseResult> getStudentsResultsByCourseId(long courseId);
    List<StudentCourseResult> getCompletedCoursesWithResultsByStudentId(long studentId);

}
