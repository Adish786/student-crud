package com.example;

import com.example.model.Student;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class StudentControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void crudEndpointsAndNameSearchWork() {
        Student created = client.toBlocking().retrieve(
                HttpRequest.POST("/students", student("Asha", "asha@example.com", 21)),
                Student.class
        );

        assertNotNull(created.getId());
        assertEquals("Asha", created.getName());

        List<Student> all = client.toBlocking().retrieve(
                HttpRequest.GET("/students"),
                Argument.listOf(Student.class)
        );
        assertTrue(all.stream().anyMatch(student -> student.getId().equals(created.getId())));

        List<Student> matches = client.toBlocking().retrieve(
                HttpRequest.GET("/students/search?name=sh"),
                Argument.listOf(Student.class)
        );
        assertEquals(1, matches.size());
        assertEquals(created.getId(), matches.get(0).getId());

        Student updated = client.toBlocking().retrieve(
                HttpRequest.PUT("/students/" + created.getId(), student("Asha K", "asha.k@example.com", 22)),
                Student.class
        );
        assertEquals("Asha K", updated.getName());
        assertEquals(22, updated.getAge());

        HttpResponse<?> deleteResponse = client.toBlocking().exchange(
                HttpRequest.DELETE("/students/" + created.getId())
        );
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatus());
    }

    private Student student(String name, String email, int age) {
        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setAge(age);
        return student;
    }
}
