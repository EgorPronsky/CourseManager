package services.impl;

import dao.custom.StudentCourseResultDAO;
import dao.custom.impl.HibernateCourseDAO;
import dao.custom.impl.HibernateStudentCourseResultDAO;
import domain.archive.CourseResult;
import domain.archive.StudentCourseResult;
import domain.course.Course;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import services.StudentCourseResultService;
import util.converters.StudentCourseResultMapService;

import java.util.List;
import java.util.Map;

@Slf4j
public class StudentCourseResultServiceImpl implements StudentCourseResultService {

    private final StudentCourseResultDAO dao;

    private StudentCourseResultServiceImpl() {
        log.info("Initializing {}", StudentCourseResultService.class.getName());
        dao = new HibernateStudentCourseResultDAO();
        log.info("{} was initialized", StudentCourseResultService.class.getName());
    }

    // Init on-demand
    private static class ServiceHolder {
        static final StudentCourseResultServiceImpl service = new StudentCourseResultServiceImpl();
    }

    public static StudentCourseResultServiceImpl getService() {
        return ServiceHolder.service;
    }

    @Override
    public void saveStudentCourseResults(Course course, Map<User, CourseResult> studentsResults) {
        List<StudentCourseResult> scrList =
                StudentCourseResultMapService.convertStudentsResultsToDTO(course, studentsResults);
        dao.saveAll(scrList);
    }

    @Override
    public List<StudentCourseResult> getStudentsResultsByCourseId(long courseId) {
        return dao.getStudentsResultsByCourseId(courseId);
    }

    @Override
    public List<StudentCourseResult> getCoursesResultsByStudentId(long studentId) {
        return dao.getCoursesResultsByStudentId(studentId);
    }

}
