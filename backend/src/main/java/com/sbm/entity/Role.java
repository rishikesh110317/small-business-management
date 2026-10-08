package com.sbm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    public static final String OWNER = "ROLE_OWNER";
    public static final String EMPLOYEE = "ROLE_EMPLOYEE";
    public static final String PLATFORM_ADMIN = "ROLE_PLATFORM_ADMIN";
}
