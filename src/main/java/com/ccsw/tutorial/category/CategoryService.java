package com.ccsw.tutorial.category;

import com.ccsw.tutorial.category.model.Category;
import com.ccsw.tutorial.category.model.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    void save(Long id, CategoryDto categoryDTO);

    void delete(Long id) throws Exception;
}
