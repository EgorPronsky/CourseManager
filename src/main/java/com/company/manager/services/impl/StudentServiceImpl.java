package com.company.manager.services.impl;

import com.company.manager.dao.custom.StudentDAO;
import com.company.manager.dao.custom.impl.HibernateStudentDAO;
import com.company.manager.domain.user.Student;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.StudentService;

import java.security.InvalidParameterException;

@Slf4j
public class StudentServiceImpl implements StudentService {

    private static final StudentServiceImpl service;
    private final StudentDAO dao;

    private StudentServiceImpl(){
        log.info("Initializing {}", StudentServiceImpl.class.getName());
        dao = new HibernateStudentDAO();
        log.info("{} was initialized", StudentServiceImpl.class.getName());
    }

    static {
        service = new StudentServiceImpl();
    }

    public static StudentServiceImpl getService() {
        return service;
    }

    @Override
    public void saveStudent(Student student) {
        dao.save(student);
    }

    @Override
    public void updateStudent(Student student) {
        dao.update(student);
    }

    @Override
    public Student getStudentById(long studentId) {
        return dao.findById(studentId)
                .orElseThrow(() -> new InvalidParameterException("Student wasn't received by given id"));
    }




}
