import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Provides an implementation of the Set interface.
 * A doubly-linked list is used as the underlying data structure.
 * Although not required by the interface, this linked list is
 * maintained in ascending natural order. In those methods that
 * take a LinkedSet as a parameter, this order is used to increase
 * efficiency.
 *
 * @author Caroline Kirkconnell
 * @version 2020-10-19
 */
public class LinkedSet <T extends Comparable <? super T>> implements Set<T> {

   //////////////////////////////////////////////////////////
   // Do not change the following three fields in any way. //
   //////////////////////////////////////////////////////////

   /** References to the first and last node of the list. */
   Node front;
   Node rear;

   /** The number of nodes in the list. */
   int size;

   /////////////////////////////////////////////////////////
   // Do not change the following constructor in any way. //
   /////////////////////////////////////////////////////////

   /**
    * Instantiates an empty LinkedSet.
    */
   public LinkedSet() {
      front = null;
      rear = null;
      size = 0;
   }


   //////////////////////////////////////////////////
   // Public interface and class-specific methods. //
   //////////////////////////////////////////////////

   ///////////////////////////////////////
   // DO NOT CHANGE THE TOSTRING METHOD //
   ///////////////////////////////////////
   /**
    * Return a string representation of this LinkedSet.
    *
    * @return a string representation of this LinkedSet
    */
   @Override
   public String toString() {
      if (isEmpty()) {
         return "[]";
      }
      StringBuilder result = new StringBuilder();
      result.append("[");
      for (T element : this) {
         result.append(element + ", ");
      }
      result.delete(result.length() - 2, result.length());
      result.append("]");
      return result.toString();
   }


   ///////////////////////////////////
   // DO NOT CHANGE THE SIZE METHOD //
   ///////////////////////////////////
   /**
    * Returns the current size of this collection.
    *
    * @return  the number of elements in this collection.
    */
   public int size() {
      return size;
   }

   //////////////////////////////////////
   // DO NOT CHANGE THE ISEMPTY METHOD //
   //////////////////////////////////////
   /**
    * Tests to see if this collection is empty.
    *
    * @return  true if this collection contains no elements, false otherwise.
    */
   public boolean isEmpty() {
      return (size == 0);
   }


   /**
    * Ensures the collection contains the specified element. Neither duplicate
    * nor null values are allowed. This method ensures that the elements in the
    * linked list are maintained in ascending natural order.
    *
    * @param  element  The element whose presence is to be ensured.
    * @return true if collection is changed, false otherwise.
    */
   public boolean add(T element) {
      if (element == null || contains(element)) {
         return false;
      }
      
      Node n = new Node(element);
      Node f = front;
      
      if (isEmpty()) {
         front = n;
         rear = n;
         rear.next = null;
         size++;
         return true;
      }
      else if (front.element.compareTo(element) > 0) {
         n.next = front;
         front.prev = n;
         front = n;
         size++;
         return true;
      }
      else if (element.compareTo(rear.element) > 0) {
         rear.next = n;
         n.prev = rear;
         rear = n;
         rear.next = null;
         size++;
         return true;
      }
      else {
         Node previous = prevNode(element);
         previous.next.prev = n;
         n.next = previous.next;
         previous.next = n;
         n.prev = previous;
         size++;
         return true;
      }
   }
   
   private Node locate(T element) {
      Node n = front;
      while (n != null) {
         if (n.element.equals(element)) {
            return n;
         }
         n = n.next;
      }
      return null;
   }
   
   private Node prevNode(T element) {
      Node f = front;
      while (f != null) {
         if (f.element.compareTo(element) > 0) {
            return f.prev;
         }
         f = f.next;
      }
      return f;
   }
   
   private boolean constantAdd(T element) {
      if (element == null) {
         return false;
      }
      Node n = new Node(element);
      Node f = front;
      
      if (isEmpty()) {
         front = n;
         rear = n;
         rear.next = null;
         size++;
         return true;
      }
      else if (front.element.compareTo(element) > 0) {
         n.next = front;
         front.prev = n;
         front = n;
         size++;
         return true;
      }
      else if (front.element.compareTo(element) < 0) {
         rear.next = n;
         n.prev = rear;
         rear = n;
         rear.next = null;
         size++;
         return true;
      }
      return false;
   }

   /**
    * Ensures the collection does not contain the specified element.
    * If the specified element is present, this method removes it
    * from the collection. This method, consistent with add, ensures
    * that the elements in the linked lists are maintained in ascending
    * natural order.
    *
    * @param   element  The element to be removed.
    * @return  true if collection is changed, false otherwise.
    */
   public boolean remove(T element) {
      Node n = locate(element);
      if (element == null || isEmpty() || !contains(element)) {
         return false;
      }
      
      if (size == 1) {
         front = null;
         rear = null;
         size = 0;
         return true;
      }
      if (n == front) {
         front = front.next;
         front.prev = null;
      }
      else {
         if (n.next != null) {
            n.prev.next = n.next;
            n.next.prev = n.prev;
         }
         else {
            n.prev.next = null;
            rear = n.prev;
         }
      }
      size--;
      return true;
   }


   /**
    * Searches for specified element in this collection.
    *
    * @param   element  The element whose presence in this collection is to be tested.
    * @return  true if this collection contains the specified element, false otherwise.
    */
   public boolean contains(T element) {
      return locate(element) != null;
   }


   /**
    * Tests for equality between this set and the parameter set.
    * Returns true if this set contains exactly the same elements
    * as the parameter set, regardless of order.
    *
    * @return  true if this set contains exactly the same elements as
    *               the parameter set, false otherwise
    */
   public boolean equals(Set<T> s) {
      if (size == s.size() && complement(s).size() == 0) {
         return true;
      }   
      return false;
   }


   /**
    * Tests for equality between this set and the parameter set.
    * Returns true if this set contains exactly the same elements
    * as the parameter set, regardless of order.
    *
    * @return  true if this set contains exactly the same elements as
    *               the parameter set, false otherwise
    */
   public boolean equals(LinkedSet<T> s) {
      if (size == s.size() && complement(s).size() == 0) {
         return true;
      }
      return false;
   }


   /**
    * Returns a set that is the union of this set and the parameter set.
    *
    * @return  a set that contains all the elements of this set and the parameter set
    */
   public Set<T> union(Set<T> s) {
      LinkedSet<T> returnSet = new LinkedSet<T>();
      Node f = front;
      
      while (f != null) {  
         returnSet.constantAdd(f.element);
         f = f.next;
      }
      if (s == null) {
         return returnSet;
      }
      
      Iterator<T> i = s.iterator();
   
      while (i.hasNext()) {
         returnSet.add(i.next());
      }
      return returnSet;
   }


   /**
    * Returns a set that is the union of this set and the parameter set.
    *
    * @return  a set that contains all the elements of this set and the parameter set
    */
   public Set<T> union(LinkedSet<T> s) {
      LinkedSet<T> returnSet = new LinkedSet<T>();
      Node f = front;
      Node n = s.front;
      if (s.isEmpty()) {
         while (f != null) {
            returnSet.constantAdd(f.element);
            f = f.next;
         }
         return returnSet;
      }
      if (this.isEmpty()) {
         return s;
      }
      while (f != null && n != null) {
         if (f.element.compareTo(n.element) > 0) {
            returnSet.constantAdd(n.element);
            n = n.next;
         }
         else if (f.element.compareTo(n.element) < 0) {
            returnSet.constantAdd(f.element);
            f = f.next;
         }
         else if (f.element.compareTo(n.element) == 0) {
            returnSet.constantAdd(n.element);
            f = f.next;
            n = n.next;
         }
      }
      if (f == null) {
         while (n != null) {
            returnSet.constantAdd(n.element);
            n = n.next;
         }
      }
      else if (n == null) {
         while (f != null) {
            returnSet.constantAdd(f.element);
            f = f.next;
         }
      }
      return returnSet;
   }


   /**
    * Returns a set that is the intersection of this set and the parameter set.
    *
    * @return  a set that contains elements that are in both this set and the parameter set
    */
   public Set<T> intersection(Set<T> s) {
      LinkedSet<T> returnSet = new LinkedSet<T>();
      Node f = front;
      if (s == null) {
         return returnSet;
      }
      while (f != null) {   
         if (s.contains((T)f.element)) {
            returnSet.add((T)f.element);
         }
         f = f.next;
      }
      return returnSet;
   }

   /**
    * Returns a set that is the intersection of this set and
    * the parameter set.
    *
    * @return  a set that contains elements that are in both
    *            this set and the parameter set
    */
   public Set<T> intersection(LinkedSet<T> s) {
      LinkedSet<T> returnSet = new LinkedSet<T>();
      Node f = front;
      Node n = s.front;
      if (s.isEmpty()) {
         return returnSet;
      }
      if (this.isEmpty()) {
         return returnSet;
      }
      while (f != null && n != null) {   
         if (f.element.compareTo(n.element) > 0) {
            n = n.next;
         }
         else if (f.element.compareTo(n.element) < 0) {
            f = f.next;
         }
         else if (f.element.compareTo(n.element) == 0) {
            returnSet.constantAdd(n.element);
            f = f.next;
            n = n.next;
         }
      }
      return returnSet;
   }


   /**
    * Returns a set that is the complement of this set and the parameter set.
    *
    * @return  a set that contains elements that are in this set but not the parameter set
    */
   public Set<T> complement(Set<T> s) {
      LinkedSet<T> returnSet = new LinkedSet<T>();
      Node f = front;
      if (s.isEmpty()) {   
         return this;
      }
      if (this.isEmpty()) {
         return returnSet;
      }
      while (f != null) {
         if (!s.contains((T)f.element)) {
            returnSet.add((T)f.element);
         }
         f = f.next;
      }
      return returnSet;
   }


   /**
    * Returns a set that is the complement of this set and
    * the parameter set.
    *
    * @return  a set that contains elements that are in this
    *            set but not the parameter set
    */
   public Set<T> complement(LinkedSet<T> s) {
      LinkedSet<T> returnSet = new LinkedSet<T>();
      Node f = front;
      Node n = s.front;
      if (s.isEmpty()) {
         return this;
      }
      if (this.isEmpty()) {
         return returnSet;
      }
      
      while (f != null && n != null) {
         if (f.element.compareTo(n.element) > 0) {
            returnSet.constantAdd(n.element);
            n = n.next;
         }
         else if (f.element.compareTo(n.element) < 0) {
            returnSet.constantAdd(f.element);
            f = f.next;
         }
         else if (f.element.compareTo(n.element) == 0) {
            f = f.next;
            n = n.next;
         }
      }
      return returnSet;
   }


   /**
    * Returns an iterator over the elements in this LinkedSet.
    * Elements are returned in ascending natural order.
    *
    * @return  an iterator over the elements in this LinkedSet
    */
   public Iterator<T> iterator() {
      return new LinkedIte();
   }
   
   private class LinkedIte implements Iterator<T> {
      private Node current = front;
      
      public boolean hasNext() {
         return current != null;
      }
      
      public T next() {
         if (!hasNext()) {
            throw new NoSuchElementException();
         }
         T result = current.element;
         current = current.next;
         return result;
      }
      
      public void remove() {
         throw new UnsupportedOperationException();
      }
   }


   /**
    * Returns an iterator over the elements in this LinkedSet.
    * Elements are returned in descending natural order.
    *
    * @return  an iterator over the elements in this LinkedSet
    */
   public Iterator<T> descendingIterator() {
      return new DescendingLinkedIte(rear);
   }
   
   private class DescendingLinkedIte implements Iterator<T> {
      Node t;
      
      public DescendingLinkedIte(Node rear) {
         t = rear;
      }
      
      public boolean hasNext() {
         return t != null && t.element != null;
      }
      
      public T next() {
         if (t != null) {
            T r = t.element;
            t = t.prev;
            return r;
         }
         return null;
      }
      
      public void remove() {
         throw new UnsupportedOperationException();
      }
   }


   /**
    * Returns an iterator over the members of the power set
    * of this LinkedSet. No specific order can be assumed.
    *
    * @return  an iterator over members of the power set
    */
   public Iterator<Set<T>> powerSetIterator() {
      return new PowerIte(this.size);
   }
   
   private class PowerIte implements Iterator<Set<T>> {
      Node currentNode;
      int current = 0;
      int number;
      int numberOfSets;
      
      public PowerIte(int numberOfElements) {
         current = 0;
         currentNode = front;
         number = numberOfElements;
         numberOfSets = (int) Math.pow(2, number);
      }
      
      public boolean hasNext() {
         return current < numberOfSets;
      }
      
      public Set<T> next() {
         Set<T> newSet = new LinkedSet<T>();
         String binaryS = Integer.toBinaryString(current);
         while (binaryS.length() < number) {
            binaryS = "0" + binaryS;
            LinkedSet<T> current1 = new LinkedSet<T>();
         }
         for (int i = 0; i < number; i++) {
            if (binaryS.charAt(i) == '1') {
               T element = currentNode.element;
               newSet.add(element);
            }
            currentNode = currentNode.next;
         }
         currentNode = front;
         current++;
         return newSet;
      }
         
      public void remove() {
         throw new UnsupportedOperationException();
      }
   }
   //////////////////////////////
   // Private utility methods. //
   //////////////////////////////

   // Feel free to add as many private methods as you need.

   ////////////////////
   // Nested classes //
   ////////////////////

   //////////////////////////////////////////////
   // DO NOT CHANGE THE NODE CLASS IN ANY WAY. //
   //////////////////////////////////////////////

   /**
    * Defines a node class for a doubly-linked list.
    */
   class Node {
      /** the value stored in this node. */
      T element;
      /** a reference to the node after this node. */
      Node next;
      /** a reference to the node before this node. */
      Node prev;
   
      /**
       * Instantiate an empty node.
       */
      public Node() {
         element = null;
         next = null;
         prev = null;
      }
   
      /**
       * Instantiate a node that containts element
       * and with no node before or after it.
       */
      public Node(T e) {
         element = e;
         next = null;
         prev = null;
      }
   }

}