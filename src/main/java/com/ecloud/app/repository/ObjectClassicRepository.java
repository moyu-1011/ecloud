package com.ecloud.app.repository;

import com.ecloud.app.pojo.ObjectClassic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectClassicRepository extends JpaRepository<ObjectClassic, Integer> {

    @Query("SELECT classic FROM ObjectClassic WHERE name=?1")
    String findClassicByName(String name);

}
