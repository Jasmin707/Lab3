/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Ethan Cranmer
 * elc2255
 * 16475
 * Jasmin Rajan
 * JOR427
 * 16470
 * Slip days used: <0>
 * Git URL:https://github.com/Jasmin707/Lab3
 * Fall 2016
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	public static Set<String> dict;
	public static String letters = "abcdefghijklmnopqrstuvwxyz";
	public static ArrayList<String> begin = new ArrayList<>();
	public static ArrayList<String> print = new ArrayList<>();
	
	public static void main(String[] args) throws Exception {
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0])); 
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out;			// default to Stdout
		}
		initialize();
		//check input
		begin = parse(kb);
		if (begin.size() == 0){
			return;
		}
		System.out.println("Running getWordLadderDFS:");
		print = getWordLadderDFS(begin.get(0), begin.get(1));
		printLadder(print);
		System.out.println();
		System.out.println("Running getWordLadderBFS:");
		print = getWordLadderBFS(begin.get(0), begin.get(1));
		printLadder(print);
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		ArrayList<String> out = new ArrayList<>();
		String input;
		Scanner fromInput;
		while(out.size() < 2 && (input = keyboard.nextLine()) != null) {
			if (input.equals("/quit")) {
				return new ArrayList<String>();
			}
			fromInput = new Scanner(input);
			while (fromInput.hasNext()) {
				out.add(fromInput.next().toLowerCase());
			}
		}
		return out;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		ArrayList<String> out = new ArrayList<>();
		ArrayList<String> temp = new ArrayList<>();
		dict = makeDictionary();
		dict.remove(start);
		if(!processDFS( start, end, temp)){
			//means that the search failed
			temp.clear();
		}else{
    		for( int i = 0; i < temp.size(); i++ ){
    			out.add( temp.get( temp.size()-i-1 ));
    		}
    	}
		return out; // replace this line later with real return
	}
	
	private static boolean processDFS(String current, String end, ArrayList<String> ladder) {
		if( current.equals(end) ){
			ladder.add( current );
			return true;
		}else{
			if (dict.size() == 0){
				return false;
			}
		}
		Layer currentLayer = new Layer();
		String temp = new String();
		for(int i = 0; i < current.length(); i ++){
			//iterates through alphabet to change letters
			for( int j = 0; j < 26; j++){
				temp = current.substring(0,i) + letters.charAt(j) + current.substring(i+1);
				if( dict.contains(temp) ){
					//if it is remove it from dictionary and continue traversing dict with new value
					dict.remove(temp);
					currentLayer.getLayer().add(temp);
				}
			}
		}
		//interates through layer
		ArrayList<Integer> priority = new ArrayList<>();
		int count;
		for(int i = 0; i < currentLayer.getLayer().size(); i++){
			count = 0;
			//iterates through string and compares character
			for(int k = 0; k < end.length(); k++){
				if( currentLayer.getLayer().get(i).charAt(k) == end.charAt(k)){
					count++;
				}
			}
			priority.add(count);
		}
		//picking witch variation
		for(int i = end.length(); i >= 0; i--){
			for( int k = 0; k < priority.size(); k++){
				if( priority.get(k) == i ){
					if( processDFS(currentLayer.getLayer().get(k), end, ladder)){
						ladder.add(current);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/*
    private static boolean processDFS(String current, String end, ArrayList<String> ladder, int newI){
		if(newI < 0) return false;
    	if(current.equals(end)){
			ladder.add(current);
			return true;
		}else{
			String next = new String();
			//iterates through current string
			for(int i = newI; i < current.length(); i++){
				//iterate through alphabet
				if(current.charAt(i)!= end.charAt(i)){
					for( int j = 0; j < 26; j++){
						//makes new substring to be checked
						next = current.substring(0,i) + letters.charAt(j) + current.substring(i+1);
						//checking to see if it is in dictionary
						if( dict.contains(next) ){
								//if it is remove it from dictionary and continue traversing dict with new value
							dict.remove(next);
							if( processDFS( next, end, ladder, (i + 1) % 5 ) ){
								//means that a next value reached end
								ladder.add(current);
								return true;
							}else{
								if( processDFS( next, end, ladder, (i + -1) % 5 ) ){										//means that a next value reached end
									ladder.add(current);
									return true;
								}
							}
						}
					}
				}
			}
		}
	return false;
	}
	*/
	
    

	/**
     * this method generates an array list
     * 
     * @param start
     * @param end
     * @return
     */
	public static ArrayList<String> getWordLadderBFS(String start, String end) {
		ArrayList<String> temp = new ArrayList<>();
		ArrayList<String> out = new ArrayList<>();
    	Layer first = new Layer();
    	dict = makeDictionary();
    	first.getLayer().add(start);
    	dict.remove(start);
    	if(!processBFS(first, end, temp, dict.size())){
    		temp.clear();
    	}else{
    		for( int i = 0; i < temp.size(); i++ ){
    			out.add( temp.get( temp.size()-i-1 ));
    		}
    	}
		return out; // replace this line later with real return
	}
    
	private static boolean processBFS(Layer current, String end, ArrayList<String> ladder, int priorDictSize) {
		for(int i = 0; i < current.getLayer().size(); i++ ){
			if( current.getLayer().get(i).equals(end) ){
				ladder.add(current.getLayer().get(i));
				return true;
			}else{
				if ( dict.size() == 0 ){
					return false;
				}
			}
		}
		Layer next = new Layer();
		String temp = new String();
		//iterates through whole arrayList
		for(int i = 0; i < current.getLayer().size(); i ++){
			//iterates through string
			for(int j = 0; j < current.getLayer().get(i).length(); j ++){
				//iterates through alphabet to change letters
				for( int k = 0; k < 26; k++){
					temp = current.getLayer().get(i).substring(0,j) + letters.charAt(k) + current.getLayer().get(i).substring(j+1);
					if( dict.contains(temp) ){
						//if it is remove it from dictionary and continue traversing dict with new value
						dict.remove(temp);
						next.getLayer().add(temp);
					}
				}
			}
		}
		if( priorDictSize == dict.size()){
			return false;
		}
		if( processBFS( next, end, ladder, dict.size()) ){
			String fromNext = ladder.get(ladder.size() - 1);
			for(int i = 0; i < current.getLayer().size(); i ++){
				//iterates through string
				for(int j = 0; j < current.getLayer().get(i).length(); j ++){
					//iterates through alphabet to change letters
					for( int k = 0; k < 26; k++){
						temp = fromNext.substring(0,j) + letters.charAt(k) + fromNext.substring(j+1);
						//checks if the modified previous string is in the current values
						if( current.getLayer().contains(temp) ){
							//if it is add it to the ladder to be returned
							ladder.add(temp);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toLowerCase());
		}
		return words;
	}
	
	public static void printLadder(ArrayList<String> ladder) {
		if( ladder.size() == 0){
			System.out.println("no word ladder can be found between " + begin.get(0) + " and " + begin.get(1) + ".");
		}
		else{
			System.out.println("a " + String.valueOf(ladder.size() - 2) + "-rung word ladder exists between " + ladder.get(ladder.size()-1)+ " and " + ladder.get(0) + ".");
			for(int i = 0; i < ladder.size(); i++){
				System.out.println( ladder.get(i) );
			}
		}
	}
	// TODO
	// Other private static methods here
}
