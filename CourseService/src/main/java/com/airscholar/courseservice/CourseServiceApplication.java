package com.airscholar.courseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@SpringBootApplication
public class CourseServiceApplication {

	@GetMapping("/courses")
	public List<Course> getCourses(){
		return Stream.of(new Course("1", "Java", 5, 5000),
				new Course("2", "Python", 3, 5000),
				new Course("3", "C++", 2, 5000))
				.collect(Collectors.toList());
	}

	public static void main(String[] args) {
		SpringApplication.run(CourseServiceApplication.class, args);
	}

}
