package com.yvanscoop.ecommerce.repositories;


import com.yvanscoop.ecommerce.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query("select u from User u where u.username like :nom  order by u.id")
    public Page<User> chercher(@Param("nom") String nom, Pageable pageable);

    public User getByUsername(String name);

}
