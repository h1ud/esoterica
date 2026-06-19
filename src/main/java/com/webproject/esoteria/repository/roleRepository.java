package com.webproject.esoteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webproject.esoteria.domain.entity.Role;

public interface roleRepository extends JpaRepository<Role, Long> {

}
