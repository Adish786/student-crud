package com.example.service;

import com.example.model.Student;
import com.example.repository.StudentRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student create(Student student) {
        student.setId(null);
        return repository.save(student);
    }

    public List<Student> findAll() {
        return repository.findAll();
    }

    public Optional<Student> findById(Long id) {
        return repository.findById(id);
    }

    public List<Student> findByName(String name) {
        return repository.findByNameContains(name);
    }

    public Optional<Student> update(Long id, Student newData) {
        Optional<Student> existing = repository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }

        Student student = existing.get();
        student.setName(newData.getName());
        student.setEmail(newData.getEmail());
        student.setAge(newData.getAge());
        return Optional.of(repository.update(student));
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
