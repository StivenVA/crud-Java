package org.project.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    private String direction;
    private String phone;
    private Date birthdate;
    @Lob
    private byte[] image;
    @Lob
    private byte[] video;

    @Column(name = "created_at")
    private Timestamp createdAt;
}
