package com.finance.project.modules.ledger.domain;

import com.finance.project.modules.ledger.domain.Category;
import com.finance.project.modules.ledger.domain.CategoryID;
import com.finance.project.modules.ledger.domain.ICategoryRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ICategoryRepository {

    Category save(Category category);

    Optional<Category> findById(String id, String denomination);

    boolean existsById(CategoryID categoryID);

    long count();

    List<Category> findAll();

    void delete(CategoryID categoryID);

    List<Category> findAllById(String id, String denomination);
}
