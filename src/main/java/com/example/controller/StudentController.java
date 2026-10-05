package com.example.controller;

import com.example.model.Student;
import com.example.service.StudentService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.validation.Validated;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

/**
 * REST API for Student.
 *   POST   /students        -> Create
 *   GET    /students        -> Read all
 *   GET    /students/{id}   -> Read one
 *   PUT    /students/{id}   -> Update
 *   DELETE /students/{id}   -> Delete
 */
@Validated
@Controller("/students")
public class StudentController {

    private final StudentService service;

    // Micronaut injects the service for us (Dependency Injection)
    public StudentController(StudentService service) {
        this.service = service;
    }

    // CREATE
    @Post
    public HttpResponse<Student> create(@Valid @Body Student student) {
        Student saved = service.create(student);
        return HttpResponse.created(saved, URI.create("/students/" + saved.getId()));
    }

    // READ ALL
    @Get
    public List<Student> getAll() {
        return service.findAll();
    }

    // SEARCH BY NAME
    @Get("/search")
    public List<Student> searchByName(@QueryValue String name) {
        return service.findByName(name);
    }

    // READ ONE
    @Get("/{id}")
    public HttpResponse<Student> getById(Long id) {
        return service.findById(id).map(HttpResponse::ok).orElse(HttpResponse.notFound());
    }

    // UPDATE
    @Put("/{id}")
    public HttpResponse<Student> update(Long id, @Valid @Body Student newData) {
        return service.update(id, newData).map(HttpResponse::ok).orElse(HttpResponse.notFound());
    }

    // DELETE
    @Delete("/{id}")
    public HttpResponse<Void> delete(Long id) {
        if (!service.delete(id)) {
            return HttpResponse.notFound();
        }
        return HttpResponse.noContent();
    }
}
