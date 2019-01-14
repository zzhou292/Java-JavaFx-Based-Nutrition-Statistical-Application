/**
 * Filename: BPTree.java
 * 
 * Project: team project P5
 * 
 * Authors: sapan (sapan@cs.wisc.edu), Zhikang Meng, Jason ZHOU, Kejia Fan, James Higgins,YULU Zou
 *
 * Semester: Fall 2018
 * 
 * Course: CS400
 * 
 * Lecture: 002
 * 
 * Due Date: Before 10pm on December 12, 2018 Version: 1.0
 * 
 * Credits: NONE
 * 
 * Bugs: no known bugs
 */
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
 * @author sapan (sapan@cs.wisc.edu), Meng, Zhou, Zou, Fan, Higgins
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


  /**
   * Inserts the key and value in the appropriate nodes in the tree
   * 
   * Note: key-value pairs with duplicate keys can be inserted into the tree.
   * 
   * @param key
   * @param value
   */
  @Override
  public void insert(K key, V value) {
    root.insert(key, value);

  }


  /**
   * Gets the values that satisfy the given range search arguments.
   * 
   * Value of comparator can be one of these: "<=", "==", ">="
   * 
   * Example: If given key = 2.5 and comparator = ">=": return all the values with the corresponding
   * keys >= 2.5
   * 
   * If key is null or not found, return empty list. If comparator is null, empty, or not according
   * to required form, return empty list.
   * 
   * @param key to be searched
   * @param comparator is a string
   * @return list of values that are the result of the range search; if nothing found, return empty
   *         list
   */
  @Override
  public List<V> rangeSearch(K key, String comparator) {
    if (!comparator.contentEquals(">=") && !comparator.contentEquals("==")
        && !comparator.contentEquals("<="))
      return new ArrayList<V>();
    return root.rangeSearch(key, comparator);
  }


  /**
   * Returns a string representation for the tree This method is provided to students in the
   * implementation.
   * 
   * @return a string representation
   */
  @SuppressWarnings("unchecked")
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
   * @author sapan , Meng, Zhou, Zou, Fan, Higgins
   */
  private abstract class Node {

    // List of keys
    List<K> keys;

    boolean isInternal;// check if this node is an internal node

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
     * @return Node This represents the new generated brother
     */
    abstract Node split();

    /**
     * Gets the values that satisfy the given range search arguments of this node recursively.
     * 
     * @param key to be searched
     * @param comparator is a string
     * @return list of values that are the result of the range search; if nothing found, return
     *         empty list
     */
    abstract List<V> rangeSearch(K key, String comparator);

    /**
     * Check if the current is over flow
     * 
     * @return boolean This returns ture when a node is over flow
     */
    abstract boolean isOverflow();


    /**
     * Returns a string representation for the this node
     * 
     * @return a string representation
     */
    public String toString() {
      return keys.toString();
    }

  } // End of abstract class Node

  /**
   * This class represents an internal node of the tree. This class is a concrete sub class of the
   * abstract Node class and provides implementation of the operations required for internal
   * (non-leaf) nodes.
   * 
   * @author sapan , Meng, Zhou, Zou, Fan, Higgins
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
     * Gets the first leaf key of this internal node
     * 
     * @return key the first leaf key of this internal node
     */
    K getFirstLeafKey() {
      K firstLeafKey = children.get(0).getFirstLeafKey();
      return firstLeafKey;
    }

    /**
     * Check if the current is over flow
     * 
     * @return boolean This returns ture when this internal node is over flow
     */
    boolean isOverflow() {
      return children.size() > branchingFactor;
    }

    /**
     * Inserts key and value in the appropriate leaf node and balances the tree if required by
     * splitting through this internal node
     * 
     * @param key
     * @param value
     */
    @SuppressWarnings("unchecked")
    void insert(K key, V value) {
      Node kid = searchForKid(key);// get the correct kid for insert the key
      if (!kid.isInternal) {// when kid is a leaf node, insert as a leaf node
        ((LeafNode) kid).insertKid(key, value);
      } else// otherwise insert as this internal node
        kid.insert(key, value);
      if (kid.isOverflow()) {// when kid is over flow after the insertion, split it
        Node brother = kid.split();
        // add the new generated brother to this node
        insertkid(brother.getFirstLeafKey(), brother);
      }
      if (root.isOverflow()) {
        // if the root is over flow, split it
        Node brother = split();
        InternalNode newRoot = new InternalNode();
        // add the current root and its brother to the new root
        newRoot.keys.add(brother.getFirstLeafKey());
        newRoot.children.add(this);
        newRoot.children.add(brother);
        root = newRoot;
      }
    }

    /**
     * This add the new generated brother to this node at correct position
     * 
     * @param firstLeafKey first leaf key of the brother
     * @param brother the new generated brother after split
     */
    private void insertkid(K firstLeafKey, Node brother) {
      int kidIndex = -1;// initialize the insert position to a unreachable
      // once the correct position found, save the index and break
      for (int i = 0; i < keys.size(); ++i) {
        if (keys.get(i).compareTo(firstLeafKey) > 0) {
          kidIndex = i;
          break;
        }
      }
      // still not found, just insert it as the first kid
      if (kidIndex == -1) {
        keys.add(firstLeafKey);
        children.add(brother);
      } else {// otherwise insert to an appropriate position
        keys.add(kidIndex, firstLeafKey);
        children.add(kidIndex + 1, brother);
      }
    }

    /**
     * Find the correct kid for the key insertion
     * 
     * @param key key to be inserted
     * @return Node this represents the reference of the correct kid to be inserted key
     */
    private Node searchForKid(K key) {
      int kidIndex = -1;// initialize the insert position to a unreachable
      // once the correct position found, save the index and break
      for (int i = 0; i < keys.size(); ++i) {
        if (keys.get(i).compareTo(key) > 0) {
          kidIndex = i;
          break;
        }
      }
      if (kidIndex == -1)// if not find, the kid to be inserted is the last kid
        return children.get(keys.size());
      else// otherwise return the correct kid to be inserted
        return children.get(kidIndex);
    }

    /**
     * Perform a range search start from the first kid
     * 
     * @param key to be inserted
     * @return Node this represents the correct position to range search
     */
    private Node rangeSearchFirstKid(K key) {
      int kidIndex = -1;// initialize the insert position to a unreachable
      // once the correct position found, save the index and break
      for (int i = 0; i < keys.size(); ++i) {
        if (keys.get(i).compareTo(key) >= 0) {
          kidIndex = i;
          break;
        }
      }
      if (kidIndex == -1)// if not find, the kid to be inserted is the last kid
        return children.get(keys.size() - 1);
      return children.get(kidIndex);// otherwise return the correct kid to be inserted
    }

    /**
     * Perform a range search start from the last kid
     * 
     * @param key the key to be inserted
     * @return Node This returns the correct position to insert the kid
     */
    private Node rangeSearchLastKid(K key) {
      int kidIndex = -1;// initialize the insert position to a unreachable
      // once the correct position found, save the index and break
      for (int i = 0; i < keys.size(); ++i) {
        if (keys.get(i).compareTo(key) >= 0) {
          kidIndex = i;
        }
      }
      if (kidIndex == -1)// if not find, the kid to be inserted is the last kid
        return children.get(keys.size());
      else // otherwise return the correct kid to be inserted
        return children.get(kidIndex + 1);
    }

    /**
     * Gets the new sibling created after splitting the node
     * 
     * @return Node This represents the new generated brother
     */
    Node split() {
      InternalNode brother = new InternalNode();
      int start = keys.size() / 2 + 1;
      int end = keys.size();
      // copy the last half to the brother
      brother.keys.addAll(keys.subList(start, end));
      brother.children.addAll(children.subList(start, end + 1));
      // clear the last half of this node
      keys.subList(start - 1, end).clear();
      children.subList(start, end + 1).clear();
      return brother;
    }

    /**
     * Gets the values that satisfy the given range search arguments of this node recursively.
     * Through this internal node
     * 
     * @param key to be searched
     * @param comparator is a string
     * @return list of values that are the result of the range search; if nothing found, return
     *         empty list
     */
    List<V> rangeSearch(K key, String comparator) {
      if (comparator.equals("<=")) {// to get <= result, range search start from last kid
        return rangeSearchLastKid(key).rangeSearch(key, comparator);
      } else// otherwise start from the first kid
        return rangeSearchFirstKid(key).rangeSearch(key, comparator);
    }

  } // End of class InternalNode

  /**
   * This class represents a leaf node of the tree. This class is a concrete sub class of the
   * abstract Node class and provides implementation of the operations that required for leaf nodes.
   * 
   * @author sapan , Meng, Zhou, Zou, Fan, Higgins
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
     * insert the key and value pair to the correct position
     * 
     * @param key key to be inserted
     * @param value value to be inserted
     */
    void insertKid(K key, V value) {
      int valueIndex = -1;
      // search for right node to insert
      for (int i = 0; i < keys.size(); ++i) {
        if (key.compareTo(keys.get(i)) < 0) {
          valueIndex = i;
          break;
        }
      }
      // if not found
      if (valueIndex == -1) {
        keys.add(key);
        values.add(value);
        // else add key to certain position
      } else {
        keys.add(valueIndex, key);
        values.add(valueIndex, value);
      }

    }

    /**
     * Gets the first leaf key of this leaf node
     * 
     * @return key the first leaf key of this internal node
     */
    K getFirstLeafKey() {
      return keys.get(0);
    }

    /**
     * Check if the current is over flow
     * 
     * @return boolean This returns ture when this leaf node is over flow
     */
    boolean isOverflow() {
      return values.size() > branchingFactor - 1;
    }

    /**
     * Inserts key and value in the appropriate leaf node and balances the tree if required by
     * splitting
     * 
     * @param key
     * @param value
     */
    void insert(K key, V value) {
      int valueIndex = -1;
      // search for right node to insert
      for (int i = 0; i < keys.size(); ++i) {
        if (key.compareTo(keys.get(i)) < 0) {
          valueIndex = i;
          break;
        }
      }
      // if not found
      if (valueIndex == -1) {
        keys.add(key);
        values.add(value);
        // else add key to certain position
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
     * Gets the new sibling created after splitting the node
     * 
     * @return Node This represents the new generated brother
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
     * Gets the values that satisfy the given range search arguments of this node recursively.
     * 
     * @param key to be searched
     * @param comparator is a string
     * @return list of values that are the result of the range search; if nothing found, return
     *         empty list
     */
    List<V> rangeSearch(K key, String comparator) {
      List<V> result = new ArrayList<V>();
      LeafNode current = this;
      while (current != null) {
        // kit and vit to iterate through keys and values of this leaf node
        Iterator<K> kIt = current.keys.iterator();
        Iterator<V> vIt = current.values.iterator();
        int count = 0;// count set to 0 by default
        // while still more key to iterate
        while (kIt.hasNext()) {
          // move to the next key and value
          K keyc = kIt.next();
          V value = vIt.next();
          // this represents the comparison result of key and this key being iterated
          int cmp1 = keyc.compareTo(key);
          // once match the expectation of each condition ,add it to the result list
          if ((cmp1 <= 0 && comparator.equals("<=")) || (cmp1 == 0 && comparator.equals("=="))
              || (cmp1 >= 0 && comparator.equals(">="))) {
            if (comparator.equals("<=")) {
              if (count == 0) {// nothing in the list, add to first position
                result.add(0, value);
                count++;
              } else// Otherwise add to the second position
                result.add(1, value);
            } else// otherwise add to a correct position
              result.add(value);
          }

        }
        if (comparator.equals("<=")) {
          // to get the <= results, move to the previous leaf node
          current = current.previous;
        } else {// otherwise move to the next leaf node
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
