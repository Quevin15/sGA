import java.util.*;

public class Main {
	public static void main(String[] args) {
		var sGA = new SimpleGeneticAlgorithm();
		/*
		var in = new Scanner(System.in);
		int n = in.nextInt();
		int l = in.nextInt();
		int s = in.nextInt();
		double pM = in.nextDouble();
		double pC = in.nextDouble();
		var result = sGA.orderBasedGA(n,l,s,pC,pM,new OneMax(),nSteps);
*/
		var watch = new Stopwatch();
		int size = 100;
		var result = sGA.orderBasedSwapMutationGA(100, size, size, 0.8, 0.3,new NQueens(), 0.0);
		System.out.println(watch.elapsedTime());
		System.out.println(result);
//		var result = sGA.oneMaxNGenerations(4,5,2,0.7,0.1,new OneMax(),2);
	}
}