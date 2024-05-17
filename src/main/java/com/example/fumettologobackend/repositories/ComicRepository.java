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

    // restituisce un fumetto in base all'id fornito
    Comic findById(int id);

    // cerca e restituisce una pagina strutturata di Comic il cui titolo contiene la stringa fornita
    // (ignora maiuscole e minuscole).
    // @Param specifica il parametro della query
    // Pageable specifica dettagli di paginazione
    @Query("SELECT c FROM Comic c WHERE (LOWER(c.title) LIKE LOWER(CONCAT('%',:title,'%')))")
    Page<Comic> findByTitle(@Param("title") String title, Pageable pageable);

    // cerca e restituisce una pagina di Comic che soddisfa i criteri di ricerca basati su titolo, autore, etc...
    // se un parametro è null viene ignorato dalla query
    // l'OR consente la ricerca parziale e la gestione dei parametri opzionali
    @Query("SELECT c " +
            "FROM Comic c " +
            "WHERE (LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%')) OR :title IS NULL) AND " +
            "(LOWER(c.author) LIKE LOWER(CONCAT('%', :author, '%')) OR :author IS NULL) AND " +
            "(LOWER(c.publisher) LIKE LOWER(CONCAT('%', :publisher, '%')) OR :publisher IS NULL) AND " +
            "(LOWER(c.category) LIKE LOWER(CONCAT('%', :category, '%')) OR :category IS NULL)")
    Page<Comic> filter(@Param("title") String title,
                       @Param("author") String author,
                       @Param("publisher") String publisher,
                       @Param("category") String category,
                       Pageable pageable
    );

}