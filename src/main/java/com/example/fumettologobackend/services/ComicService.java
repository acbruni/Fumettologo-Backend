package com.example.fumettologobackend.services;

import com.example.fumettologobackend.entities.Comic;
import com.example.fumettologobackend.repositories.ComicRepository;
import com.example.fumettologobackend.support.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComicService {
    private final ComicRepository comicRepository;

    @Autowired // inietta automaticamente un'istanza di ComicRepository quando viene creata un'istanza di ComicService
    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    // viene eseguito in un contesto transazionale di sola lettura
    @Transactional(readOnly = true)
    public Comic findById(int id) throws ComicNotFoundException {
        Comic comic = this.comicRepository.findById(id); // recupera il fumetto tramite l'id

        // se è nullo lancia l'eccezione altrimenti lo restituisce
        if(comic == null) {
            throw new ComicNotFoundException();
        }
        return comic;
    }

    @Transactional(readOnly = true)
    public List<Comic> findAll(int pageNumber, int pageSize, String sortBy) { // pagina da recuperare, elementi per pag (dimensione), ordinamento
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy)); // creazione della pagina con i criteri forniti
        Page<Comic> pagedResult = comicRepository.findAll(pageable); // ottiene una pagina di risultati dal repository

        // se la pagina contiene elementi allora restituisce la lista dei fumetti
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        // altrimenti restituisce una lista vuota
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Comic> findByTitle(String title, int pageNumber, int pageSize, String sortBy) { // titolo, etc...
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy)); // creazione pagina
        Page<Comic> pagedResult = comicRepository.findByTitle(title, pageable); // ottiene una pagina di risultati dal repository filtrato in base alle specifiche

        // se la pagina contiene elementi restituisce la lista dei fumetti ottenuta
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        // altrmenti restituisce una lista vuota
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Comic> filter(String title, String author, String publisher, String category,
                                     int pageNumber, int pageSize, String sortBy) {

        // utilizzo di 'switch' per determinare l'ordinamento basato sul parametro sortBy e creazione della pagina
        PageRequest pageable = switch (sortBy) {
            case "Titolo A-Z" -> PageRequest.of(pageNumber, pageSize, Sort.by("title").ascending());
            case "Titolo Z-A" -> PageRequest.of(pageNumber, pageSize, Sort.by("title").descending());
            case "Prezzo crescente" -> PageRequest.of(pageNumber, pageSize, Sort.by("price").ascending());
            case "Prezzo decrescente" -> PageRequest.of(pageNumber, pageSize, Sort.by("price").descending());
            default -> PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        };
        Page<Comic> pagedResult = comicRepository.filter(title,author,publisher,category,pageable); // ottiene una pagina di risultati dal repository

        // se la pagina contiene elementi viene restituita la lista di fumetti ottenuta
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        // altrimenti restituisce una lista vuota
        return new ArrayList<>();
    }

    @Transactional
    public void updatePrice(int id, float price) throws ComicNotFoundException {
        Comic comic = findById(id); // recupera il fumetto tramite l'id
        comic.setPrice(price); // aggiorna il prezzo
        this.comicRepository.save(comic); // salva le modifiche apportate al fumetto nel db
    }

}