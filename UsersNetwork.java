package twitterset;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Comparator;

/**
 * UsersNetwork provides methods that operate on a social network.
 * 
 * A social network of users is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, this is true even if 
 * A is followed by other people in the network.
 * Twitter usernames are case-insensitive, so "brad" is the same as "BRAd".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 */
public class UsersNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Brad follows Colin
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Brad follows Colin is if Brad
     *         @-mentions Colin in a message. 
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Message> tweets) {
    	
//    	Set<String> usernames = new HashSet<String>();
//    	for (int i = 0; i < tweets.size(); i++){
//    		usernames.add(tweets.get(i).getAuthor().toLowerCase());
//    	}
    	
        Map<String, Set<String>> usersMap = new HashMap<String, Set<String>>();
        
        for (int i = 0; i < tweets.size(); i++){
    		String username = tweets.get(i).getAuthor().toLowerCase(); //iterate through usernames in tweets
    		
    		Set<String> followedUsersSet = new HashSet<String>(); //pre-allocate for the users the current user follows
    		for (int j = 0; j < tweets.size(); j++){
    			
    			if( tweets.get(j).getAuthor().equals(username) ){ //we check only for the user who is up next
    				Set<String> mentionedUsers = Wrangle.getMentionedUsers(Arrays.asList(tweets.get(j))); //get all users the current user follows
    				mentionedUsers.remove(username); // the user cannot follow himself
    				followedUsersSet.addAll(mentionedUsers);
    			}
    			
    		}    		
    		usersMap.put(username, followedUsersSet);    		
        }
              
        return usersMap;
    }

    /**
     * Find the people in a network who have the greatest influence,
     * meaning that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        
    	List<String> allNamesList = new ArrayList<String>();
    	Set<String> allNamesSet = new HashSet<String>();
    	
    	for (Map.Entry<String, Set<String>> entry: followsGraph.entrySet()){
    		Set<String> usersSet = entry.getValue();
    		replaceSet(usersSet);
    		List<String> usersList = new ArrayList<String>(usersSet);//already lowercase
    		allNamesList.addAll(usersList);
    		allNamesSet.addAll(usersSet);
    	}
    	
    	HashMap<String,Integer> freqMap = new HashMap<String,Integer>();
    	
    	for(String name: allNamesSet){
    		int freq = Collections.frequency(allNamesList, name);
    		freqMap.put(name, freq);
    	}
    	
    	TreeMap<String,Integer>freqMapOrdered = sortMapByValue(freqMap);
    	
    	List<String> usersOrdered = new ArrayList<String>(freqMapOrdered.keySet());
    	List<String> mappedUsers = new ArrayList<String>(followsGraph.keySet());
    	replaceList(mappedUsers);
    	
    	List<String> newMappedUsers = new ArrayList<String>(mappedUsers); 
    	newMappedUsers.removeAll(usersOrdered);
    	usersOrdered.addAll(newMappedUsers);

    	return usersOrdered;
    }
    
    // convert all strings' set elements to lowercase
    public static void replaceSet(Set<String> strings)
    {
        String[] stringsArray = strings.toArray(new String[0]);
        for (int i=0; i<stringsArray.length; ++i)
        {
            stringsArray[i] = stringsArray[i].toLowerCase();
        }
        strings.clear();
        strings.addAll(Arrays.asList(stringsArray));
    }
    
    // convert all strings' list elements to lowercase
    public static void replaceList(List<String> strings)
    {
        ListIterator<String> iterator = strings.listIterator();
        while (iterator.hasNext())
        {
            iterator.set(iterator.next().toLowerCase());
        }
    }
    
    //a method to sort a map by value
	public static TreeMap<String, Integer> sortMapByValue(HashMap<String, Integer> map){
		Comparator<String> comparator = new ValueComparator(map);
		//TreeMap is a map sorted by its keys. 
		//The comparator is used to sort the TreeMap by keys. 
		TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
		result.putAll(map);
		return result;
	}


    
    public static void main(String[] args)
    {
    	
        final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
        final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
        final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
        final Instant d4 = Instant.parse("2016-02-17T12:00:00Z");
        final Instant d5 = Instant.parse("2016-02-17T13:00:00Z");
        final Instant d6 = Instant.parse("2016-02-17T14:00:00Z");
        final Instant d7 = Instant.parse("2016-02-17T15:00:00Z");
        
        final Message tweet1 = new Message(1, "alyssa", "who@uic.edu is it reasonable @jack to talk about tech so much? @donald", d1);
        final Message tweet2 = new Message(2, "humptiedumptie", "tech talk in 30 minutes #hype", d2);
        final Message tweet3 = new Message(3, "jack", "blah @jack meets @jack and @dick", d3);
        final Message tweet4 = new Message(4, "alyssa", "what is the fuss with @humptiedumptie and @dick @jack", d4);
        final Message tweet5 = new Message(5, "donald", "what is the fuss with @Liz and @jack and @dick", d5);
        
        final Message tweet6 = new Message(6, "john", "blah-blah I do not follow anyone", d6);
        final Message tweet7 = new Message(7, "betsy", "what an awesome day", d7);
        Map<String, Set<String>> network = guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet6, tweet7));
        //Map<String, Set<String>> network2 = guessFollowsGraph(Arrays.asList(tweet2, tweet1));
        
        System.out.print(network + "\n");
        System.out.print(influencers(network));
    }
}

// A comparator that compares Strings
class ValueComparator implements Comparator<String>{

	HashMap<String, Integer> map = new HashMap<String, Integer>();

	public ValueComparator(HashMap<String, Integer> map){
		this.map.putAll(map);
	}

	@Override
	public int compare(String s1, String s2) {
		if(map.get(s1) >= map.get(s2)){
			return -1;
		}else{
			return 1;
		}	
	}
}
