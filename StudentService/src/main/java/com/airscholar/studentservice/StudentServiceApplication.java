package com.airscholar.studentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@SpringBootApplication
public class StudentServiceApplication {

	@GetMapping("/students")
	public List<Student> getStudents(){
		return Stream.of(new Student(1, "John", "1", "B"),
				new Student(2, "Jane", "2", "A"),
				new Student(3, "Jack", "3", "C"))
				.collect(Collectors.toList());
	}

	public static void main(String[] args) {
		SpringApplication.run(StudentServiceApplication.class, args);
	}

}
