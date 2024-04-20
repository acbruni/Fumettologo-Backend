package com.example.fumettologobackend.repositories;

import com.example.fumettologobackend.entities.Comic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComicRepository extends JpaRepository<Comic,Integer> {
    Comic findById(int id);

    @Query("SELECT b FROM Comic b WHERE (LOWER(b.title) LIKE LOWER(CONCAT('%',:title,'%')))")
    Page<Comic> findByTitle(@Param("title") String title, Pageable pageable);


}
