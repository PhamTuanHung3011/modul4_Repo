package codegym.service;

import codegym.model.Student;
import codegym.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentService implements IStudentService{
    @Autowired
    StudentRepo studentRepo;


    @Override
    public List<Student> findAll() {
        return (List<Student>) studentRepo.findAll();
    }

    @Override
    public void save(Student student) {
        studentRepo.save(student);
    }

    @Override
    public void delete(long id) {
        studentRepo.deleteById(id);
    }

    @Override
    public Student findById(long id) {
        return studentRepo.findById(id).get();
    }

}
