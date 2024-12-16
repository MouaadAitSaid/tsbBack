package com.utils;


import java.util.List;
import java.util.Map;

public class SearchRequest {

    private String searchTerm; // Terme de recherche
    private int page = 0;      // Page actuelle, valeur par défaut : 0
    private int size = 10;     // Nombre d'éléments par page, valeur par défaut : 10
    private List<String> searchableFields; // Champs recherchables
    private Map<String, Object> filters;

    // Getters et Setters

    public Map<String, Object> filters() {
        return filters;
    }

    public SearchRequest setFilters(Map<String, Object> filters) {
        this.filters = filters;
        return this;
    }

    public String searchTerm() {
        return searchTerm;
    }

    public SearchRequest setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
        return this;
    }

    public int page() {
        return page;
    }

    public SearchRequest setPage(int page) {
        this.page = page;
        return this;
    }

    public int size() {
        return size;
    }

    public SearchRequest setSize(int size) {
        this.size = size;
        return this;
    }

    public List<String> searchableFields() {
        return searchableFields;
    }

    public SearchRequest setSearchableFields(List<String> searchableFields) {
        this.searchableFields = searchableFields;
        return this;
    }
}
