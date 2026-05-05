package com.ccsw.tutorial.game.model;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.category.model.Category;
import jakarta.persistence.*;

@Entity
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "age", nullable = false)
    private String age;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "authorId", nullable = false)
    private Author author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
