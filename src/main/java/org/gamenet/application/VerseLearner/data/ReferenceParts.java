package org.gamenet.application.VerseLearner.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReferenceParts
{
	public String getBookName() {
		return bookName;
	}
	public Integer getChapterNumber() {
		return chapterNumber;
	}
	public Integer getEndingVerseNumber() {
		return endingVerseNumber;
	}
	public Integer getStartingVerseNumber() {
		return startingVerseNumber;
	}
	public ReferenceParts(String reference) throws ReferenceException
	{
		reference = reference.trim();
		bookName = Book.findBookNamePartInReference(reference);
		if (null == bookName)
		{
			throw new ReferenceException("No valid book found in " + reference);
		}
		if (reference.length() > bookName.length() + 1)
		{
			if (' ' != reference.charAt(bookName.length()))
			{
				throw new ReferenceException("Illegal book: position=" + bookName.length() + " in reference "+ reference);
			}
			String rest = reference.substring(bookName.length()+ 1).trim();
			
			Pattern pattern = Pattern.compile("^(\\d+)(\\:(\\d+)(\\-(\\d+))?)?$");
		    Matcher m = pattern.matcher(rest);
		    if (m.matches())
	    	{
		    	String chapterNumberString = m.group(1);
		    	if (null == chapterNumberString)
		    	{
		    		chapterNumber = null;
		    	}
		    	else
		    	{
		    		chapterNumber = new Integer(chapterNumberString);
		    	}

		    	String startingVerseNumberString = m.group(3);
		    	if (null == startingVerseNumberString)
		    	{
		    		startingVerseNumber = null;
		    	}
		    	else
		    	{
			    	startingVerseNumber = new Integer(startingVerseNumberString);
		    	}
		    	
		    	String endingVerseNumberString = m.group(5);
		    	if (null == endingVerseNumberString)
		    	{
			    	endingVerseNumber = null;
		    	}
		    	else
		    	{
			    	endingVerseNumber = new Integer(endingVerseNumberString);
		    	}
	    	}
		}
	}

	String bookName;
	Integer chapterNumber;
	Integer startingVerseNumber;
	Integer endingVerseNumber;
}