package com.company.manager.services.impl;

import com.company.manager.dao.custom.StudentCourseResultDAO;
import com.company.manager.dao.custom.impl.HibernateStudentCourseResultDAO;
import com.company.manager.domain.archive.CourseResult;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.User;
import com.company.manager.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.StudentCourseResultService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class StudentCourseResultServiceImpl implements StudentCourseResultService {

    private static final StudentCourseResultServiceImpl service;
    private final StudentCourseResultDAO dao;

    private StudentCourseResultServiceImpl() {
        log.info("Initializing {}", StudentCourseResultService.class.getName());
        dao = new HibernateStudentCourseResultDAO(HibernateUtil.getSessionFactory());
        log.info("{} was initialized", StudentCourseResultService.class.getName());
    }

    static {
        service = new StudentCourseResultServiceImpl();
    }

    public static StudentCourseResultServiceImpl getService() {
        return service;
    }

    @Override
    public void deleteStudentCourseResult(StudentCourseResult scr) {
        dao.delete(scr);
    }

    @Override
    public void updateStudentCourseResults(Collection<StudentCourseResult> scrCollection) {
        dao.updateAll(scrCollection);
    }

    @Override
    public List<StudentCourseResult> getStudentsResultsByCourseId(long courseId) {
        return dao.getStudentsResultsByCourseId(courseId);
    }

    @Override
    public List<StudentCourseResult> getCompletedCoursesWithResultsByStudentId(long studentId) {
        LocalDate today = LocalDate.now();
        return dao.getEndedBeforeDateCoursesWithResultsByStudentId(studentId, today);
    }

}
