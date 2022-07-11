package com.tates.api.demo.repositories;

import com.tates.api.demo.models.Admin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AdminRepository extends CrudRepository<Long, Admin> {
Boolean existsByEmail(String email);
}