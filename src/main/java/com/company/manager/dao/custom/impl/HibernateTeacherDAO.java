package com.company.manager.dao.custom.impl;

import com.company.manager.dao.custom.TeacherDAO;
import com.company.manager.dao.generic.impl.HibernateGenericDAO;
import com.company.manager.domain.user.Teacher;

public class HibernateTeacherDAO extends HibernateGenericDAO<Teacher> implements TeacherDAO {

    public HibernateTeacherDAO() {
        super(Teacher.class);
    }

}
