package com.lightsoundsanctuary.user_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    // Getters and Setters
    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
