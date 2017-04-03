package prince;

import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

import prince.RandomizedQueue;

public class Permutation {
	public static void main(String[] args){
		int k = Integer.parseInt(args[0]);
		
		RandomizedQueue<String> rQueue = new RandomizedQueue<String>();
		try{
			int cnt = 0;
			while(true){
				// use reservoir sampling
				String str = StdIn.readString();
				++cnt;
				if(rQueue.size()<k){
					rQueue.enqueue(str);
				}else{
					int idx = StdRandom.uniform(cnt);
					if(idx<k){
						rQueue.dequeue();
						rQueue.enqueue(str);
					}
				}
			}
		}catch (NoSuchElementException e) {
			// TODO: handle exception
		}
		for (String s : rQueue) {
			System.out.println(s);
		}
	}
}
