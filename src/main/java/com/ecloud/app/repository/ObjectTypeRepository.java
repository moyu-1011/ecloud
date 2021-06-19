package com.ecloud.app.repository;

import com.ecloud.app.pojo.ObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ObjectTypeRepository extends JpaRepository<ObjectType, Integer> {

    @Query(value = "SELECT type FROM ObjectType ")
    List<String> findTypes();

    @Modifying
    @Query(value = "UPDATE ObjectType SET type = ?1 WHERE id = ?2")
    void updateTypeById(String type, int id);

    @Modifying
    ObjectType saveAndFlush(ObjectType objectType);

    @Modifying
    void deleteById(int id);
}
