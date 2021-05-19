package com.company.manager.dao.custom;

import com.company.manager.dao.generic.GenericDAO;
import com.company.manager.domain.archive.StudentCourseResult;

import java.util.Collection;
import java.util.List;

public interface StudentCourseResultDAO extends GenericDAO<StudentCourseResult> {

    void saveAll(Collection<StudentCourseResult> scrCollection);
    List<StudentCourseResult> getStudentsResultsByCourseId(long courseId);
    List<StudentCourseResult> getCoursesResultsByStudentId(long studentId);

}
