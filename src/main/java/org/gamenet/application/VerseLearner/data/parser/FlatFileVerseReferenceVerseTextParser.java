package org.gamenet.application.VerseLearner.data.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.gamenet.application.VerseLearner.data.ReferenceException;
import org.gamenet.application.VerseLearner.data.ReferenceParts;
import org.gamenet.application.VerseLearner.data.Verse;

public class FlatFileVerseReferenceVerseTextParser implements VerseParser
{

    public List<Verse> parseInputStream(InputStream inputStream)
        throws IOException, ReferenceException
    {
        List<Verse> verseList = new ArrayList<Verse>();
        Reader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = bufferedReader.readLine();
        while (null != line)
        {
            while (0 == line.trim().length())
            {
                line = bufferedReader.readLine();
                if (null == line)
                {
                    break;
                }
                continue;
            }
            if (null == line)
            {
                break;
            }
            
            ReferenceParts reference = new ReferenceParts(line);
    
            String verseText = bufferedReader.readLine();
            if (null == verseText)
            {
                break;
            }
            while (0 == verseText.trim().length())
            {
                verseText = bufferedReader.readLine();
                if (null == verseText)
                {
                    break;
                }
            }
            
            Verse verse = new Verse(reference, verseText);
            verseList.add(verse);
    
            line = bufferedReader.readLine();
        }
        
        return verseList;
    }

}
