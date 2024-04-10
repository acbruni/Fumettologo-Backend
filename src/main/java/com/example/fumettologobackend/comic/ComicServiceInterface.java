package com.example.fumettologobackend.comic;

import com.example.fumettologobackend.support.exceptions.*;

import java.util.List;

public interface ComicServiceInterface {

    Comic findById(int id) throws ComicNotFoundException;
    List<Comic> findAll(int pageNumber, int pageSize, String sortBy);
    List<Comic> findByTitle(String title, int pageNumber, int pageSize, String sortBy);
    void updatePrice(int id, float price) throws ComicNotFoundException;

}
