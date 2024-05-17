package com.example.fumettologobackend.controllers;

import com.example.fumettologobackend.entities.Comic;
import com.example.fumettologobackend.services.ComicService;
import com.example.fumettologobackend.support.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:9090", allowedHeaders = "*")
public class ComicController {
    private final ComicService comicService;

    @Autowired
    public ComicController(ComicService comicService) {
        this.comicService = comicService;
    }

    @GetMapping("/comic")
    public ResponseEntity<?> getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                    @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        // recupera tutti i fumetti con supporto per paginazione e ordinamento (num pag da recuperare, dimensione, ordinamento)
        List<Comic> comics = this.comicService.findAll(pageNumber, pageSize, sortBy);
        if (comics.isEmpty()) {
            // HTTP 404 se non vengono trovati fumetti
            return new ResponseEntity<>("No comics found", HttpStatus.NOT_FOUND);
        }
        // HTTP 200 se ha successo
        return new ResponseEntity<>(comics, HttpStatus.OK);
    }

    @GetMapping("/comic/title")
    public ResponseEntity<?> getByTitle(@RequestParam(value = "title") String title,
                                        @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        // recupera i fumetti in base al titolo (titolo del fumetto da cercare, pagina da recuperare, dimensione, ordinamento)
        List<Comic> comics = this.comicService.findByTitle(title, pageNumber, pageSize, sortBy);
        if (comics.isEmpty()) {
            return new ResponseEntity<>("No comics found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(comics, HttpStatus.OK);
    }

    @GetMapping("/comic/filter")
    public ResponseEntity<?> filter(@RequestParam(value = "title", defaultValue = "") String title,
                                            @RequestParam(value = "author", defaultValue = "") String author,
                                            @RequestParam(value = "publisher", defaultValue = "") String publisher,
                                            @RequestParam(value = "category", defaultValue = "") String category,
                                            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        // filtra i fumetti in base al titolo, autore etc...
        List<Comic> books = comicService.filter(title, author, publisher, category, pageNumber, pageSize, sortBy);
        if (books.isEmpty()) {
            return new ResponseEntity<>("No books found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PutMapping("/admin/comic/{id}/updatePrice")
    public ResponseEntity<?> updatePrice(@PathVariable int id, @RequestParam float newPrice) {
        try {
            // aggiorna il prezzo del fumetto tramite id
            comicService.updatePrice(id, newPrice);
            return new ResponseEntity<>("Price updated successfully", HttpStatus.OK);
        } catch (ComicNotFoundException e) {
            return new ResponseEntity<>("Comic not found", HttpStatus.NOT_FOUND);
        }
    }

}
