package miningTweet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Tweet {
	JSONObject tweetObj = new JSONObject();
	JSONObject entitiesObj = new JSONObject();
	JSONArray hashtagsJArray = new JSONArray();
	HashSet<String> hashtags = new HashSet<String>();
	//JSONObject entityObj = new JSONObject();
	public Tweet(String s) {
		JSONParser parser = new JSONParser();
		try{
			this.tweetObj = (JSONObject) parser.parse(s);
			this.entitiesObj = (JSONObject) tweetObj.get("entities");
			
			//System.out.println(tweetObj.keySet());
			this.hashtagsJArray = (JSONArray) entitiesObj.get("hashtags");
			for(Object element: hashtagsJArray){
				//System.out.println(((JSONObject)element).get("text"));
				hashtags.add(((JSONObject)element).get("text").toString().toLowerCase());	//to lower case and then process
			}
			//System.out.println(hashtagsJArray);
		}
		catch(ParseException pe){
			System.out.println("position: " + pe.getPosition());
		    System.out.println(pe);
		}
	}
	public boolean hashtagsEquals(Tweet that) {
	    return this.hashtags.equals(that.hashtags);
	}
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException{
		//String file = "/Volumes/SSD disk Yiru/saclay/workspace/DDMaML/src/miningTweet/one_tweet.txt";
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "UTF-8"));
			String tweetTxt = in.readLine();
			Tweet tw = new Tweet(tweetTxt);
			//JSONObject entity =
			//System.out.println(tw.tweetObj.get("entities"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
