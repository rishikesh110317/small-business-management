package com.sbm.dto;

import lombok.Data;

@Data
public class BusinessDto {
    private Long id;
    private String name;
    private String ownerName;
    private String email;
    private String phone;
    private String address;
    private Boolean isActive;
}
