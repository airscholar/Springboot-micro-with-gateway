package com.airscholar.courseservice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Course {
    private String id;
    private String name;
    private Integer durationInMonths;
    private Integer price;
}