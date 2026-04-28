package com.ccsw.tutorial.category;

import com.ccsw.tutorial.category.model.Category;
import com.ccsw.tutorial.category.model.CategoryDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return (List<Category>) this.categoryRepository.findAll();
    }

    @Override
    public void save(Long id, CategoryDto categoryDTO) {
        Category category;

        if (id == null) {
            category = new Category();
        } else {
            category = this.categoryRepository.findById(id).orElse(null);
        }
        category.setName(categoryDTO.getName());

        this.categoryRepository.save(category);
    }

    @Override
    public void delete(Long id) throws Exception {
        if (this.categoryRepository.findById(id).orElse(null) == null) {
            throw new Exception("Not exist");
        }
        this.categoryRepository.deleteById(id);
    }
}
