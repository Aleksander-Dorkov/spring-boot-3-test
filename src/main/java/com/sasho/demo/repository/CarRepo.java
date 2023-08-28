package com.sasho.demo.repository;

import com.sasho.demo.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CarRepo extends JpaRepository<Car, Long> {
    List<Car> findAllByIdIn(Set<Long> ids);
}
