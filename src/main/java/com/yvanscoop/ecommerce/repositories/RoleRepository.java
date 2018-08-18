package com.yvanscoop.ecommerce.repositories;

import com.yvanscoop.ecommerce.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role,Long> {
    @Query("select r from Role r where r.role like :nom  order by r.id")
    public Page<Role> chercher(@Param("nom") String nom, Pageable pageable);
}
