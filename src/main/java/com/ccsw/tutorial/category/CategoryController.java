package com.ccsw.tutorial.category;

import com.ccsw.tutorial.category.model.Category;
import com.ccsw.tutorial.category.model.CategoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Category", description = "API of Category")
@RequestMapping(value = "/category")
@RestController
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper mapper;

    @Operation(summary = "Find", description = "Method that returns a list of categories")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryService.findAll();
        return categories.stream().map(e -> mapper.map(e, CategoryDTO.class)).collect(Collectors.toList());
    }

    @Operation(summary = "Save and Update", description = "Method that saves or updates a category")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void Save(@PathVariable(name = "id", required = false) Long id, @RequestBody CategoryDTO categoryDTO) {
        categoryService.save(id, categoryDTO);
    }

    @Operation(summary = "Delete", description = "Method that deletes a category")
    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {
        categoryService.delete(id);
    }

}
