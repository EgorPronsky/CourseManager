package com.company.manager.services;

import com.company.manager.domain.user.Teacher;

public interface TeacherService {

    void saveTeacher(Teacher teacher);
    Teacher getTeacherById(long teacherId);

}
