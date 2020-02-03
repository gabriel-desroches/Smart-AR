# Smart-AR
Written by Gabriel Desroches and Briana Toia
Submitted for COMP 352 Data Structures + Algorithms
 
This "smart" AR is essentially a method of storing and retrieving fictional license plates
and the associated cars. It uses two different ADTs to store the data depending on how many are 
currently stores and a user set threshold.
 
When under the threshold, plates are stored in a linked list. 
When over the threshold, plates are stored in an AVL tree. 
  
When crossing over or under the threshold, all the data is transferred to the appropriate 
data type and the other is deleted. Threshold can be changed on an established AR, and transfers will occur 
automatically if needed.
  
Plates are stored as nodes with key value pair, where the license plates themselves are the key and the 
value is an arrayList containing all the information regarding the cars that have had or currently had that plate.
Duplicate plate values indicate a new car is registered with that plate. The new car's data is added onto
the arrayList.
