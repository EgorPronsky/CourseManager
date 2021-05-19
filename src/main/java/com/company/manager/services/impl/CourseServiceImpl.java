package com.company.manager.services.impl;

import com.company.manager.dao.custom.CourseDAO;
import com.company.manager.dao.custom.impl.HibernateCourseDAO;
import com.company.manager.domain.course.Course;
import com.company.manager.services.CourseService;
import lombok.extern.slf4j.Slf4j;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
public class CourseServiceImpl implements CourseService {

    private static final CourseServiceImpl service;
    private final CourseDAO dao;

    private CourseServiceImpl() {
        log.info("Initializing {}", CourseServiceImpl.class.getName());
        dao = new HibernateCourseDAO();
        log.info("{} was initialized", CourseServiceImpl.class.getName());
    }

    static {
        service = new CourseServiceImpl();
    }

    public static CourseServiceImpl getService() {
        return service;
    }

    @Override
    public void saveOrUpdateCourse(Course course) {
        dao.saveOrUpdate(course);
    }

    @Override
    public void deleteCourse(Course course) {
        dao.delete(course);
    }

    @Override
    public Optional<Course> findCourseById(long courseId) {
        return dao.findById(courseId);
    }

    @Override
    public Course getCourseById(long courseId) {
        Optional<Course> courseOpt = findCourseById(courseId);
        if (!courseOpt.isPresent()) throw new InvalidParameterException("Course wasn't found");
        return courseOpt.get();
    }

    @Override
    public List<Course> getCoursesById(Collection<Long> idCollection) {
        return dao.getCoursesById(idCollection);
    }


    @Override
    public List<Course> getStudentFutureCourses(long studentId) {
        LocalDate today = LocalDate.now();
        return dao.getStudentCoursesStartAfterDate(studentId, today);
    }

    @Override
    public List<Course> getStudentCurrentCourses(long studentId) {
        LocalDate today = LocalDate.now();
        return dao.getStudentCoursesActiveOnDate(studentId, today);
    }

    @Override
    public List<Course> getStudentFinishedUngradedCourses(long studentId) {
        LocalDate today = LocalDate.now();
        return dao.getStudentUngradedCoursesEndedBeforeDate(studentId, today);
    }

    @Override
    public List<Course> getStudentAvailableToJoinCourses(long studentId) {
        LocalDate today = LocalDate.now();
        return dao.getStudentNotSubscribedCoursesStartAfterDate(studentId, today);
    }


    @Override
    public List<Course> getTeacherFutureCourses(long teacherId) {
        LocalDate today = LocalDate.now();
        return dao.getTeacherCoursesStartAfterDate(teacherId, today);
    }

    @Override
    public List<Course> getTeacherCurrentCourses(long teacherId) {
        LocalDate today = LocalDate.now();
        return dao.getTeacherCoursesActiveOnDate(teacherId, today);
    }

    @Override
    public List<Course> getTeacherFinishedNotGradedCourses(long teacherId) {
        LocalDate today = LocalDate.now();
        return dao.getTeacherNotGradedCoursesEndedBeforeDate(teacherId, today);
    }






}
