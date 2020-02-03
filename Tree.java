import java.util.List;
import java.util.ArrayList;

public class Tree<T1, T2> {

	//parts of the code were adapted from information 
	//provided by Mayank Jaiswal on https://www.geeksforgeeks.org/avl-tree-set-1-insertion/
	public class Node{
		int height;
		Node left, right;

		//key and history of cars
		T1 key;
		ArrayList<T2> cars = new ArrayList<T2>();

		//constructor
		Node(T1 l, ArrayList<T2> c) 
		{ 
			key = l;
			cars = (ArrayList<T2>)c.clone(); //do we clone or assign the same address ? will use less memory 
			height = 0;	//could be changed to 1 if including null nodes.
		} 
	}

	Node root = null;
	int numberOfNodes;

	private int height(Node node) {
		return (node == null) ? 0 : node.height;
	}

	private int getBalance(Node node) {
		return (node == null) ? 0 : height(node.left) - height(node.right); 

	}

	private int max(int a, int b) {
		return (a>b) ? a : b;
	}

	
	//code for rotates was written using logic from https://www.youtube.com/watch?v=Y-nmgO8ALjM&t=538s
	//by Dr. Rob Edwards from San Diego State University
	//method to perform a left rotate
	private Node leftRotate(Node a) {
		//check to see if we are moving root

		Node b = a.right;
		a.right = b.left;
		b.left = a;
		a.height = max(height(a.left), height(a.right)) ;
		b.height = max(height(b.left), height(b.right)) ;

		return b;
	}

	private Node rightRotate(Node b) {
		//check to see if we are moving root
		//	boolean switchRoot = false;
		//	if (root == b) switchRoot = true;
		Node a = b.left;
		//System.out.println(b.left);
		b.left = a.right;
		a.right = b;

		a.height = max(height(a.left), height(a.right)) ;
		b.height = max(height(b.left), height(b.right)) ;

		//if root was moved, change root pointer
		//	if (switchRoot) root = a;
		return a;
	}

	//method to print a inorder (sorted) traversal of the tree.
	//call this method which will call lower method with root
	public ArrayList<T1> inOrder() {
		ArrayList<T1> listOfKeys = new ArrayList<T1>();
		return getInOrder(root, listOfKeys);
	}

	private ArrayList<T1> getInOrder(Node node, ArrayList<T1> keys) {
		if (node != null) {
			getInOrder(node.left, keys);
			keys.add(node.key);
			getInOrder(node.right, keys);
		}
		return keys; //is this return ok?
	}

	//method to return the parent node (if any)
	//this method will call the lower one with root
	//possibly broken, parameter should be a key
	//wrote this method accidentally when trying to find prevKey
	private T1 findParent(Node node) {
		//verifies result is not null
		if (parent(root, node) != null) return parent(root, node);
		else return null;

	}

	private T1 parent(Node parent, Node node) {

		String leftKey = (String) parent.left.key;
		String rightKey = (String) parent.right.key;
		String Key = (String) node.key; //key for which we want the previous key

		//we compare a node's key with its children's. If we see a match in the children
		//with the key we are looking for the previous node for, we return the parent as it is the previous
		if(Key.compareTo(leftKey) == 0) return (T1) Key;
		else if (Key.compareTo(rightKey) == 0) return (T1) Key;
		else if (parent(parent.left, node) != null) return parent(parent.left, node); 
		else if (parent(parent.right, node) != null) return parent(parent.right, node);
		else return null;	//return null if no match, only happens when looking at root of tree
	}

	public T1 previousKey(T1 key) {

		ArrayList<T1> keys = inOrder();

		for (int i = 1; i<numberOfNodes; i++) {
			if (key.equals(keys.get(i)))
				return keys.get(i-1);
		}
		return null;
	}

	public T1 nextKey(T1 key) {

		ArrayList<T1> keys = inOrder();

		for (int i = 0; i<numberOfNodes-1; i++) {
			if (key.equals(keys.get(i)))
				return keys.get(i+1);
		}
		return null;
	}

	public boolean duplicateExists(T1 license) {
		if (numberOfNodes == 0) return false;
		else return findDuplicate(root, license);
	}

	private boolean findDuplicate(Node node, T1 license) {
		if (node != null) {
			String keyToAdd = (String) license;
			//current key
			String currentKey = (String) node.key;

			if (keyToAdd.compareTo(currentKey) < 0) {
				return findDuplicate(node.left, license);
			}
			else if (keyToAdd.compareTo(currentKey) > 0) {
				return findDuplicate(node.right, license);
			}
			else if (keyToAdd.compareTo(currentKey) == 0){ 
				return true;
			}

		} return false;
	}

	public ArrayList<T2> getValues(T1 key){
		return getValues(root, key);
	}

	private ArrayList<T2> getValues(Node node, T1 key){
		String searchKey = (String) key;
		String currentKey = (String) node.key;

		if (searchKey.compareTo(currentKey) < 0) {
			return getValues(node.left, key);
		}
		else if (searchKey.compareTo(currentKey) > 0) {
			return getValues(node.right, key);
		}
		else if (searchKey.compareTo(currentKey) == 0){ 
			return node.cars;
		}

		return null;
	}

	public ArrayList<T2> previousCars(T1 key){
		return previousCars(root, key);
	}

	private ArrayList<T2> previousCars(Node node, T1 key){
		String searchKey = (String) key;
		String currentKey = (String) node.key;

		if (searchKey.compareTo(currentKey) < 0) {
			return previousCars(node.left, key);
		}
		else if (searchKey.compareTo(currentKey) > 0) {
			return previousCars(node.right, key);
		}
		else if (searchKey.compareTo(currentKey) == 0){ 
			ArrayList<T2> cars = new ArrayList<T2>();
			cars = getValues(key);
			cars.remove(0);
			return cars;
		}
		return null;
	}

	//call this one from SmartAR, calls insert with root as parameter

	public void add(T1 key, T2 values) {
		root = insert(root, key, values);
	}

	//different add that uses an arraylist for the values  (used in transfers)
	public void add(T1 key, ArrayList<T2> values) {
		root = insert(root, key, values);
	}

	private Node insert(Node node, T1 key, T2 value) {
		//creating the new node and its data

		if (node == null) {
			ArrayList<T2> cars = new ArrayList<T2>();
			cars.add(value);
			Node n = new Node(key, cars);
			numberOfNodes++;
			return n;
		}

		//key we want to add
		String keyToAdd = (String) key;
		//current key
		String currentKey = (String) node.key;

		if (keyToAdd.compareTo(currentKey) < 0) {
			node.left = insert(node.left, key, value);
		}
		else if (keyToAdd.compareTo(currentKey) > 0) {
			node.right = insert(node.right, key, value);
		}
		else {
			node.cars.add(0,value);
			return node;
		}
		//update height of current root node
		node.height = max(height(node.left), height(node.right)) + 1;

		//get balance and then perform balance if needed.
		int balance = getBalance(node);
		//System.out.println("---"+balance);
		//left left case, casting required because of generics
		if (balance > 1 && getBalance(node.left) > 0) {
			return rightRotate(node);
		}

		//left right case
		if (balance > 1 && getBalance(node.left) < 0) {
			node.left = leftRotate(node.left);
			return rightRotate(node);
		}

		//right right case 
		if(balance < -1 && getBalance(node.right) < 0) {
			return leftRotate(node);
		}

		//right left case
		if (balance < -1 && getBalance(node.right) > 0) {
			node.right = rightRotate(node.right);
			return leftRotate(node);
		}

		return node;

	}

	private Node insert(Node node, T1 key, ArrayList<T2> value) {
		//creating the new node and its data

		if (node == null) {
			ArrayList<T2> cars = (ArrayList<T2>) value.clone();
			Node n = new Node(key, cars);
			numberOfNodes++;
			if (root == null) root = n;
			return n;
		}

		//key we want to add
		String keyToAdd = (String) key;
		//current key
		String currentKey = (String) node.key;

		if (keyToAdd.compareTo(currentKey) < 0) {
			node.left = insert(node.left, key, value);
		}
		else if (keyToAdd.compareTo(currentKey) > 0) {
			node.right = insert(node.right, key, value);
		}
		else {
			node.cars.addAll(0, value);
			return node;
		}

		//update height of current root node
		node.height = max(height(node.left), height(node.right)) + 1;

		//get balance and then perform balance if needed.
		int balance = getBalance(node);

		//left left case, casting required because of generics
		if (balance > 1 && getBalance(node.left) > 0) {
			return rightRotate(node);
		}

		//left right case
		if (balance > 1 && getBalance(node.left) < 0) {
			node.left = leftRotate(node.left);
			return rightRotate(node);
		}

		//right right case 
		if(balance < -1 && getBalance(node.right) < 0) {
			return leftRotate(node);
		}

		//right left case
		if (balance < -1 && getBalance(node.right) > 0) {
			node.right = rightRotate(node.right);
			return leftRotate(node);
		}

		return node;
	}

	//call this method from SmartAR to remove a node
	public void remove(T1 key){
		if (!duplicateExists(key)) return;
		root = remove (root, key);
	}
	//method will recursively loop through to find the key to remove. 
	//On the way back up will check all the balances to see if rotates are needed
	private Node remove(Node node, T1 key) {

		//key we are looking for
		String keyToFind = (String) key;
		//current key
		String currentKey = (String) node.key;

		if (keyToFind.compareTo(currentKey) < 0) {
			node.left = remove(node.left, key);
		}
		else if (keyToFind.compareTo(currentKey) > 0) {
			node.right = remove(node.right, key);
		}
		else {	
			if (node.left == null) {
				numberOfNodes--;
				System.out.println("Removed Key "+key);
				return node.right;
			}
			else if (node.right == null) {
				numberOfNodes--;
				System.out.println("Removed Key "+key);
				return node.left;
			}
			else {
				numberOfNodes--;
				Node x = node;
				node = min(x.right);
				node.right = deleteMin(x.right);
				node.left = x.left;
				System.out.println("Removed Key "+key);
			}
		}
		//update height of current root node
		node.height = max(height(node.left), height(node.right)) + 1;

		//get balance and then perform balance if needed.
		int balance = getBalance(node);
		//System.out.println("---"+balance);
		//left left case, casting required because of generics
		if (balance > 1 && getBalance(node.left) > 0) {
			return rightRotate(node);
		}

		//left right case
		if (balance > 1 && getBalance(node.left) < 0) {
			node.left = leftRotate(node.left);
			return rightRotate(node);
		}

		//right right case 
		if(balance < -1 && getBalance(node.right) < 0) {
			return leftRotate(node);
		}

		//right left case
		if (balance < -1 && getBalance(node.right) > 0) {
			node.right = rightRotate(node.right);
			return leftRotate(node);
		}

		return node;

	}

	//methods needed for remove. Used to find the min to remove and replace 
	public T1 min() {
		if (numberOfNodes == 0) System.out.println("Empty tree, no min");
		return min(root).key;
	}

	private Node min(Node node) {
		if(node.left == null) return node;
		return min(node.left);
	}

	public void deleteMin() {
		if (numberOfNodes == 0) System.out.println("Empty tree, no max");
		root = deleteMin(root);
	}

	private Node deleteMin(Node node) {
		if (node.left == null) return node.right;
		node.left = deleteMin(node.left);
		node.height = max(height(node.left), height(node.right)) + 1;

		//balance the tree, may make this process a method
		int balance = getBalance(node);
		//System.out.println("---"+balance);
		//left left case, casting required because of generics
		if (balance > 1 && getBalance(node.left) > 0) {
			return rightRotate(node);
		}

		//left right case
		if (balance > 1 && getBalance(node.left) < 0) {
			node.left = leftRotate(node.left);
			return rightRotate(node);
		}

		//right right case 
		if(balance < -1 && getBalance(node.right) < 0) {
			return leftRotate(node);
		}

		//right left case
		if (balance < -1 && getBalance(node.right) > 0) {
			node.right = rightRotate(node.right);
			return leftRotate(node);
		}

		return node;
	}

	//method to clear the tree. Makes the root null so rest is taken by garbage collection
	public void clear() {
		root = null;
		numberOfNodes = 0;
	}

}