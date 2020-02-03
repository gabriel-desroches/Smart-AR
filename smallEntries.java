import java.util.*;

public class smallEntries <T1, T2>
{
	public LinkedList<Node> sequence = new LinkedList<Node>();

	public LinkedList<Node> getSequence()
	{
		return sequence;
	}

	class Node 
	{ 
		T1 license;
		ArrayList<T2> cars;

		Node(T1 l, ArrayList<T2> c) 
		{ 
			license = l;
			cars = (ArrayList<T2>)c.clone(); //do we clone or assign the same address ? will use less memory 
		} 

		public T1 getKey() 
		{
			return license;
		}

		public ArrayList<T2> getValue() 
		{
			return cars;
		}

		public String toString()
		{
			return (license + ", " + getValue());
		}
	}

	public void sequenceAdd(T1 key, T2 value) //check threshold
	{
		ArrayList<T2> cars = new ArrayList<T2>();
		cars.add(value);
		boolean dne = true;

		if(sequence.size() == 0)
		{
			Node element = new Node(key, cars);
			sequence.add(element);
		}
		else
		{
			for(int i = 0; i<sequence.size();i++)
			{
				Node node = (Node)sequence.get(i);

				if(node.getKey().equals(key))
				{
					node.getValue().add(0,value);
					dne = false;
				}
			}
			if(dne)
			{
				Node element = new Node(key, cars);


				int size = sequence.size();
				for(int i=0; i<size; i++)
				{
					//current node in the linked list
					Node node = (Node)sequence.get(i);
					//current key in the node
					String s1 = (String) node.getKey();
					//key we wish to insert 
					String s2 = (String) key;

					if(i!= sequence.size()-1)
					{
						//key in the next node
						String s3 = (String) nextKey(node.getKey());

						if(s1.compareTo(s2) > 0)
						{
							sequence.add(i,element); 
							break;
						}
						//if s1 < s2 and s3 > s2
						else if(s1.compareTo(s2) < 0 && s3.compareTo(s2) > 0)
						{
							sequence.add(i+1, element);
							break;
						}

					}
					else
					{
						//if the last element is greater than the element that we wish to add
						if(s1.compareTo(s2) > 0)
						{
							sequence.add(i, element);
							break;
						}
						//if the last element is less than the element that we wish to add
						else 
						{
							sequence.addLast(element);
							break;
						}
					}
				}
			}
		}
	}
	
	//second version of sequence add with an arraylist as parameter for transfers
	public void sequenceAdd(T1 key, ArrayList<T2> value) //check threshold
	{
	//	ArrayList<T2> cars = (ArrayList<T2>) value.clone();
		boolean dne = true;

		if(sequence.size() == 0)
		{
			Node element = new Node(key, value);
			sequence.add(element);
		}
		else
		{
			for(int i = 0; i<sequence.size();i++)
			{
				Node node = (Node)sequence.get(i);

				if(node.getKey().equals(key))
				{
					node.getValue().addAll(0,value);
					dne = false;
				}
			}
			if(dne)
			{
				Node element = new Node(key, value);


				int size = sequence.size();
				for(int i=0; i<size; i++)
				{
					//current node in the linked list
					Node node = (Node)sequence.get(i);
					//current key in the node
					String s1 = (String) node.getKey();
					//key we wish to insert 
					String s2 = (String) key;

					if(i!= sequence.size()-1)
					{
						//key in the next node
						String s3 = (String) nextKey(node.getKey());

						if(s1.compareTo(s2) > 0)
						{
							sequence.add(i,element); 
							break;
						}
						//if s1 < s2 and s3 > s2
						else if(s1.compareTo(s2) < 0 && s3.compareTo(s2) > 0)
						{
							sequence.add(i+1, element);
							break;
						}

					}
					else
					{
						//if the last element is greater than the element that we wish to add
						if(s1.compareTo(s2) > 0)
						{
							sequence.add(i, element);
							break;
						}
						//if the last element is less than the element that we wish to add
						else 
						{
							sequence.addLast(element);
							break;
						}
					}
				}
			}
		}
	}

	public void remove(T1 key) //check threshold 
	{
		for(int i = 0; i<sequence.size();i++)
		{
			Node node = (Node)sequence.get(i);

			if(node.getKey().equals(key))
			{
				sequence.remove(i);
				break;
			}
		}
	}

	public ArrayList<T2> getValues(T1 key) 
	{
		ArrayList<T2> cars = new ArrayList<T2>();
		for(int i = 0; i<sequence.size();i++)
		{
			Node node = (Node)sequence.get(i);

			if(node.getKey().equals(key))
			{
				cars = (ArrayList<T2>)node.getValue().clone();
				break;
			}

		}
		return cars;
	}

	public T1 nextKey(T1 key)
	{
		for(int i = 0; i<sequence.size();i++)
		{
			Node node = (Node)sequence.get(i);

			if(node.getKey().equals(key) && i + 1 <sequence.size())
			{
				Node n = (Node)sequence.get(i+1);
				T1 nextK = n.getKey();
				return nextK;
			}
		}
		return null;
	}

	public T1 prevKey(T1 key)
	{
		for(int i = 0; i<sequence.size();i++)
		{
			Node node = (Node)sequence.get(i);

			if(node.getKey().equals(key) && i - 1 >= 0)
			{
				Node n = (Node)sequence.get(i-1);
				T1 nextK = n.getKey();
				return nextK;
			}
		}
		return null;
	}

	public ArrayList<T2> previousCars(T1 key)
	{
		ArrayList<T2> cars = new ArrayList<T2>();
		cars = getValues(key);
		cars.remove(0);
		return cars;
	}

	public ArrayList<T1> getKeys()
	{
		ArrayList<T1> keys = new ArrayList<T1>(sequence.size());
		for(int i = 0; i < sequence.size(); i++)
		{
			Node n = (Node)sequence.get(i);
			keys.add(n.getKey());
		}
		return keys;
	}	

	public boolean sequenceDuplicate(T1 key)
	{
		for(int i = 0; i < sequence.size(); i++)
		{
			Node n = (Node)sequence.get(i);
			if(n.getKey().equals(key))
				return true;
		}
		return false;
	}

	//method that empties the linked list. Called by smart AR when transferring to tree
	public void clear() {
		sequence.clear();
		
	}


}//How can the memory be small if the methods want us to return sequences, therefore it is at least O(n)?