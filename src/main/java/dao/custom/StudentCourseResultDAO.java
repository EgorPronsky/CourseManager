package dao.custom;

import dao.generic.GenericDAO;
import domain.archive.StudentCourseResult;

import java.util.List;

public interface StudentCourseResultDAO extends GenericDAO<StudentCourseResult> {

    void saveAll(List<StudentCourseResult> scrList);
    List<StudentCourseResult> getStudentsResultsByCourseId(long courseId);
    List<StudentCourseResult> getCoursesResultsByStudentId(long studentId);

}
