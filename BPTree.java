package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;



/**
 * Implementation of a B+ tree to allow efficient access to many different indexes of a large data
 * set. BPTree objects are created for each type of index needed by the program. BPTrees provide an
 * efficient range search as compared to other types of data structures due to the ability to
 * perform log_m N lookups and linear in-order traversals of the data items.
 * 
 * @author sapan (sapan@cs.wisc.edu)
 *
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

  // Root of the tree
  private Node root;

  // Branching factor is the number of children nodes
  // for internal nodes of the tree
  private int branchingFactor;


  /**
   * Public constructor
   * 
   * @param branchingFactor
   */
  public BPTree(int branchingFactor) {
    if (branchingFactor <= 2) {
      throw new IllegalArgumentException("Illegal branching factor: " + branchingFactor);
    }
    this.branchingFactor = branchingFactor;  //Initialize branchingFactor
    root = new LeafNode();  //Initialize root as a leafnode at first
  }


  /*
   * (non-Javadoc)
   * 
   * @see BPTreeADT#insert(java.lang.Object, java.lang.Object)
   */
  @Override
  public void insert(K key, V value) {
	  root.insert(key, value);
    
  }


  /*
   * (non-Javadoc)
   * 
   * @see BPTreeADT#rangeSearch(java.lang.Object, java.lang.String)
   */
  @Override
  public List<V> rangeSearch(K key, String comparator) {
	//If the comparator is illegal, return empty list
    if (!comparator.contentEquals(">=") && !comparator.contentEquals("==")
        && !comparator.contentEquals("<="))
      return new ArrayList<V>();  
    //else return list
    return root.rangeSearch(key, comparator);
  }


  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
	//create a new queue
    Queue<List<Node>> queue = new LinkedList<List<Node>>();
    queue.add(Arrays.asList(root));
    StringBuilder sb = new StringBuilder();
    
    //use a while loop to iterate all nodes
    while (!queue.isEmpty()) {
      Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
      while (!queue.isEmpty()) {
        List<Node> nodes = queue.remove();
        sb.append('{');
        Iterator<Node> it = nodes.iterator();  //use iterator to iterate
        while (it.hasNext()) {
          Node node = it.next();
          sb.append(node.toString());
          if (it.hasNext())
            sb.append(", ");
          if (node instanceof BPTree.InternalNode)
            nextQueue.add(((InternalNode) node).children);  //add node to queue
        }
        sb.append('}');
        if (!queue.isEmpty())
          sb.append(", ");
        else {
          sb.append('\n');
        }
      }
      queue = nextQueue;
    }
    return sb.toString();
  }


  /**
   * This abstract class represents any type of node in the tree This class is a super class of the
   * LeafNode and InternalNode types.
   * 
   * @author sapan
   */
  private abstract class Node {

    // List of keys
    List<K> keys;
    // check if the node is internal or not
    boolean isInternal;

    /**
     * Package constructor
     */
    Node() {
      this.keys = new ArrayList<K>();  //initialize key list
    }

    /**
     * Inserts key and value in the appropriate leaf node and balances the tree if required by
     * splitting
     * 
     * @param key
     * @param value
     */
    abstract void insert(K key, V value);

    /**
     * Gets the first leaf key of the tree
     * 
     * @return key
     */
    abstract K getFirstLeafKey();

    /**
     * Gets the new sibling created after splitting the node
     * 
     * @return Node
     */
    abstract Node split();

    /*
     * (non-Javadoc)
     * 
     * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
     */
    abstract List<V> rangeSearch(K key, String comparator);

    /**
     * 
     * @return boolean
     */
    abstract boolean isOverflow();
    

    public String toString() {
      return keys.toString();
    }

  } // End of abstract class Node

  /**
   * This class represents an internal node of the tree. This class is a concrete sub class of the
   * abstract Node class and provides implementation of the operations required for internal
   * (non-leaf) nodes.
   * 
   * @author sapan
   */
  private class InternalNode extends Node {

    // List of children nodes
    List<Node> children;
    
    /**
     * Package constructor
     */
    InternalNode() {
      super();
      this.children = new ArrayList<Node>();  //initialize children
      isInternal = true;  //initialize as internal
    }

    /**
     * (non-Javadoc)
     * get first left leaf key
     * @see BPTree.Node#getFirstLeafKey()
     */
    K getFirstLeafKey() {
      K firstLeafKey = children.get(0).getFirstLeafKey();
      return firstLeafKey;
    }

    /**
     * (non-Javadoc)
     * check if children gets overflow
     * @see BPTree.Node#isOverflow()
     */
    boolean isOverflow() {
      return children.size() > branchingFactor;
    }

    /**
     * (non-Javadoc)
     * insert nodes into tree by iterating tree from leaf to root
     * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
     */
    void insert(K key, V value) {
      //initialize new kid to be inserted
      Node kid = searchForKid(key);
      
      //if that is a leaf kid, call insertKid method in leaf node
      if(!kid.isInternal) 
    	  ((LeafNode) kid).insertKid(key, value);
      //else call insert method in leaf node or internal node
      else kid.insert(key, value);
      
      //if the node is overflow, split it
      if(kid.isOverflow()) {
          Node brother = kid.split();
          insertkid(brother.getFirstLeafKey(), brother);
      }
      
      //if root is overflow, split and create a new root
      if (root.isOverflow()) {
    	//split and preserve the left part
        Node brother = split();
        //create a new key as parent 
        InternalNode newRoot = new InternalNode();
        
        //choose a key to be in the parent node and add children to it
        newRoot.keys.add(brother.getFirstLeafKey());
        newRoot.children.add(this); 
        newRoot.children.add(brother);
        root = newRoot;  //update the root
      }
    }
    
    /**
     * The private helper method finds the position to put newly split node
     * @param firstLeafKey
     * @param brother
     */
    private void insertkid(K firstLeafKey, Node brother) {
    	int kidIndex = -1;  //initialize the index
    	
    	//loop to find the last key in a series of duplicated keys
    	//or key larger than it
        for(int i = 0; i < keys.size(); ++i) {
      	  if(keys.get(i).compareTo(firstLeafKey) > 0) {
      		  kidIndex = i;
      		  break;
      	  }
        }
        if(kidIndex == -1) {  //there's no same or smaller key, add to the end
        	keys.add(firstLeafKey);
        	children.add(brother);
        }else {  //else insert key in proper position
        	keys.add(kidIndex, firstLeafKey);
        	children.add(kidIndex+1, brother);
        }
    }
    
    /**
     * The helper method finds the proper position to insert key
     * into children
     * @param key
     * @return
     */
    private Node searchForKid(K key) {
      int kidIndex = -1;  //Initialize the kidIndex
      
      //loop to find the proper child node
      for(int i = 0; i < keys.size(); ++i) {
    	  if(keys.get(i).compareTo(key) > 0) {
    		  kidIndex = i;
    		  break;
    	  }
      }
      //if there's no bigger or same nodes, add to the end
      if(kidIndex == -1)  return children.get(keys.size());
      else return children.get(kidIndex);
    }
    
    /**
     * private helper method to find the first node in a series
     * of duplicate nodes
     * @param key
     * @return
     */
    private Node rangeSearchFirstKid(K key) {
    	int kidIndex = -1;  //Initialize the kidIndex
    	
    	//loop to find the node
    	for(int i = 0; i < keys.size(); ++i) {
    		if(keys.get(i).compareTo(key) >= 0) {
    			kidIndex = i;
    			break;
    		}
    	}
    	//If there's no same/bigger node, return the second last node
    	if(kidIndex == -1) return children.get(keys.size() - 1);
    	return children.get(kidIndex);
    }
    
    /**
     * private helper method to find the last node in a series
     * of duplicate nodes
     * @param key
     * @return
     */
    private Node rangeSearchLastKid(K key) {
    	int kidIndex = -1;  //Initialize the kidIndex
    	
    	//loop to find the node
    	for(int i = 0; i < keys.size(); ++i) {
    		if(keys.get(i).compareTo(key) >= 0) {
    			kidIndex = i;
    		}
    	}
    	//if there's no same/bigger node, return the last node
    	if(kidIndex == -1) return children.get(keys.size());
        else return children.get(kidIndex + 1);
    }
    
    /**
     * (non-Javadoc)
     * This method split the node into two and returns the left part
     * @see BPTree.Node#split()
     */
    Node split() {
      //Create a new node to contain the second part
      InternalNode brother = new InternalNode();
      int start = keys.size() / 2 + 1;
      int end = keys.size();
      
      //copy and update keys and children to the left part
      brother.keys.addAll(keys.subList(start, end));
      brother.children.addAll(children.subList(start, end + 1));
      
      //clear the original left part
      keys.subList(start - 1, end).clear();
      children.subList(start, end + 1).clear();
      return brother;  //return the new left part
    }

    /**
     * (non-Javadoc)
     * This method search the tree for leaf nodes that satisfy the 
     * comparator
     * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
     */
    List<V> rangeSearch(K key, String comparator) {
      //to find <= nodes, search start the last key in a series of duplicate keys
      if(comparator.equals("<=")) {
    	  return rangeSearchLastKid(key).rangeSearch(key,comparator);
      }
      //else search from the first key in a series of duplicate keys
      else return rangeSearchFirstKid(key).rangeSearch(key, comparator);
    }

  } // End of class InternalNode

  /**
   * This class represents a leaf node of the tree. This class is a concrete sub class of the
   * abstract Node class and provides implementation of the operations that required for leaf nodes.
   * 
   * @author sapan
   */
  private class LeafNode extends Node {

    // List of values
    List<V> values;

    // Reference to the next leaf node
    LeafNode next;

    // Reference to the previous leaf node
    LeafNode previous;
    /**
     * Package constructor
     */
    LeafNode() {
      super();
      values = new ArrayList<V>();
      this.next = null;
      this.previous = null;
    }
    
    /**
     * insert node into tree without split call
     * @param key
     * @param value
     */
    public void insertKid(K key, V value) {
    	int valueIndex = -1;
        //search for right node to insert
        for(int i = 0; i < keys.size(); ++i) {
      	  if(key.compareTo(keys.get(i)) < 0) {
      		  valueIndex = i;
      		  break;
      	  }
        }
        //if not found
        if (valueIndex == -1) {  
      	keys.add(key);
      	values.add(value);
        //else add key to certain position
        } else {
          keys.add(valueIndex, key);
          values.add(valueIndex, value);
        }
		
	}

	/**
     * (non-Javadoc)
     * return the first left key in node
     * @see BPTree.Node#getFirstLeafKey()
     */
    K getFirstLeafKey() {
      return keys.get(0);
    }

    /**
     * (non-Javadoc)
     * check if the node is overflowed
     * @see BPTree.Node#isOverflow()
     */
    boolean isOverflow() {
    	return values.size() > branchingFactor - 1;
    }

    /**
     * (non-Javadoc)
     * insert root into tree with split call
     * @see BPTree.Node#insert(Comparable, Object)
     */
    void insert(K key, V value) {
      int valueIndex = -1;
      //search for right node to insert
      for(int i = 0; i < keys.size(); ++i) {
    	  if(key.compareTo(keys.get(i)) < 0) {
    		  valueIndex = i;
    		  break;
    	  }
      }
      //if not found
      if (valueIndex == -1) {  
    	keys.add(key);
    	values.add(value);
      //else add key to certain position
      } else {
        keys.add(valueIndex, key);
        values.add(valueIndex, value);
      }
      if (isOverflow()) { 
        // once it is overflow, split it to get a brother
        // create a new root that stores the info of both root and its brother
        Node brother = split();
        InternalNode newRoot = new InternalNode();
        newRoot.keys.add(brother.getFirstLeafKey());
        newRoot.children.add(this);
        newRoot.children.add(brother);
        root = newRoot;  //update the root
      }
    }

    /**
     * split the current node and create a brother at its right. return the reference of its
     * brother.
     * 
     * @see BPTree.Node#split()
     */
    Node split() {
      // crate a new brother of the current node
      LeafNode brother = new LeafNode();
      // copy the info of the right half of the current node into its brother
      int start = keys.size() / 2;
      int end = keys.size();
      brother.keys.addAll(keys.subList(start, end));
      brother.values.addAll(values.subList(start, end));
      // remove the right half of the current node
      keys.subList(start, end).clear();
      values.subList(start, end).clear();
      // update linked list reference
      if (this.next != null) {
        this.next.previous = brother;
        brother.previous = this;
        brother.next = this.next;
        this.next = brother;
    	
      } else {
        this.next = brother;
        brother.previous = this;
        brother.next = null;
      }
      return brother;
    }

    /**
     * (non-Javadoc)
     * This method search through the tree to find nodes that satisfy
     * the comparator
     * @see BPTree.Node#rangeSearch(Comparable, String)
     */
    List<V> rangeSearch(K key, String comparator) {
      List<V> result = new ArrayList<V>();
      LeafNode current = this;
      
      //while to current node is not null
      //iterate the keys in the node
      while (current != null) {
        Iterator<K> kIt = current.keys.iterator();
        Iterator<V> vIt = current.values.iterator();
        int count = 0;  //keep count the keys
       
        while (kIt.hasNext()) {
          K keyc = kIt.next();
          V value = vIt.next();
          
          int cmp1 = keyc.compareTo(key);
          //if the key satisfy the comparator, add the key to the list
          if ((cmp1 <= 0 && comparator.equals("<=")) || (cmp1 == 0 && comparator.equals("=="))
              || (cmp1 >= 0 && comparator.equals(">="))) {
              if (comparator.equals("<=")) {
                  if (count == 0) {
                      result.add(0, value);
                      count++;
                  }
                  else
                      result.add(1, value);
              }
              else
                  result.add(value);
          }

        }
        //if the comparator is <=, iterate backward
        if(comparator.equals("<=")) {
        	current = current.previous;
        //else iterate forward
        }else {
        	current = current.next;
        }
      }
      return result;
    }
  } // End of class LeafNode


  /**
   * Contains a basic test scenario for a BPTree instance. It shows a simple example of the use of
   * this class and its related types.
   * 
   * @param args
   */
  public static void main(String[] args) {
    // create empty BPTree with branching factor of 3
    BPTree<Double, Double> bpTree = new BPTree<>(3);

    // create a pseudo random number generator
    Random rnd1 = new Random();

    // some value to add to the BPTree
    Double[] dd = {0.0d, 0.5d, 0.2d, 0.8d};

    int loopnumber = 20;

    // build an ArrayList of those value and add to BPTree also
    // allows for comparing the contents of the ArrayList
    // against the contents and functionality of the BPTree
    // does not ensure BPTree is implemented correctly
    // just that it functions as a data structure with
    // insert, rangeSearch, and toString() working.
    List<Double> list = new ArrayList<>();
    for (int i = 0; i < loopnumber; i++) {
      Double j = dd[rnd1.nextInt(4)];
      list.add(j);
      bpTree.insert(j, j);
      System.out.println("\n\nTree structure:\n" + j + " is added \n" + bpTree.toString());
    }
    List<Double> filteredValues = bpTree.rangeSearch(0.9d, ">=");
    System.out.println("Filtered values: " + filteredValues.toString());
  }

} // End of class BPTree