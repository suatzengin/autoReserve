package com.autoreserve.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class CarType {
    @Id
    private String id;

    private String name;

    private int totalStock;
}
