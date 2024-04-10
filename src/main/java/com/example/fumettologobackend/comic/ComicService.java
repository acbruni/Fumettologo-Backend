package com.example.fumettologobackend.comic;

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
public class ComicService implements ComicServiceInterface {
    private final ComicRepository comicRepository;

    @Autowired
    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Comic findById(int id) throws ComicNotFoundException {
        Comic comic = this.comicRepository.findById(id);
        if(comic == null) {
            throw new ComicNotFoundException();
        }
        return comic;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comic> findAll(int pageNumber, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Comic> pagedResult = comicRepository.findAll(pageable);
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comic> findByTitle(String title, int pageNumber, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Comic> pagedResult = comicRepository.findByTitle(title, pageable);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public void updatePrice(int id, float price) throws ComicNotFoundException {
        Comic comic = findById(id);
        comic.setPrice(price);
        this.comicRepository.save(comic);
    }

}