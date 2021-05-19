package com.company.manager.dao.custom.impl;

import com.company.manager.dao.custom.StudentDAO;
import com.company.manager.dao.generic.impl.HibernateGenericDAO;
import com.company.manager.domain.user.Student;

public class HibernateStudentDAO extends HibernateGenericDAO<Student> implements StudentDAO {

    public HibernateStudentDAO() {
        super(Student.class);
    }

}
