package fr.baretto.library;

public class Book {
    private String title;
    private String author;

    public Book(String title, String author){
        this.title = title;
        this.author= author;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public String toString(){
        return "titre: " + title + " - auteur: " + author;
    }
}