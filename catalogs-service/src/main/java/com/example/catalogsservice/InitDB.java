package com.example.catalogsservice;

import com.example.catalogsservice.entity.Catalog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            // 회원 등록
            em.persist(new Catalog("CATALOG-001", "Berlin", 100, 1500));
            em.persist(new Catalog("CATALOG-002", "Tokyo", 100, 1500));
            em.persist(new Catalog("CATALOG-003", "Stockholm", 100, 1500));
        }
    }
}
