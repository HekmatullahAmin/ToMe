package com.hekmatullahamin.plan.model;

public class Book {
    private int bookId, bookNumberOfPagesRead = 0, bookTotalBookPages;
    private String bookName;

    public Book() {
    }

    public Book(int bookId, int bookNumberOfPagesRead, int bookTotalBookPages, String bookName) {
        this.bookId = bookId;
        this.bookNumberOfPagesRead = bookNumberOfPagesRead;
        this.bookTotalBookPages = bookTotalBookPages;
        this.bookName = bookName;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getBookNumberOfPagesRead() {
        return bookNumberOfPagesRead;
    }

    public void setBookNumberOfPagesRead(int bookNumberOfPagesRead) {
        this.bookNumberOfPagesRead = bookNumberOfPagesRead;
    }

    public int getBookTotalBookPages() {
        return bookTotalBookPages;
    }

    public void setBookTotalBookPages(int bookTotalBookPages) {
        this.bookTotalBookPages = bookTotalBookPages;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
