package com.company.manager.dao.custom;

import com.company.manager.dao.generic.GenericDAO;
import com.company.manager.domain.archive.StudentCourseResult;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface StudentCourseResultDAO extends GenericDAO<StudentCourseResult> {

    void updateAll(Collection<StudentCourseResult> scrCollection);
    List<StudentCourseResult> getStudentsResultsByCourseId(long courseId);
    List<StudentCourseResult> getEndedBeforeDateCoursesWithResultsByStudentId(long studentId, LocalDate date);

}
