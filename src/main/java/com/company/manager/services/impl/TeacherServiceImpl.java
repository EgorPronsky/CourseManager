package com.company.manager.services.impl;

import com.company.manager.dao.custom.TeacherDAO;
import com.company.manager.dao.custom.impl.HibernateTeacherDAO;
import com.company.manager.domain.user.Teacher;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.TeacherService;

import java.security.InvalidParameterException;

@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private static final TeacherServiceImpl service;
    private final TeacherDAO dao;

    private TeacherServiceImpl(){
        log.info("Initializing {}", TeacherServiceImpl.class.getName());
        dao = new HibernateTeacherDAO();
        log.info("{} was initialized", TeacherServiceImpl.class.getName());
    }

    static {
        service = new TeacherServiceImpl();
    }

    public static TeacherServiceImpl getService() {
        return service;
    }

    @Override
    public void saveTeacher(Teacher teacher) {
        dao.save(teacher);
    }

    @Override
    public Teacher getTeacherById(long teacherId) {
        return dao.findById(teacherId)
                .orElseThrow(() -> new InvalidParameterException("Teacher wasn't received by given id"));
    }




}
