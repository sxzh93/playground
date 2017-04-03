package prince;

import java.lang.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item>{
	private Node first, last;
	private int size;
	
	public Deque(){
		// construct an empty deque
		this.first = this.last = null;
		this.size = 0;
	}
	
	public boolean isEmpty(){	
		// is the deque empty?
		return this.first == null;
	}
	
	public int size(){
		// return the number of items on the deque
		return this.size;
	}
	
	public void addFirst(Item item){
		// add the item to the front
		if(item == null){
			throw new NullPointerException();
		}
		Node oldFirst = this.first;
		this.first = new Node();
		this.first.val = item;
		if(oldFirst == null){
			this.last = this.first;
		}else{
			oldFirst.pre = this.first;
		}
		this.first.next = oldFirst;
		++this.size;
	}
	
	public void addLast(Item item){
		// add the item to the end
		if(item == null){
			throw new NullPointerException();
		}
		Node oldLast = this.last;
		this.last = new Node();
		this.last.val = item;
		if(oldLast == null){
			this.first = this.last;
		}else{
			oldLast.next = this.last;
		}
		last.pre = oldLast;
		++this.size;
	}
	
	public Item removeFirst(){
		// remove and return the item from the front
		if(this.isEmpty()){
			throw new NoSuchElementException();
		}
		Item ret = this.first.val;
		this.first = this.first.next;
		if(this.first == null){
			this.last = null;
		}else{
			this.first.pre = null;
		}
		--this.size;
		return ret;
	}
	
	public Item removeLast(){
		// remove and return the item from the end
		if(this.isEmpty()){
			throw new NoSuchElementException();
		}
		Node oldLast = last;
		this.last = this.last.pre;
		if(this.last == null){
			this.first = null;
		}else{
			this.last.next = null;
		}
		--this.size;
		return oldLast.val;
	}
	
	public Iterator<Item> iterator(){
		// return an iterator over items in order from front to end
		return new DequeIterator();
	}
	
	private class Node{
		Item val;
		Node next;
		Node pre;
	}
	
	private class DequeIterator implements Iterator<Item> {
		private Node current = first;
		
		public boolean hasNext(){
			return current != null;
		}
		
		public Item next(){
			if(current == null){
				throw new NoSuchElementException();
			}
			Item ret = current.val;
			current = current.next;
			return ret;
		}
	}
	
	public static void main(String[] args){
		// unit testing (optional)
		Deque<String> dq = new Deque<String>();
		dq.addFirst("Hello");
		dq.addLast("world");
		dq.addLast("How");
		for (String str : dq) {
			System.out.println(str);
		}
		System.out.println(dq.removeFirst());
		System.out.println(dq.removeLast());
		System.out.println(dq.removeLast());
	}
}
