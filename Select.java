package twitterset;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
//import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
//import java.util.Set;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

/**
 * Select consists of methods that select a list of tweets matching a
 * condition.
 */
public class Select {

    /**
     * Find tweets written by a particular user.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param username
     *            Twitter username, required to be a valid Twitter username as
     *            defined by Tweet.getAuthor()'s spec.
     * @return all and only the tweets in the list whose author is username,
     *         in the same order as in the input list.
     */
    public static List<Message> writtenBy(List<Message> tweets, String username) {
        
        List<Message> filtered = new ArrayList<Message>();
        
        for (int i = 0; i < tweets.size(); i++){
    		String author = tweets.get(i).getAuthor().toLowerCase();
        	
    		if (author.equals(username.toLowerCase())){
    			filtered.add(tweets.get(i));
    		}
        }
        
        return filtered;
    }

    /**
     * Find tweets that were sent during a particular period.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param timespan
     *            timespan
     * @return all and only the tweets in the list that were sent during the timespan,
     *         in the same order as in the input list.
     */
    public static List<Message> inPeriod(List<Message> tweets, Period timespan) {
    	
    	List<Message> inPeriod = new ArrayList<Message>();
    	
        for (int i = 0; i < tweets.size(); i++){
    		Instant tweetTimestamp = tweets.get(i).getTimestamp();
        	
    		if ( (tweetTimestamp.isAfter(timespan.getStart()) && tweetTimestamp.isBefore(timespan.getEnd())) || 
    				tweetTimestamp.equals(timespan.getStart()) || tweetTimestamp.equals(timespan.getEnd()) ){
    			inPeriod.add(tweets.get(i));
    		}
        }
        
        return inPeriod;
    }
    

    /**
     * Find tweets that contain certain words.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets. 
     *            A word is a nonempty sequence of nonspace characters.
     * @return all the tweets in the list such that the message text (represented 
     *         by a sequence of nonempty words bounded by space characters 
     *         and the ends of the string) includes at least one of the words 
     *         found in the words' list. Word comparison is not case-sensitive,
     *         so "Trump" is the same as "trump".  The returned messages are in the
     *         same order as in the input list.
     */
    public static List<Message> containing(List<Message> tweets, List<String> words) {
        
    	ListIterator<String> iterator = words.listIterator();
    	List<String> wordsLower = new ArrayList<String>();
    	while (iterator.hasNext()){
    		wordsLower.add(iterator.next().toLowerCase());
    	}
    	
        List<Message> wordTweets = new ArrayList<Message>();
        
        for (int i = 0; i < tweets.size(); i++){
        	List<String> textWords = Arrays.asList(tweets.get(i).getText().toLowerCase().split("\\s+"));
        	//.replaceAll("\\p{P}", "")
        	
        	if(!Collections.disjoint(wordsLower, textWords)){
        		wordTweets.add(tweets.get(i));
        	}
        	
        }
        
        return wordTweets;
    }


    
    public static void main(String[] args)
    {
    	
        final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
        final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
        final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
        final Instant d4 = Instant.parse("2016-02-17T12:00:00Z");
        final Instant d5 = Instant.parse("2016-02-17T13:00:00Z");
        
        final Message tweet1 = new Message(1, "alyssa", "who@uic.edu is it reasonable to talk about tech so much?", d1);
        final Message tweet2 = new Message(2, "humptiedumptie", "tech talk in 30 minutes #hype", d2);
        final Message tweet3 = new Message(3, "jack", "blah @Dick meets @jack", d3);
        final Message tweet4 = new Message(4, "jane", "what is the matter with @jack and @dick", d4);
        final Message tweet5 = new Message(5, "donald", "what is the fuss with @Liz and @JaCk", d5);
        List<Message> filtered = writtenBy(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5),"alyssa");
        System.out.print(filtered);
    }
}
