package online.stemlink.skillmentor.services;

import online.stemlink.skillmentor.entities.Student;


import java.util.List;

public interface StudentService {

    Student createNewStudent(Student student);
    List<Student> getAllStudents();
    Student getStudentById(Long id);
    Student updateStudentById(Long id, Student updatedStudent);
    void deleteStudent(Long id);
}
