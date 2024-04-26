package org.gamenet.application.VerseLearner.data;

public class Chapter
{
	private Book book;
	private int chapterNumber;
	
	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public int getChapterNumber() {
		return chapterNumber;
	}

	public void setChapterNumber(int chapterNumber) {
		this.chapterNumber = chapterNumber;
	}

	public Chapter(Book book, int chapterNumber)
	{
		this.book = book;
		this.chapterNumber = chapterNumber;
	}

	public static Chapter findChapter(Book book, int chapterNumber) {
		Chapter chapter = new Chapter(book, chapterNumber);
		book.addChapter(chapter);
		
		return chapter;
	}
}
