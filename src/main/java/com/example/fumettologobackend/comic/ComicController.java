package com.example.fumettologobackend.comic;

import com.example.fumettologobackend.support.exceptions.*;
import jakarta.validation.Valid;
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

    @GetMapping("/comics")
    public ResponseEntity<?> getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                    @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        List<Comic> comics = this.comicService.findAll(pageNumber, pageSize, sortBy);
        if (comics.isEmpty()) {
            return new ResponseEntity<>("No comics found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(comics, HttpStatus.OK);
    }

    @GetMapping("/comics/title")
    public ResponseEntity<?> getByTitle(@RequestParam(value = "title") String title,
                                        @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        List<Comic> comics = this.comicService.findByTitle(title, pageNumber, pageSize, sortBy);
        if (comics.isEmpty()) {
            return new ResponseEntity<>("No comics found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(comics, HttpStatus.OK);
    }
    @PutMapping("/admin/comics/{id}/updatePrice")
    public ResponseEntity<?> updatePrice(@PathVariable int id, @RequestParam float newPrice) {
        try {
            comicService.updatePrice(id, newPrice);
            return new ResponseEntity<>("Price updated successfully", HttpStatus.OK);
        } catch (ComicNotFoundException e) {
            return new ResponseEntity<>("Comic not found", HttpStatus.NOT_FOUND);
        }
    }

}
