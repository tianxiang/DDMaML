package miningTweet;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class Parser implements Closeable, Iterator<Tweet>{

	protected BufferedReader tweetReader;
	/** TRUE if we have treated the last tweet*/
	  protected boolean hasReachedEnd = false;
	
	protected String cachedLine;
	/** Constructs a parser from the tweet corpus file*/
	public Parser(File tweetFile) throws IOException {
	  tweetReader = new BufferedReader(new InputStreamReader(new FileInputStream(tweetFile), "UTF-8"));
	}	  
	
	/** Returns the next page*/
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		if (cachedLine ==null)
			try {
				cachedLine = tweetReader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e);
			}
		return cachedLine != null;
	}

	@Override
	public Tweet next() {
		// TODO Auto-generated method stub
		
		try {
		    String line = (cachedLine == null)? tweetReader.readLine(): cachedLine;
		    cachedLine = null;
		    if (line == null) throw new NoSuchElementException("Reached end of tweet file");
		    else  return (new Tweet(line));
		    } catch (IOException e) {
		    	throw new RuntimeException(e);
		    }
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Remove in Tweet parser");
	}
	public static void main(String[] args) throws IOException {
		//String file = "/Volumes/SSD disk Yiru/saclay/distributed data mining & machine learning/TP/NewYorkOneWeek/NewYork-2015-2-23";
		int cpt = 0;
		try (Parser parser = new Parser(new File(args[0]))){
			while (parser.hasNext()){
				System.out.print(parser.next().hashtags);
				cpt++;
				System.out.println(cpt);
			}
		}
	}

}
