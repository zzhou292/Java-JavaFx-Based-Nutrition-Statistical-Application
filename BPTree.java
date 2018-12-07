package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    this.branchingFactor = branchingFactor;
    root = new LeafNode();
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
    if (!comparator.contentEquals(">=") && !comparator.contentEquals("==")
        && !comparator.contentEquals("<="))
      return new ArrayList<V>();
    return root.rangeSearch(key, comparator);
  }


  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    Queue<List<Node>> queue = new LinkedList<List<Node>>();
    queue.add(Arrays.asList(root));
    StringBuilder sb = new StringBuilder();
    while (!queue.isEmpty()) {
      Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
      while (!queue.isEmpty()) {
        List<Node> nodes = queue.remove();
        sb.append('{');
        Iterator<Node> it = nodes.iterator();
        while (it.hasNext()) {
          Node node = it.next();
          sb.append(node.toString());
          if (it.hasNext())
            sb.append(", ");
          if (node instanceof BPTree.InternalNode)
            nextQueue.add(((InternalNode) node).children);
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
    
    boolean isInternal;

    /**
     * Package constructor
     */
    Node() {
      this.keys = new ArrayList<K>();
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
      this.children = new ArrayList<Node>();
      isInternal = true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#getFirstLeafKey()
     */
    K getFirstLeafKey() {
      K firstLeafKey = children.get(0).getFirstLeafKey();
      return firstLeafKey;
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#isOverflow()
     */
    boolean isOverflow() {
      return children.size() > branchingFactor;
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
     */
    void insert(K key, V value) {
      Node kid = searchForKid(key);
      if(!kid.isInternal) {
    	  ((LeafNode) kid).insertKid(key, value); 
      }
      else kid.insert(key, value);
      if(kid.isOverflow()) {
          Node brother = kid.split();
          insertkid(brother.getFirstLeafKey(), brother);
      }
      if (root.isOverflow()) {
        Node brother = split();
        InternalNode newRoot = new InternalNode();
        newRoot.keys.add(brother.getFirstLeafKey());
        newRoot.children.add(this); 
        newRoot.children.add(brother);
        root = newRoot;
      }
    }

    private void insertkid(K firstLeafKey, Node brother) {
    	int kidIndex = -1;
        for(int i = 0; i < keys.size(); ++i) {
      	  if(keys.get(i).compareTo(firstLeafKey) > 0) {
      		  kidIndex = i;
      		  break;
      	  }
        }
        if(kidIndex == -1) {
        	keys.add(firstLeafKey);
        	children.add(brother);
        }else {
        	keys.add(kidIndex, firstLeafKey);
        	children.add(kidIndex+1, brother);
        }
    }

    private Node searchForKid(K key) {
      int kidIndex = -1;
      for(int i = 0; i < keys.size(); ++i) {
    	  if(keys.get(i).compareTo(key) > 0) {
    		  kidIndex = i;
    		  break;
    	  }
      }
      if(kidIndex == -1)  return children.get(keys.size());
      else return children.get(kidIndex);
    }
    
    private Node rangeSearchFirstKid(K key) {
    	int kidIndex = -1;
    	for(int i = 0; i < keys.size(); ++i) {
    		if(keys.get(i).compareTo(key) >= 0) {
    			kidIndex = i;
    			break;
    		}
    	}
    	if(kidIndex == -1) return children.get(keys.size() - 1);
    	return children.get(kidIndex);
    }
    
    private Node rangeSearchLastKid(K key) {
    	int kidIndex = -1;
    	for(int i = 0; i < keys.size(); ++i) {
    		if(keys.get(i).compareTo(key) >= 0) {
    			kidIndex = i;
    		}
    	}
    	if(kidIndex == -1) return children.get(keys.size());
    	else return children.get(kidIndex + 1);
    }
    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#split()
     */
    Node split() {
      InternalNode brother = new InternalNode();
      int start = keys.size() / 2 + 1;
      int end = keys.size();

      brother.keys.addAll(keys.subList(start, end));
      brother.children.addAll(children.subList(start, end + 1));

      keys.subList(start - 1, end).clear();
      children.subList(start, end + 1).clear();
      return brother;
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
     */
    List<V> rangeSearch(K key, String comparator) {
      if(comparator.equals("<=")) {
    	  return rangeSearchLastKid(key).rangeSearch(key,comparator);
      }
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
     * 
     * @see BPTree.Node#getFirstLeafKey()
     */
    K getFirstLeafKey() {
      return keys.get(0);
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#isOverflow()
     */
    boolean isOverflow() {
    	return values.size() > branchingFactor - 1;
    }

    /**
     * (non-Javadoc)
     * 
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
        // than assign the this new root as our root
        Node brother = split();
        InternalNode newRoot = new InternalNode();
        newRoot.keys.add(brother.getFirstLeafKey());
        newRoot.children.add(this);
        newRoot.children.add(brother);
        root = newRoot;
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
     * 
     * @see BPTree.Node#rangeSearch(Comparable, String)
     */
    List<V> rangeSearch(K key, String comparator) {
      List<V> result = new LinkedList<V>();
      LeafNode current = this;
      while (current != null) {
        Iterator<K> kIt = current.keys.iterator();
        Iterator<V> vIt = current.values.iterator();

        while (kIt.hasNext()) {
          K keyc = kIt.next();
          V value = vIt.next();
          int cmp1 = keyc.compareTo(key);
          if ((cmp1 <= 0 && comparator.equals("<=")) || (cmp1 == 0 && comparator.equals("=="))
              || (cmp1 >= 0 && comparator.equals(">=")))
            result.add(value);
        }
        if(comparator.equals("<=")) {
        	current = current.previous;
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
    List<Double> filteredValues = bpTree.rangeSearch(0.8d, "<=");
    System.out.println("Filtered values: " + filteredValues.toString());
  }

} // End of class BPTree
