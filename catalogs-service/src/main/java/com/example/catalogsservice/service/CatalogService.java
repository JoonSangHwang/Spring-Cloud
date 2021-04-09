package com.example.catalogsservice.service;

import com.example.catalogsservice.entity.Catalog;

public interface CatalogService {
    Iterable<Catalog> getAllCatalogs();
}
