package com.autoreserve.repository;

import com.autoreserve.model.CarType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarTypeRepository extends JpaRepository<CarType, String> {
}