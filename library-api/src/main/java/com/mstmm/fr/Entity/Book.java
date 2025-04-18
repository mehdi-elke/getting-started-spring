package com.mstmm.fr.Entity;

import javax.persistence.*;

@Entity
@Table(name = "books")
public class Book implements BookInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    public Book(Long number, String title, String author) {
        this.id = number;
        this.title = title;
        this.author = author;
    }

    public Book() {

    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    @Override
    public String getTitle() { return title; }

    @Override
    public void setTitle(String title) { this.title = title; }

    @Override
    public String getAuthor() { return author; }

    @Override
    public void setAuthor(String author) { this.author = author; }

    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "', author='" + author + "'}";
    }
}
