package com.sasho.demo.repository;

import com.sasho.demo.domain.Address;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {

    @EntityGraph(attributePaths = "author")
    List<Address> findAllBy();
}
