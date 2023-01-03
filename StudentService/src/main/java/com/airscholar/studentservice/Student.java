package com.airscholar.studentservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Student {
    private Integer id;
    private String name;
    private String rollNo;
    private String section;

}
