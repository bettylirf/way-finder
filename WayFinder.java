package project4;

import java.util.ArrayList;

/**
 * This application uses recursive algorithm to find all possible ways 
 * out of a number puzzle specified in the command line argument.
 * 
 * The puzzle uses an array of positive integers, and a valid way out is
 * from index 0 to the last index in the array. At each step, the distance to be traveled
 * is identified as the entry corresponding to the index of the array. And both possibility
 * of traveling to left and right is explored.
 * 
 * If there exists at least one way out of the puzzle, the visual representation
 * of the way out as well as the total number of ways will be printed to the screen.
 *
 * @author Betty Li
 *
 */
public class WayFinder {
	/** An ArrayList of Integer used to store the indexes
	 * of the valid way out of the number puzzle. */
	public static ArrayList<Integer> allIndex;
	
	public static void main(String[] args) {
		
		//verify that the command line argument exists 
		if (args.length == 0) {
			System.err.println("Usage Error: the program expects an integer array an argument.\n");
			System.exit(1);
		}
		
		//initialize an integer array that is of the same length with that of 
		//the command line arguments
		int[] arr = new int[args.length];
		try {
			for(int i = 0; i < args.length; i++) {
				int num = Integer.parseInt(args[i]);
				
				//verify that each input number is between 0 and 99
				if(num < 0 || num > 99) 
					throw new IllegalArgumentException("Error: all values have to be "
							+ "non-negative integers in the range of 0 to 99 inclusive");
				
				//verify that the last number is zero
				if(i == args.length - 1 && !(num == 0))
					throw new IllegalArgumentException("Error: the last value should be zero.");
				
				//store all numbers in an integer array
				arr[i] = num;
			}
		} catch(NumberFormatException ex) {
			System.out.println("Error: the puzzle values shouldn't contain non-integer values.");
			System.exit(1);
		} catch(IllegalArgumentException ex) {
			System.out.print(ex.getMessage());
			System.exit(1);
		}
		
		//create a new ArrayList of Integers for storing the indexes of each movement that
		//builds up the way out of the number puzzle
		allIndex = new ArrayList<>();
		
		//initialize an int variable that accepts the counting of number of ways out from
		//running the search for way out algorithm with initial index of 0 and the number array
		int numOfWays = findWay(0,arr);
		
		//if the number of ways out equals to zero, print there isn't a way out of the puzzle;
		//else number of ways out is larger than zero, therefore print the total numbers of way out.
		if(numOfWays == 0)
			System.out.println("No way through this puzzle.");
		else
			System.out.println("There are "+ numOfWays + " ways through the puzzle.");
	}
	
	/**
	 * This method implements the algorithm that find and print all possible ways through the puzzle,
	 * and returns the total number of ways to go through the puzzle without repeated positions.
	 * 
	 * @param index int the current index
	 * @param arr an array of positive integers used as the puzzle to travel through
	 * @return int that counts the total number of ways out of the puzzle
	 */
	public static int findWay(int index, int[] arr) {
		//if index equals to the last index of the number array
		//we have found one way out;
		//call the printWay method to print the representation of each step traveled to the console
		//increment the return value by one and keep on finding other possible ways
		if(index == arr.length - 1) {
			allIndex.add(index);
			printWay(allIndex, arr);
			return 1 + findWay(index + 1, arr);
		} 
		//otherwise, we are not at the finish line
		else {
			//if the current index is out of bound or we have already visited this index before
			//get back to the last position that has another choice that we haven't explored
			//from there, make a move right
			if (index < 0 || index > arr.length - 1 || allIndex.contains(index)) {
				while(!(allIndex.isEmpty()) && index >= allIndex.get(allIndex.size() - 1)) {
					index = allIndex.remove(allIndex.size() - 1);
				}
				//if the ArrayList storing indexes is empty
				//all possible combinations of ways have been explored
				//return zero to terminate the algorithm
				if(allIndex.isEmpty())
					return 0;
				index = allIndex.get(allIndex.size() - 1);
				return findWay(index + arr[index], arr);
			}
			//otherwise, the current index is within the range of the number puzzle
			//add the current index to the ArrayList storing all indexes and make a move left
			else {
				//if the entry in the array corresponding to the current index is zero
				//the finish line is unreachable; terminate the algorithm
				if(arr[index] == 0)
					return 0;
				allIndex.add(index);
				return findWay(index - arr[index], arr);
			}
		}
	}
	
	/**
	 * This method prints to the console the representation of each move that builds up 
	 * to one way out of the puzzle.
	 * Each entry that is involved in each move is followed by L or R that indicates the 
	 * direction of the movement.
	 * 
	 * @param allIndex an ArrayList of Integer that stores the indexes of each move through the puzzle
	 * @param arr an array of positive integers used as the number puzzle to travel through
	 */
	public static void printWay(ArrayList<Integer> allIndex, int[] arr) {
		for(int i = 0; i < allIndex.size() - 1; i++) {
			String line = "[";
			int currentIndex = allIndex.get(i);
			int nextIndex = allIndex.get(i + 1);
			int j;
			for(j = 0; j < arr.length - 1; j++) {
				Character dir = ' ';
				if(j == currentIndex) {
					//if current index is smaller than the next index
					//this move is a move to the right
					if(currentIndex < nextIndex)
						dir = 'R';
					//otherwise, this move is a move to the left
					else
						dir = 'L';
				}
				if(j == 0)
					line += String.format("%2d%c,", arr[j], dir);
				else
					line += String.format("%3d%c,", arr[j], dir);
			}
			line += String.format("%3d%c", 0, ' ') + "]\n";
			System.out.print(line);
		}
		System.out.println();
	}
}
