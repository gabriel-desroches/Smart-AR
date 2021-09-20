/*
 * 
 * Written by Gabriel Desroches and Briana Toia
 * Submitted for COMP 352 Data Structures + Algorithms
 * 
 * This "smart" AR is essentially a method of storing and retrieving fictional license plates
 * and the associated cars. It uses two different ADTs to store the data depending on how many are 
 * currently stores and a user set threshold.
 * 
 * When under the threshold, plates are stored in a linked list. 
 * When over the threshold, plates are stored in an AVL tree. 
 * 
 * When crossing over or under the threshold, all the data is transferred to the appropriate 
 * data type and the other is deleted. Threshold can be changed on an established AR, and transfers will occur 
 * automatically if needed.
 * 
 * Plates are stored as nodes with key value pair, where the license plates themselves are the key and the 
 * value is an arrayList containing all the information regarding the cars that have had or currently had that plate.
 * Duplicate plate values indicate a new car is registered with that plate. The new car's data is added onto
 * the arrayList.
 */

/*
 * example of code to run to test: 
 * large threshold: demonstration of speed difference when transferring to tree
----------------------------------------------------------
	SmartAR<String, String> AR = new SmartAR<String, String>();
	AR.setKeyLength(8);
	AR.setThreshold(1000);
	try {
				Scanner scanner = new Scanner(new FileInputStream("ar_test_file2.txt"));
				int i = 0; 
				while(scanner.hasNext()) {
					i++;
					String next = scanner.next();
					System.out.println(next+"    "+i);
					AR.add(next, "Initial Car");
				}
				scanner.close();
			} catch (FileNotFoundException e)  { 
				System.out.println("Invalid file");
			}

 */


//The smart AR acts as the controller, it chooses whether to use the linkedList's methods or the Tree's.

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class SmartAR <T1, T2>
{
	int length; 
	int numberOfElements;
	int threshold;
	private smallEntries<T1, T2> obj = new smallEntries<T1, T2>();
	private Tree<T1, T2> AVLTree = new Tree<T1, T2>();

	public static void main(String[] args)
	{
		
		SmartAR<String, String> AR = new SmartAR<String, String>();
		AR.setKeyLength(8);
		AR.setThreshold(700);

		try {
					Scanner scanner = new Scanner(new FileInputStream("ar_test_file1.txt"));
					int i = 0; 
					while(scanner.hasNext()) {
						i++;
						String next = scanner.next();
						System.out.println(next+"    "+i);
						AR.add(next, "Initial Car");
					}
					System.out.println(AR.allKeys());
					scanner.close();
				} catch (FileNotFoundException e)  { 
					System.out.println("Invalid file");
				}
		
		System.out.println(AR.nextKey("00471I5"));
		System.out.println(AR.prevKey("00471I5"));
		AR.remove("00471I5");
		System.out.println(AR.nextKey("000SAYU8A2O"));
		System.out.println(AR.prevKey("000SAYU8A2O"));
		System.out.println(AR.prevKey("00CNXDXG25R"));

		//remove bottom comment to visually show key has been removed
		System.out.println(AR.allKeys());

		System.exit(0);

	}

	public void setKeyLength(int l)
	{
		if(l>=6 && l<=12)
			length = l;
		else
		{
			System.out.println("Incorrect length. Please choose a value from 6 to 12");
			System.exit(0);
		}
	}

	//setting the threshold. Will also check if a transfer is needed
	public void setThreshold(int t)	
	{
		if(t>=100 && t<=500000) {
			/*check with transfer before setting new threshold.
			  if the number of elements is smaller than the current threshold but larger
			  than t, need to transfer to tree */
			if (numberOfElements <= threshold && numberOfElements > t) transferToTree();

			/* if the number of elements is greater than the threshold but smaller
			 * than t, transfer to sequence*/	
			if (numberOfElements > threshold && numberOfElements <= t) transferToSequence();

			//update to the new threshold now that changes have been made.
			threshold = t;
		}
		else
		{
			System.out.println("Incorrect threshold. Please choose a value from 100 to 500,000");
			System.exit(0);
		}
	}

	public String[] generate(int n) 
	{
		String[] licenseKeys = new String[n];
		int count = 0;
		boolean duplicate = false;

		do  {
			String generatedKey = randomAlphaNumeric();

			if (numberOfElements <= threshold) 
				duplicate = obj.sequenceDuplicate((T1)generatedKey);
			else 
			{
				//check tree for duplicates with generatedKey
				//if duplicate present, duplicate = true;
				duplicate = AVLTree.duplicateExists((T1)generatedKey);
			}
			if (!duplicate) 
			{
				licenseKeys[count]= generatedKey;
				count ++; 
			}

		} while (count < n);


		System.out.println("Generated keys:");
		for (int i = 0; i<n; i++) {
			System.out.print(licenseKeys[i]+" | ");
		}
		return licenseKeys;
	}

	public void add(T1 key, T2 value) {

		if ( ((String) key).length()<6 && ((String) key).length() >12)																		
			System.out.println(key + " is an invalid key length, please enter a value of length between 6 and 12 inclusively");
		else {

			//purpose of this boolean is to prevent a transfer in case of adding duplicates
			//while numberOfElements == threshold+1 (right after we just transferred)
			//boolean is false if we just added a duplicate
			boolean validTransfer = true;

			//if the key was a duplicate, don't increment as no new node was created
			if (numberOfElements <= threshold) 
			{
				if (!obj.sequenceDuplicate(key)) numberOfElements++;
				else validTransfer = false;
				obj.sequenceAdd(key, value);

			}
			else 
			{ 
				if (!AVLTree.duplicateExists(key)) numberOfElements++;
				else validTransfer = false;
				AVLTree.add(key, value); 
			}

			if(numberOfElements == threshold+1 && validTransfer) transferToTree();
		}
	}

	public void remove(T1 key) {

		if (AVLTree.duplicateExists(key) || obj.sequenceDuplicate(key)) {
			if (numberOfElements <= threshold) obj.remove(key);
			else AVLTree.remove(key); 

			numberOfElements--;
			if(numberOfElements == threshold) transferToSequence();
		} else System.out.println("Key doesn't exist and cannot be removed.");
	}

	private void transferToTree() {
		System.out.println("Transferring to Tree");
		ArrayList<T1> keys = (ArrayList<T1>) obj.getKeys().clone();
		for (int i = 0; i < numberOfElements; i++) {
			AVLTree.add(keys.get(i), obj.getValues(keys.get(i)));		//cast here is weird, we need to make our adds work with arraylists if theres more than one car
		}

		obj.clear();
	}

	private void transferToSequence() {
		System.out.println("Transferring to Sequence");
		ArrayList<T1> allKeys = AVLTree.inOrder();
		for(int i =0; i<numberOfElements; i++) {
			obj.sequenceAdd(allKeys.get(i), AVLTree.getValues(allKeys.get(i)));
		}

		AVLTree.clear();
	}

	public ArrayList<T1> allKeys() {
		if (numberOfElements <= threshold) return obj.getKeys();
		else return AVLTree.inOrder(); 
	}

	public T1 nextKey(T1 key) {
		if (numberOfElements <= threshold) return obj.nextKey(key);
		else return AVLTree.nextKey(key); 
	}

	public T1 prevKey(T1 key) {
		if (numberOfElements <= threshold) return obj.prevKey(key);
		else return AVLTree.previousKey(key); 
	}

	public ArrayList<T2> getValues(T1 key) {
		if (numberOfElements <= threshold) return obj.getValues(key);
		else return AVLTree.getValues(key); 
	}

	public ArrayList<T2> previousCars(T1 key) {
		if (numberOfElements <= threshold) {
			return obj.previousCars(key);
		}
		else {       
			return AVLTree.previousCars(key); 
		}
	}

	//The code below for generating random alphanumeric 
	//was taken and adapted from: https://dzone.com/articles/generate-random-alpha-numeric
	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	public String randomAlphaNumeric() 
	{
		int l = length;
		StringBuilder builder = new StringBuilder();
		while (l-- != 0) 
		{
			int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}



}