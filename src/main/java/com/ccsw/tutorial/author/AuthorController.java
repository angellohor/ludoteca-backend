package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDto;
import com.ccsw.tutorial.author.model.AuthorSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Author", description = "API of Author")
@RequestMapping(value = "/author")
@RestController
@CrossOrigin(origins = "*")
public class AuthorController {

    @Autowired
    AuthorService authorService;

    @Autowired
    ModelMapper modelMapper;

    @Operation(summary = "Find", description = "Method that return a list of Authors")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<AuthorDto> findAll() {

        List<Author> authors = this.authorService.findAll();

        return authors.stream().map(e -> modelMapper.map(e, AuthorDto.class)).collect(Collectors.toList());
    }

    @Operation(summary = "Find Page", description = "Method that returns a page of Authors")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<AuthorDto> findPage(@RequestBody AuthorSearchDto authorSearchDto) {
        Page<Author> page = authorService.findPage(authorSearchDto);
        return new PageImpl<>(page.getContent().stream().map(e -> modelMapper.map(e, AuthorDto.class)).collect(Collectors.toList()), page.getPageable(), page.getTotalElements());
    }

    @Operation(summary = "Save or Update", description = "Method that saves or updates a new Author")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody AuthorDto authorDto) {
        authorService.save(id, authorDto);
    }

    @Operation(summary = "Delete", description = "Method that deletes an Author")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(name = "id", required = true) Long id) throws Exception {
        authorService.delete(id);
    }
}
