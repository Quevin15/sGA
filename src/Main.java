import java.util.*;

public class Main {
	public static void main(String[] args) {
		var sGA = new SimpleGeneticAlgorithm();
		var watch = new Stopwatch();
		for(int i =5000; i < 10000;i+= 500) {
			int size = i;
			var result = sGA.orderBasedSwapMutationGA(100, size, size, 0.0, 0.80, new NQueens(), 0.0);
			System.out.println(i+","+watch.elapsedTime()+","+sGA.k);
			sGA.k=0;
		}
	}
}