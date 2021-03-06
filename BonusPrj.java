import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * This class represents Elements list of hash table
 * */


class linkListElements {

	JSONObject v;
	String k;
	linkListElements next;
	linkListElements previous;

	public linkListElements(String key, JSONObject value){
		k = key;
		v = value;
		next = null;
		previous = null;
	}

}

/*
 * This class represents Slots list of hash table
 * */

class linkListSlot {

	int index;

	linkListSlot next;
	linkListSlot previous;
	linkListElements elements;

	public linkListSlot(int i){
		index = i;
		next = null;
		previous = null;
		elements = null;
	}
}
/*Relevant information of user will be stored as an object*/
class UserDetails1{
	public UserDetails1(String uId, String uName, long statCount, long retCount, long frenCount,
			long follCount, long favCount2) {
		Id = uId;
		name = uName;
		statusCount = statCount;
		retweetCount = retCount;
		friendCount = frenCount;
		followerCount = follCount;
		this.favCount = favCount2;

	}

	String Id, name;
	long statusCount,retweetCount,friendCount,followerCount,favCount;
	// Method to return the Followers Count of User
	public  long getFollowerCount(){
		return followerCount;
	}
	// Method to return the Status Count of User
	public long getStatusCount(){
		return statusCount;
	}
	// Method to return the Friends Count of User
	public long getFriendsCount(){
		return friendCount;
	}
}

// Class to sort the Followers Count of User
class sortFolloCount implements Comparator<UserDetails1>{


	public int compare(UserDetails1 user1, UserDetails1 user2) {

		return (int) (user1.getFollowerCount()- user2.getFollowerCount());
	}

}

//Class to sort the Status Count of User
class sortStatCount implements Comparator<UserDetails1>{


	public int compare(UserDetails1 user1, UserDetails1 user2) {

		return (int) (user1.getStatusCount()- user2.getStatusCount());
	}

}

//Class to sort the Friends Count of User
class sortFrndCount implements Comparator<UserDetails1>{


	public int compare(UserDetails1 user1, UserDetails1 user2) {

		return (int) (user1.getFriendsCount()- user2.getFriendsCount());
	}

}

/*
 * This class creates the hash table of twitter data and contains following operations:
 * 1. Creates Hash Table
 * 2. Print all slots
 * 3. Prints all keys in a given slot
 * 4. Prints all information of a given element in a given slot
 * 5. Calculates Hash Value
 * */
class hashMap{
	linkListSlot start;
	// @param slotCount - store the count of slots in hash table.
	int slotCount = 0;

	linkListElements list = null;
	linkListSlot Map = null;
	JSONArray jlist = new JSONArray();
	JSONObject obj1 = new JSONObject();

	/*
	 * This methods calculates hash value of given key
	 * Key - Twitter user ID
	 * */

	public int hashCode(long key){

		return (int)  Math.floor(key/10000000);
	}
	/*
	 * This method inserts all the key values in hash map
	 * Firstly, it calculates the hash value of the key value and then create slot node using hash value and create an element node for it
	 * In case of twitter data, 
	 * Key - Twitter user ID
	 * Value - Data related to that user ID
	 * 
	 * */

	public void putValue(String key, JSONObject value){

		//Calculate Hash Value
		int hash = hashCode(Long.parseLong(key));

		// if hash table is empty, create a new slot and a new element node

		if (Map == null) {
			// Create new slot
			Map = new linkListSlot(hash);
			// 	Create new element node
			list = new linkListElements(key,value);
			// @param start - point to new slot
			start = Map; 
			//@param elements - of new node will point to new element node
			Map.elements = list;
			// @param slotcount is increased by 1
			slotCount++;
			return;

			// if hash table is not empty
		}else{
			// @param currNode - point to start of hash table slots
			linkListSlot currNode = start;

			//if index of first slot of hash table matches hash value
			if(start.index == hash)
			{
				//create new element node @param tmpNode
				linkListElements tmpNode = new linkListElements(key,value);
				//next pointer of new element node will point to already existing first node of the list 
				tmpNode.next = start.elements;
				//previous pointer of already existing first node will point to new element node
				start.elements.previous = tmpNode;
				//element pointer of first slot of the hash table will now point to new element node
				start.elements = tmpNode;
				return;


			}
			//loop through all the slot index and check if any slot index matches the hash value
			while(currNode.next != null && currNode.index != hash)
				//move to next node
				currNode = currNode.next;

			//if @param currNode is the last slot of the hash table and it's index is not same as hash value
			if(currNode.next == null && currNode.index != hash){
				//create a new slot
				linkListSlot tmpMap = new linkListSlot(hash);
				//create a new element node
				linkListElements tmpNode = new linkListElements(key,value);
				// add new slot to the end and adjust previous and next pointers
				currNode.next = tmpMap;
				tmpMap.previous = currNode;
				tmpMap.elements = tmpNode;
				// @param slotCount increased by 1
				slotCount++;
				return;
			}

			//if index of currNode matches hash value but there are elements in that slot
			if(currNode.index == hash && currNode.elements != null){
				// create a new element node @param tmpNode
				linkListElements tmpNode = new linkListElements(key,value);
				//next pointer of new element node will point to already existing first element node of the list
				tmpNode.next = currNode.elements;
				//previous pointer of already existing first element node will point to new element node
				currNode.elements.previous = tmpNode;
				//element pointer of current slot of the hash table will now point to new element node
				currNode.elements = tmpNode;
				return;
			}
			//if index of currNode matches hash value but there are no elements in that slot 
			if(currNode.index == hash && currNode.elements == null){
				// create a new element node @param tmpNode
				linkListElements tmpNode = new linkListElements(key,value);
				//element pointer of current slot of the hash table will now point to new element node
				currNode.elements = tmpNode;
			}
		}


	}
	
	

	/*
	 * This method prints the information of a given key in a given slot
	 * Firstly, it checks whether given key exists in a given slot.
	 * If yes, it internally calls printJSON() method to print all key information
	 * otherwise, displays error
	 * */
	public void printValue(int slot, String key){
		linkListSlot pos = start;
		linkListSlot currNode = null;


		//Loop through end of the slot List
		while(pos.next != null){
			// If slot index matches the given slot
			if(pos.index == slot){

				currNode = pos;
				break;

			}
			// Move to next slot node
			pos = pos.next;

		}
		//If it's the last slot node and slot index matches
		if (pos.next == null && pos.index == slot)

			currNode = pos;

		if(currNode != null && currNode.elements != null){
			linkListElements posLink = currNode.elements;
			while(posLink.next != null){

				if(posLink.k.equals(key)){
					//System.out.println(posLink.v);
					printJSON(posLink.v);

				}
				posLink = posLink.next;
			}
			//If it's the last element node
			if(posLink.next == null && posLink.k.equals(key)){
				//Print element value
				printJSON(posLink.v);

			}
			//System.err.print("Elements Not Found");

		}else{
			//System.err.print("Elements Not Found");
		}

	}


	/*
	 * This method prints the information of a given key in a proper format
	 * */
	public void printJSON(JSONObject value){

		JSONObject newJson = (JSONObject) value;
		System.out.println("SOCIAL PROFILE: ");
		System.out.println("-------------");
		JSONObject innerJson = (JSONObject) newJson.get("user");
		JSONObject innerJsonEntities = (JSONObject) newJson.get("entities");
		JSONArray innerJsonUrls = (JSONArray) innerJsonEntities.get("urls");
		System.out.println("Name: " + innerJson.get("name"));
		System.out.println("Description: " + innerJson.get("description"));
		System.out.println("Screen Name: " + innerJson.get("screen_name"));
		System.out.println("Time Zone: " + innerJson.get("time_zone"));
		System.out.println("Favourites Count: " + innerJson.get("favourites_count"));
		System.out.println("Followers Count: " + innerJson.get("followers_count"));
		System.out.println("Friends Count: " + innerJson.get("friends_count"));
		System.out.println("Status Count: " + innerJson.get("statuses_count"));
		System.out.println("Retweet Count: " + newJson.get("retweet_count"));
		System.out.println("Tweet: " + newJson.get("text"));

		for( int i = 0; i< innerJsonUrls.size();i++){
			JSONObject jsonObjectUrl = (JSONObject) innerJsonUrls.get(i);
			String url = (String) jsonObjectUrl.get("url");
			System.out.println("URLs Shared: " + url);
		}
		System.out.println("--**--**--**--**--**--**--**--**--**--**--**--**--");

	}


	/*
	 * This method will create JSON objects for every individual user record
	 * The JSON object will contain following information:
	 * 1. Name
	 * 2. Status COunt
	 * 3. Followers COunt
	 * 4. Friend Count
	 * This method internally calls addToArray() method to add all objects to one JSON Array
	 * */

	@SuppressWarnings("unchecked")
	public JSONArray createObject(String name, long stat_count, long foll_count, long frnd_count){

		JSONObject obj = new JSONObject();
		//JSONObject obj1 = new JSONObject();

		obj.put("name",name);
		obj.put("status_count",stat_count );
		obj.put("foll_count", foll_count);
		obj.put("frnd_count", frnd_count);
		JSONArray retArr = addToArray(obj);
		return retArr;

	}
	/*
	 * This method will add all JSON objects to JSON Array before writing them to a file
	 * */
	@SuppressWarnings("unchecked")
	public JSONArray addToArray(JSONObject obj){
		jlist.add(obj);
		return jlist;


	}
	
	/*
	 * This method will write JSON objects into a JSON File which will be used later
	 * to display graphs for comparison purposes
	 * */

	@SuppressWarnings("unchecked")
	public void writeToFile(JSONArray jArray){

		//Adding JSON array to JSON Object
		obj1.put("items", jArray);

		//Create a file if doesn't exists
		try {
			File file = new File("text1.json");
			if(!file.exists()){
				file.createNewFile();

			}
			//Write JSON Object to file, appends if file already exists
			FileWriter fileWritter = new FileWriter(file,true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(obj1.toJSONString());
			bufferWritter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

/*
 * This class reads the twitter data file(Json format)
 * It will sort the list of user records internally based upon following criterias:
 * 1. Status Count - To find out the most active twitter user
 * 2. Followers COunt - To find out the user with maximum followers
 * 3. Friends COunt - To find out the user with maximum friends
 * It will display the output of points 1 to 3 to user screen
 * It will also write output of top 5 followed user to a JSON file which will be used to display the graphs
 * 
 * */

public class BonusPrj {
	static hashMap hm = new hashMap();
	static List <UserDetails1> arr = new ArrayList<UserDetails1>();
	public static void main(String args[]){


		JSONParser parser = new JSONParser();
		JSONArray jArray = new JSONArray();
		System.out.println("Enter Filename: ");
		// @param scanner - reads user input for filename
		Scanner scanner = new Scanner(System.in);

		String fileName = scanner.next();

		try {
			//Parse the json file using simple JSON
			Object obj = parser.parse(new FileReader(fileName));
			//Create JSON Array of records in JSON file
			JSONArray jsonArray = (JSONArray) obj;
			//Reading JSON Objects, JSON Array for every tweet record
			for( int i = 0; i< jsonArray.size();i++){
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				JSONObject innerJson = (JSONObject) jsonObject.get("user");
				String userId = (String) innerJson.get("id_str");
				String name = (String) innerJson.get("name");
				long statCount = (Long) innerJson.get("statuses_count");
				long retCount = (Long) jsonObject.get("retweet_count");
				long frenCount = (Long) innerJson.get("friends_count");
				long follCount = (Long) innerJson.get("followers_count");
				long favCount = (Long) innerJson.get("favourites_count");

				//Calls put method to create hash map using 2 parameter - twitter user ID and its twitter data record as JSON Object
				hm.putValue(userId, jsonObject);
				//Create an object of typw UserDetails1 for every user
				arr.add(new UserDetails1(userId,name,statCount,retCount,frenCount,follCount,favCount));

			}


			//Sorting the user list by followers count
			sortFolloCount uFoll = new sortFolloCount();
			Collections.sort(arr,uFoll);


			// This loop will create JSON object for 5 most followed tweeters
			for (int i = arr.size()-1;i>(arr.size()-6);i--)
			{

				UserDetails1 st1 = arr.get(i);
				//Calling createObject() method to create JSON object for top 5 followed users 
				jArray = hm.createObject(st1.name,st1.statusCount,st1.followerCount,st1.friendCount);

			}	
			//Call writeToFile method to write the output to JSON file
			hm.writeToFile(jArray);

			// To find the user with maximum followers count
			UserDetails1 qs3 = arr.get(arr.size()-1);
			System.out.println("\n\nUser with maximum followers Count(" + qs3.followerCount+ ") is: " + qs3.Id + " : " + qs3.name);
			int code = hm.hashCode(Long.parseLong(qs3.Id));
			hm.printValue(code, qs3.Id);

			// To find the user with maximum status count
			sortStatCount uStat = new sortStatCount();
			//Sorting the user list by status count
			Collections.sort(arr,uStat);
			UserDetails1 qs4 = arr.get(arr.size()-1);
			System.out.println("\n\nUser with maximum number of tweets is: " + qs4.Id + " : " + qs4.name + " with status Count: " + qs4.statusCount);
			code = hm.hashCode(Long.parseLong(qs4.Id));
			hm.printValue(code, qs4.Id);


			// To find the user with maximum friends count
			sortFrndCount uFrnd = new sortFrndCount();
			//Sorting the user list by friends count
			Collections.sort(arr,uFrnd);
			UserDetails1 qs5 = arr.get(arr.size()-1);
			System.out.println("\n\nUser with maximum friends is: " + qs5.Id + " : " + qs5.name + " with friend Count: " + qs4.friendCount);
			code = hm.hashCode(Long.parseLong(qs5.Id));
			hm.printValue(code, qs5.Id);


			//Exception Handling
		} catch (FileNotFoundException e) {
			System.err.println("File Not Found Exception" + e);
		} catch (IOException e) {
			System.err.println("IO Exception" + e);
		} catch (ParseException e) {
			System.err.println("Parse Exception" + e);
		}

	}

}

