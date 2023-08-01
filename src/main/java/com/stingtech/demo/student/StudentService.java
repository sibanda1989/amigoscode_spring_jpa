package com.stingtech.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional =  studentRepository
                .findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()){
            throw new IllegalStateException("email taken");
        }
        studentRepository.save(student);
        System.out.println(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        //first check if student exists
        if (!exists){
            throw new IllegalStateException(
                    "student with id " + studentId + " does not exist"
            );
        }

        studentRepository.deleteById(studentId);
    }

//    public void studentEmailExists(Student student){
//        Optional<Student> studentOptional =  studentRepository
//                .findStudentByEmail(student.getEmail());
//        if (studentOptional.isPresent()){
//            throw new IllegalStateException("email taken");
//        }
//    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        //find if student exists -> edit name, email.. if not throw exception
        Student student =  studentRepository
                .findById(studentId).orElseThrow(() ->
            new IllegalStateException( "student with id " + studentId + " does not exist"));

        if (name != null && !name.isEmpty() && !Objects.equals(student.getName(), name)){
            student.setName(name);
        }

        if (email != null && !email.isEmpty() && !Objects.equals(student.getEmail(), email)){
            Optional<Student> studentOptional =  studentRepository
                    .findStudentByEmail(student.getEmail());
            if (studentOptional.isPresent()){
                throw new IllegalStateException("email taken");
            }
            student.setEmail(email);
        }
    }
}
