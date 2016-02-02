package miningTweet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class TweetFilter {
	//TweetFilter filters the duplicated tweets (identified by hashtags) and write the filtered tweet in a new file
	//protected BufferedWriter tweetFilterWriter;
	//a hashset containing all identical tweet hashtags
	protected String tweetFileName = new String();
	public TweetFilter(String tweetFile){
		this.tweetFileName = tweetFile;
	}
	/****/
	public HashSet<HashSet<String>> filter() throws IOException {
		HashSet<HashSet<String>> tweetFiltered = new HashSet<HashSet<String>>();
		//int cpt = 0;
		try(Parser parser = new Parser(new File(this.tweetFileName))){
			while (parser.hasNext()){
				HashSet<String> hashtags = parser.next().hashtags;
				if (!tweetFiltered.contains(hashtags)){
					if (!hashtags.isEmpty()) {
						tweetFiltered.add(hashtags);
						//cpt ++;
						//if (cpt < 100){System.out.print(hashtags);System.out.println(cpt);}
					}
					//if (hashtags.isEmpty()) System.out.println(cpt);
					
				}
			}
		}
		return tweetFiltered;
	}
	
	public static void main(String[] args)throws IOException{
		String file ="/Volumes/SSD disk Yiru/saclay/distributed data mining & machine learning/TP/NewYorkOneWeek/NewYork-2015-2-23";
		TweetFilter tf = new TweetFilter(file);
		tf.filter();
	}
}
