package service;

import dao.custom.StudentCourseResultDAO;
import dao.custom.impl.HibernateCourseDAO;
import dao.custom.impl.HibernateStudentCourseResultDAO;
import dao.generic.impl.HibernateGenericDAO;
import domain.archive.CourseResult;
import domain.archive.StudentCourseResult;
import domain.course.Course;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import service.map_services.StudentCourseResultMapService;
import util.HibernateUtil;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class StudentCourseResultService {

    private final StudentCourseResultDAO dao;

    private StudentCourseResultService() {
        dao = new HibernateStudentCourseResultDAO();
    }

    // Lazy init
    private static class ServiceHolder {
        static final StudentCourseResultService service = new StudentCourseResultService();
    }

    public static StudentCourseResultService getService() {
        return ServiceHolder.service;
    }

    public void saveStudentCourseResults(Course course, Map<User, CourseResult> studentsResults) {
        List<StudentCourseResult> scrList =
                StudentCourseResultMapService.convertStudentsResultsToDTO(course, studentsResults);
        dao.saveAll(scrList);
    }

    public List<StudentCourseResult> getStudentsResultsByCourseId(long courseId) {
        return dao.getStudentsResultsByCourseId(courseId);
    }

    public List<StudentCourseResult> getCoursesResultsByStudentId(long studentId) {
        return dao.getCoursesResultsByStudentId(studentId);
    }

}
