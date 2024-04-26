package org.gamenet.application.VerseLearner;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.gamenet.application.VerseLearner.data.Book;
import org.gamenet.application.VerseLearner.data.ReferenceException;
import org.gamenet.application.VerseLearner.data.Verse;
import org.gamenet.application.VerseLearner.data.parser.BibleGatewayHtmlParser;
import org.gamenet.application.VerseLearner.data.parser.FlatFileVerseReferenceVerseTextParser;
import org.gamenet.application.VerseLearner.data.parser.VerseParser;
import org.gamenet.application.VerseLearner.data.parser.VerseParser.WrongFormatException;
import org.gamenet.application.VerseLearner.gui.VerseLearnerFrame;

public class VerseLearner {
    final private Vector<Verse> verseVector = new Vector<Verse>();
    
	private VerseLearnerFrame window = null;

	private int answerRequests;
	private int checkRequests;
	private int hintRequests;
	
	private int verseIndex;
	private List<Verse> verseList = null;

    private Vector<String> bookNameVector = null;

    private boolean showReferencesOnEachVerse = false;
    private boolean ignoreWhitespace = false;
	private boolean ignoreCase = false;
	private boolean ignorePunctuation = false;
    private boolean includeReferenceInAnswer = false;
    private boolean allowAnyNumberOfSpacesAfterASentence = true;
	
	public boolean isAllowAnyNumberOfSpacesAfterASentence() {
		return allowAnyNumberOfSpacesAfterASentence;
	}

	public void setAllowAnyNumberOfSpacesAfterASentence(
			boolean allowAnyNumberOfSpacesAfterASentence) {
		this.allowAnyNumberOfSpacesAfterASentence = allowAnyNumberOfSpacesAfterASentence;
	}

	public boolean isShowReferencesOnEachVerse() {
		return showReferencesOnEachVerse;
	}

	public void setShowReferencesOnEachVerse(boolean showReferencesOnEachVerse) {
		this.showReferencesOnEachVerse = showReferencesOnEachVerse;
	}

	public boolean isIgnoreWhitespace() {
		return ignoreWhitespace;
	}

	public void setIgnoreWhitespace(boolean ignoreWhitespace) {
		this.ignoreWhitespace = ignoreWhitespace;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public boolean isIgnorePunctuation() {
		return ignorePunctuation;
	}

	public void setIgnorePunctuation(boolean ignorePunctuation) {
		this.ignorePunctuation = ignorePunctuation;
	}

	public boolean isIncludeReferenceInAnswer() {
		return includeReferenceInAnswer;
	}

	public void setIncludeReferenceInAnswer(boolean includeReferenceInAnswer) {
		this.includeReferenceInAnswer = includeReferenceInAnswer;
	}

    public Vector<String> getBookNameVector() {
		return bookNameVector;
	}

	public void setBookNameVector(Vector<String> bookNameVector) {
		this.bookNameVector = bookNameVector;
	}

	public VerseLearner(List<Verse> verseList) {
        this.verseList = verseList;
    }

	public static void main(String[] args)
    {
		List<Verse> verseList = getVerseListFromFile(null);
        
        if (null == verseList)
        {
            System.exit(1);
        }
        
		VerseLearner verseLearner = new VerseLearner(verseList);
		verseLearner.testChapterMatch();
	}

	public void testChapterMatch() {
		verseIndex = 0;

        randomizeVersesIntoVector(verseVector, verseList);

		this.window = new VerseLearnerFrame(this);
		window.setVisible(true);
		initializeVerse();
	}

    private static List<Verse> getVerseListFromFile(Component parent)
    {
        List<Verse> verseList = null;
		try {
            // BibleGatewayHtmlParser
            String bibleGatewayDirectoryName = "/home/mkienenb/workspaces/personal/VerseLearner/biblegateway";
            
            File file = null;
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new File(bibleGatewayDirectoryName));
            int returnVal = fc.showOpenDialog(parent);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile().getAbsoluteFile();
            } else {
                return null;
            }
            
            VerseParser verseParser = new BibleGatewayHtmlParser();
            
			FileInputStream inputStream = new FileInputStream(file);
            WrongFormatException bibleGatewayHtmlParserWrongFormatException = null;
			try
            {
                verseList = verseParser.parseInputStream(inputStream);
            }
            catch (WrongFormatException e)
            {
                bibleGatewayHtmlParserWrongFormatException = e;
                
                inputStream.close();
                inputStream = null;
                inputStream = new FileInputStream(file);
                
                // FlatFileVerseReferenceVerseTextParser
                verseParser = new FlatFileVerseReferenceVerseTextParser();
                try
                {
                    verseList = verseParser.parseInputStream(inputStream);
                }
                catch (WrongFormatException e1)
                {
                    JOptionPane.showMessageDialog(parent, "Unsupported file type");
                    bibleGatewayHtmlParserWrongFormatException.printStackTrace();
                    e1.printStackTrace();
                    return null;
                }
            }
            finally
            {
                if (null != inputStream)
                {
                    inputStream.close();
                    inputStream = null;
                }
            }
		} catch (IOException e) {
            JOptionPane.showMessageDialog(parent, e.getMessage());
			e.printStackTrace();
			return null;
        } catch (ReferenceException e) {
            JOptionPane.showMessageDialog(parent, e.getMessage());
            e.printStackTrace();
            return null;
        }
		
		if (null == verseList)
		{
            JOptionPane.showMessageDialog(parent, "No verses found in file.");
            return null;
		}
        return verseList;
    }

    private void randomizeVersesIntoVector(Vector<Verse> verseTextVector, List<Verse> verseList2)
    {
        verseTextVector.clear();
        Iterator<Verse> verseIterator = verseList2.iterator();
        while (verseIterator.hasNext())
        {
            Verse verse = verseIterator.next();
            verseTextVector.add(verse);
        }
        
        Collections.shuffle(verseTextVector);
    }

	public void checkVerse(String proposedAnswer) {
		String normalizedVerseText = normalizeString(getVerseText());
		String normalizedProposedAnswer = normalizeString(proposedAnswer);
        
        // leading and trailing space aren't important
		if (isCorrectAnswer(normalizedProposedAnswer, normalizedVerseText))
		{
			handleUserAnsweredCorrectly();
		}
	}

	private boolean isCorrectAnswer(String normalizedProposedAnswer, String normalizedVerseText) {
		return normalizedProposedAnswer.trim().equals(normalizedVerseText.trim());
	}

	private void handleUserAnsweredCorrectly() {
		boolean goToNextVerse = false;
		if (isReadyForNextVerse_maybe())
		{
			goToNextVerse = true;
		}
		else
		{
			String tryAgainOption = "Try verse again.";
			String nextOption = "Close enough. Go to the next verse.";
			
			// JOptionPane.showMessageDialog(mainWindow, tryAgainOption);
			
			Object[] possibleValues = { tryAgainOption, nextOption };
			Object selectedValue = JOptionPane.showInputDialog(null,
				"Try verse again, or go to the next verse?", "Correct.",
				JOptionPane.QUESTION_MESSAGE, null,
				possibleValues, possibleValues[0]);
			
			if (selectedValue.equals(nextOption))
			{
				goToNextVerse = true;
			}
		}

		if (goToNextVerse)
		{
	        String currentVerseText = getVerseReference() + "\n" + getMemoryVerse().getText();

            window.addCurrentVerseToCompletedVerseList(currentVerseText);

            verseIndex = verseIndex + 1;
			if (verseIndex >= verseList.size())
			{
				verseIndex = 0;
			}
		}

		initializeVerse();
	}

	private boolean isReadyForNextVerse_maybe() {
		return (0 == answerRequests) && (0 == checkRequests) && (0 == hintRequests);
	}

	public Verse getMemoryVerse()
	{
		return (Verse)verseList.get(verseIndex);
	}

	private String getVerseText() {
        return getMemoryVerse().getText();
	}

	public String getVerseReference() {
        return getMemoryVerse().getReference();
	}

	private String correctAnswerPrefix(String rawProposedAnswer, String rawRealAnswer) {
		String realAnswer = normalizeString(rawRealAnswer);
		String proposedAnswer = normalizeString(rawProposedAnswer);

		int indexOfError = indexOfError(proposedAnswer, realAnswer);
		if (-1 == indexOfError)
		{
			return realAnswer;
		}
		return realAnswer.substring(0, indexOfError);
	}

	protected String normalizeString(String string)
	{
		String normalizedString = string;
		if (ignorePunctuation)
		{
			normalizedString = normalizedString.replace('.', '@');
			normalizedString = normalizedString.replace(',', '@');
			normalizedString = normalizedString.replace(';', '@');
			normalizedString = normalizedString.replace('?', '@');
			normalizedString = normalizedString.replace('-', '@');
			normalizedString = normalizedString.replace('!', '@');
			normalizedString = normalizedString.replace('(', '@');
			normalizedString = normalizedString.replace(')', '@');
			normalizedString = normalizedString.replace('"', '@');
			normalizedString = normalizedString.replace('\'', '@');
			normalizedString = normalizedString.replaceAll("@", "");
			normalizedString = removeConsecutiveWhitespace(normalizedString);
		}
		
        if (allowAnyNumberOfSpacesAfterASentence)
        {
            normalizedString = normalizedString.replaceAll("[.]\\s+", ". ");
            normalizedString = normalizedString.replaceAll("[?]\\s+", "? ");
            normalizedString = normalizedString.replaceAll("[!]\\s+", "! ");
        }
        
		if (ignoreWhitespace)
		{
			normalizedString = removeConsecutiveWhitespace(normalizedString);
		}
		
		if (ignoreCase)
		{
			normalizedString = normalizedString.toLowerCase();
		}
		
		return normalizedString;
	}

	private String removeConsecutiveWhitespace(String normalizedString) {
		String newString = normalizedString;
		do
		{
			normalizedString = newString;
			newString = newString.replace('\n', ' ').trim();
			newString = newString.replace('\r', ' ').trim();
			newString = newString.replace('\t', ' ').trim();
			newString = newString.replaceAll("  ", " ").trim();
		}
		while (newString.length() != normalizedString.length());
		
		return newString;
	}

	protected String hint(String rawProposedAnswer, String rawRealAnswer) {
		String realAnswer = normalizeString(rawRealAnswer);
		String proposedAnswer = normalizeString(rawProposedAnswer);

		int indexOfError = indexOfError(proposedAnswer, realAnswer);
		if (-1 == indexOfError)
		{
			return null;
		}
		
		String checkCharacterTypeInProposedAnswerAtError = checkCharacterTypeInStringAtError(proposedAnswer, indexOfError);
		String checkCharacterTypeInRealAnswerAtError = checkCharacterTypeInStringAtError(realAnswer, indexOfError);
		if ((null != checkCharacterTypeInProposedAnswerAtError) && (null != checkCharacterTypeInRealAnswerAtError))
		{
			return checkCharacterTypeInRealAnswerAtError;
		}

		if (indexOfError < proposedAnswer.length())
		{
			if ((Character.isLetter(proposedAnswer.charAt(indexOfError))))
			{
				if ( (indexOfError < realAnswer.length())
				  && (Character.isLetter(realAnswer.charAt(indexOfError))) )
				{
					if (Character.toLowerCase(proposedAnswer.charAt(indexOfError))
					 == Character.toLowerCase(realAnswer.charAt(indexOfError)))
					{
						return "<case sensitivity>";
					}
				}
			}
		}
		else
		{
			String hint = findWordInStringAfterIndex(realAnswer.substring(indexOfError).trim(), 0);
			if (null != hint)
			{
				return hint;
			}
		}

		String proposedAnswerWord = findWordInStringNearIndex(proposedAnswer, indexOfError);
		String realAnswerWord = findWordInStringNearIndex(realAnswer, indexOfError);
		if (null != realAnswerWord)
		{
			return realAnswerWord;
		}
		else if (null != proposedAnswerWord)
		{
			String hint = findWordInStringBeforeIndex(realAnswer, indexOfError);
			if (null != hint)
			{
				return hint;
			}
		}

		if (null != checkCharacterTypeInRealAnswerAtError)
		{
			return checkCharacterTypeInRealAnswerAtError;
		}

		return realAnswer.substring(indexOfError, indexOfError);
	}

	private String findWordInStringAfterIndex(String string, int index) {
		while ( (string.length() > index) 
			&& (! Character.isLetter(string.charAt(index)))
			&& (! Character.isDigit(string.charAt(index))) ) 
		{
			++index;
		}

		return findWordInStringNearIndex(string, index);
	}

	private String findWordInStringBeforeIndex(String string, int index) {
		while ( (0 < index) 
			&& (! Character.isLetter(string.charAt(index)))
			&& (! Character.isDigit(string.charAt(index))) ) 
		{
			--index;
		}

		return findWordInStringNearIndex(string, index);
	}

	private String findWordInStringNearIndex(String string, int index) {
		if (null == string)
		{
			return null;
		}

		if (string.length() <= index)
		{
			return null;
		}
		
		if ( (! Character.isLetter(string.charAt(index)))
		  && (! Character.isDigit(string.charAt(index))) ) 
		{
			return null;
		}

		int startIndex = index;
		int endIndex = index;

		if (Character.isLetter(string.charAt(index)))
		{
			while ((startIndex > 0) && (Character.isLetter(string.charAt(startIndex-1))))
			{
				startIndex--;
			}

			while ((endIndex < string.length()) && (Character.isLetter(string.charAt(endIndex))))
			{
				endIndex++;
			}
		}

		if (Character.isDigit(string.charAt(index)))
		{
			while ((startIndex > 0) && (Character.isDigit(string.charAt(startIndex-1))))
			{
				startIndex--;
			}

			while ((endIndex < string.length()) && (Character.isDigit(string.charAt(endIndex))))
			{
				endIndex++;
			}
		}

		return string.substring(startIndex, endIndex);
	}

	private String checkCharacterTypeInStringAtError(String string, int indexOfError) {
		if (indexOfError >= string.length())
		{
			return null;
		}

		char errorChar = string.charAt(indexOfError);

		switch(errorChar)
		{
			case ' ':
				return "<white space>";
			case '.':
			case ',':
			case ';':
			case '?':
			case '-':
			case ':':
			case '!':
			case '(':
			case ')':
			case '"':
			case '\'':
				return "<punctuation: " + errorChar + ">";
			default:
				return null;
		}
	}

	private int indexOfError(String proposedAnswer, String realAnswer) {
		int i;
		for (i = 0; i < realAnswer.length(); i++) {
			char c = realAnswer.charAt(i);
			if (proposedAnswer.length() <= i)
			{
				break;
			}
			if (proposedAnswer.charAt(i) != c)
			{
				break;
			}
		}
		if (i == realAnswer.length())
		{
			return -1;
		}
		return i;
	}

	public void openFile() {
        List<Verse> newVerseList = getVerseListFromFile(window);
        if (null != newVerseList)
        {
            verseList = newVerseList;
            verseIndex = 0;
            initializeVerse();
        }
	}

	public void quit() {
    	window.setVisible(false);
        System.exit(0);
	}

	public void initializeVerse()
	{
		answerRequests = 0;
		checkRequests = 0;
		hintRequests = 0;

		window.initializeVerseAnswer(getVerseText());
	}
	
	public void setWriteVersesFromReferencesMode() {
        initializeVerse();

        window.showWriteVersesFromReferencesMode();
	}

	public void setPutVersesInCorrectOrderMode() {
        randomizeVersesIntoVector(verseVector, verseList);

        window.showPutVersesInCorrectOrderMode();
	}


	public void setPutBooksOfTheBibleInCorrectOrderMode() {
	    bookNameVector = new Vector<String>(Book.randomBookList());

	    window.showPutBooksOfTheBibleInCorrectOrderMode();
	}

	public Vector<Verse> getVerseVector() {
		return verseVector;
	}

	public void verseDragged() {
        for (int verseIndex = 0; verseIndex < verseList.size(); verseIndex++) {
            if (false == verseVector.get(verseIndex).equals(verseList.get(verseIndex)))
            {
                return;
            }
        }
        
        Collections.shuffle(verseVector);
        showReferencesOnEachVerse = false;
        
        // Do something to show that we're done
    	window.displayMessage("Correct!");
    	// TODO: shouldn't be calling repaint from this class
        window.repaint();
	}

	public void changeVerses(String verseReference) {
        List<Verse> newVerseList;
        try {
            newVerseList = Verse.findVerseByReference(verseList, verseReference);
        } catch (ReferenceException e1) {
        	window.displayMessage("No verse found: " + e1.getMessage());
            return;
        }
        
        if (null != newVerseList)
        {
            verseList = newVerseList;
            verseIndex = 0;
            initializeVerse();
        }
        else
        {
        	window.displayMessage("No verse found.");
        }
	}

	public String requestHint(String proposedAnswer) {
		hintRequests++;
		String realAnswer = getVerseText();
		String hint = hint(proposedAnswer, realAnswer);
		
		return hint;
	}

	public void reportUnhelpfulHint(String proposedAnswer) {

		String realAnswer = getVerseText();
		String hint = hint(proposedAnswer, realAnswer);
		String answerSoFar = correctAnswerPrefix(proposedAnswer, realAnswer);

		// IMPLEMENT: report hint
		System.out.println("[" + realAnswer + "] - realAnswer");
		System.out.println("[" + proposedAnswer + "] - proposedAnswer");
		System.out.println("[" + answerSoFar + "] - answerSoFar");
		System.out.println("[" + hint + "] - hint");
	}

	public void previousVerse() {
		verseIndex = verseIndex - 1;
		if (verseIndex < 0)
		{
			verseIndex = verseList.size() - 1;
		}
		
		initializeVerse();
	}

	public void nextVerse() {
		verseIndex = verseIndex + 1;
		if (verseIndex >= verseList.size())
		{
			verseIndex = 0;
		}
		initializeVerse();
	}

	public void didShowAnswer() {
		answerRequests++;
	}

	public String checkAnswerSoFar(String proposedAnswer) {
		checkRequests++;
		return partOfTheVerseThatIsCorrectSoFar(proposedAnswer);
	}

	private String partOfTheVerseThatIsCorrectSoFar(String proposedAnswer) {
		String realAnswer = getVerseText();
		String answerSoFar = correctAnswerPrefix(proposedAnswer, realAnswer);
		return answerSoFar;
	}

	public void bookDragged() {
        String[] correctBookOrderArray = Book.getBookArray();
        for (int bookIndex = 0; bookIndex < correctBookOrderArray.length; bookIndex++) {
            if (false == bookNameVector.get(bookIndex).equals(correctBookOrderArray[bookIndex]))
            {
                return;
            }
        }
        
        Collections.shuffle(bookNameVector);
        
        // Do something to show that we're done
    	window.displayMessage("Correct!");
    	// TODO: shouldn't be calling repaint from this class
        window.repaint();
	}

}
