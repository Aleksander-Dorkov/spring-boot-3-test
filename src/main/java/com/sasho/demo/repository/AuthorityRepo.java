package com.sasho.demo.repository;

import com.sasho.demo.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AuthorityRepo extends JpaRepository<Authority, Long> {
    Set<Authority> findAllByAuthorityIn(Set<String> authorityNames);

    Authority findAllByAuthority(String authorityName);
}
