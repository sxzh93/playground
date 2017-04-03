package prince;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private int size;
	private int capacity;
	private Item[] s;
	
	public RandomizedQueue(){
		// construct an empty randomized queue
		this.size = 0;
		this.capacity = 2;
		s = (Item[]) new Object[this.capacity];
	}
	
	public boolean isEmpty(){
		// is the queue empty?
		return this.size == 0;
	}
	
	public int size(){
		// return the number of items on the queue
		return this.size;
	}
	
	private void resize(int capacity){
		// resize an array to capacity
		assert capacity >= this.size;
		Item[] temp = (Item[]) new Object[capacity];
		for(int i=0;i<this.size;++i){
			temp[i] = this.s[i];
		}
		this.s = temp;
		this.capacity = capacity;
	}
	
	public void enqueue(Item item){
		// add the item
		if(item == null){
			throw new NullPointerException();
		}
		if(this.size == this.capacity){
			this.resize(2*this.capacity);
		}
		s[this.size++] = item;
	}
	
	public Item dequeue(){
		// remove and return a random item
		if(this.isEmpty()){
			throw new NoSuchElementException();
		}
		
		int didx = StdRandom.uniform(this.size);
		Item del = this.s[didx];
		this.s[didx] = this.s[this.size-1];
		this.s[this.size-1] = del;
		
		s[--this.size] = null;
		
		if(this.size>0 && this.size<=this.capacity/4){
			this.resize(this.capacity/2);
		}
		
		return del;
	}
	
	public Item sample(){
		// return (but do not remove) a random item
		if(this.isEmpty()){
			throw new NoSuchElementException();
		}
		
		int sidx = StdRandom.uniform(this.size);
		return this.s[sidx];
	}
	
	private class RandomizedQueueIterator implements Iterator<Item> {
		private int idx = 0;
		private Item[] seq;
		
		public RandomizedQueueIterator() {
			this.seq = (Item[]) new Object[size];
			for(int i=0;i<size;++i){
				this.seq[i] = s[i];
			}
			StdRandom.shuffle(this.seq);
		}
		
		public boolean hasNext(){
			return idx != this.seq.length;
		}
		
		public Item next(){
			if(idx == size){
				throw new NoSuchElementException();
			}
			return this.seq[idx++];
		}
	}
	
	public Iterator<Item> iterator(){
		// return an independent iterator over items in random order
		return new RandomizedQueueIterator();
	}
	
	public static void main(String[] args){
		// unit testing (optional)
		RandomizedQueue<String> rq = new RandomizedQueue<String>();
		rq.enqueue("hello");
		rq.enqueue("world");
		rq.enqueue("how");
		rq.enqueue("are");
		rq.enqueue("you");
		for (String str : rq) {
			System.out.println(str);
		}
		
		for (int i=0;i<6;++i){
			System.out.println(rq.dequeue());
		}
	}
}
