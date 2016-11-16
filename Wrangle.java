package twitterset;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wrangle consists of methods that obtain information from a list of tweets.
 */
public class Wrangle {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of Twitter messages with distinct ids, not mutated by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every message in the list.
     */
    public static Period getPeriod(List<Message> tweets) {
    	
    	if(tweets.isEmpty()){
    		Period timespan = new Period(Instant.EPOCH, Instant.EPOCH);
    		return timespan;
    	}
    	
    	Instant oldestTimeStamp = tweets.get(0).getTimestamp();
    	Instant newestTimeStamp = tweets.get(0).getTimestamp();
    	
    	for (int i = 1; i < tweets.size(); i++){
    		if (oldestTimeStamp.isAfter(tweets.get(i).getTimestamp())){
    			oldestTimeStamp = tweets.get(i).getTimestamp();
    		}
    		
    		if (newestTimeStamp.isBefore(tweets.get(i).getTimestamp())){
    			newestTimeStamp = tweets.get(i).getTimestamp();
    		}
    	}
    	
    	Period timespan = new Period(oldestTimeStamp,newestTimeStamp);
    	
        return timespan;

    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Message.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Message> tweets) {
    	
    	Set<String> users = new HashSet<String>();
    	String pattern = "\\B@[\\w-_]+";
    	Pattern r = Pattern.compile(pattern);
    	
    	for (int i = 0; i < tweets.size(); i++){
    		String text = tweets.get(i).getText();
    		Matcher m = r.matcher(text);
    		
    		while (m.find()) {
    			users.add(m.group().toLowerCase().substring(1));
    	    }
    		
    	}
        
    	return users;

    }


    
    public static void main(String[] args)
    {
    	
    	final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
        final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
        
        final Message tweet1 = new Message(1, "alyssa", "is it blah@uic.edu reasonable to @dick talk about @JAck so much?", d1);
        final Message tweet2 = new Message(2, "humptiedumptie", "tech talk in @jack 30 minutes #hype", d2);
        Set<String> users = getMentionedUsers(Arrays.asList(tweet1, tweet2));
        
        System.out.print(users);
        
        List<Message> usersEmpty = new ArrayList<Message>();
        
        Period timespan = getPeriod(usersEmpty);
        System.out.print(timespan);
    }
    
    
    
    
}
