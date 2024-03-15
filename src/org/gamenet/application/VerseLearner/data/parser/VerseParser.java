package org.gamenet.application.VerseLearner.data.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.gamenet.application.VerseLearner.data.ReferenceException;
import org.gamenet.application.VerseLearner.data.Verse;

public interface VerseParser
{
    public class WrongFormatException extends Exception
    {
		private static final long serialVersionUID = 1L;

		public WrongFormatException(String message)
        {
            super(message);
        }
    }

    public List<Verse> parseInputStream(InputStream inputStream)
        throws IOException, ReferenceException, WrongFormatException;
}