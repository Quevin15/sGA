import java.util.*;

public class Main {
	public static void main(String[] args) {
		var sGA = new SimpleGeneticAlgorithm();
		var watch = new Stopwatch();
		int size = 2000;
		var result = sGA.orderBasedSwapMutationGA(100, size, size, 0.95, 0.80,new NQueens(), 0.0);
		//System.out.println(result);
		System.out.println(watch.elapsedTime());

//		var result = sGA.oneMaxNGenerations(4,5,2,0.7,0.1,new OneMax(),2);
	}
}