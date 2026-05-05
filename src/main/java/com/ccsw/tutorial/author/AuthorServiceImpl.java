package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDto;
import com.ccsw.tutorial.author.model.AuthorSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    @Override
    public Author get(Long id) {
        return authorRepository.findById(id).orElse(null);
    }

    @Override
    public List<Author> findAll() {
        return (List<Author>) this.authorRepository.findAll();
    }

    @Override
    public Page<Author> findPage(AuthorSearchDto authorSearchDto) {
        return this.authorRepository.findAll(authorSearchDto.getPageable().getPageable());
    }

    @Override
    public void save(Long id, AuthorDto authorDto) {

        Author author;
        if (id == null) {
            author = new Author();
        } else {
            author = this.get(id);
        }
        BeanUtils.copyProperties(authorDto, author, "id");
        this.authorRepository.save(author);
    }

    @Override
    public void delete(Long id) throws Exception {
        if (this.get(id) == null) {
            throw new Exception();
        }

        this.authorRepository.deleteById(id);
    }
}
