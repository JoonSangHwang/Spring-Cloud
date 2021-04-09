package com.example.catalogsservice.repository;

import com.example.catalogsservice.entity.Catalog;
import org.springframework.data.repository.CrudRepository;

public interface CatalogRepository extends CrudRepository<Catalog, Long> {
    Catalog findByProductId(String productId);
}
