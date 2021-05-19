package com.company.manager.services;

import com.company.manager.domain.user.Student;

public interface StudentService {

   void saveStudent(Student student);
   void updateStudent(Student student);
   Student getStudentById(long studentId);

}
