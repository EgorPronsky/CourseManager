package com.company.manager.services.impl;

import com.company.manager.dao.custom.StudentCourseResultDAO;
import com.company.manager.dao.custom.impl.HibernateStudentCourseResultDAO;
import com.company.manager.domain.archive.CourseResult;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.StudentCourseResultService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class StudentCourseResultServiceImpl implements StudentCourseResultService {

    private static final StudentCourseResultServiceImpl service;
    private final StudentCourseResultDAO dao;

    private StudentCourseResultServiceImpl() {
        log.info("Initializing {}", StudentCourseResultService.class.getName());
        dao = new HibernateStudentCourseResultDAO();
        log.info("{} was initialized", StudentCourseResultService.class.getName());
    }

    static {
        service = new StudentCourseResultServiceImpl();
    }

    public static StudentCourseResultServiceImpl getService() {
        return service;
    }

    @Override
    public void saveStudentCourseResults(Collection<StudentCourseResult> scrCollection) {
        dao.saveAll(scrCollection);
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