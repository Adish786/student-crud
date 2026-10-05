package com.example.repository;

import com.example.model.Student;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

/**
 * No code needed! Micronaut Data generates the implementation at compile time.
 * CrudRepository already gives us: save, findById, findAll, deleteById, existsById ...
 */
@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
    List<Student> findByNameContains(String name);
}
