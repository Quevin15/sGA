import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.*;

public class SimpleGeneticAlgorithm {
	static Random generator;

	SimpleGeneticAlgorithm() {
		SimpleGeneticAlgorithm.generator = new Random();
	}

	SimpleGeneticAlgorithm(Random generator){
		SimpleGeneticAlgorithm.generator = generator;
	}

	public String orderBasedGA(int n, int l,int range,double pCrossover, double pMutation, IFitness fitness,double isFit){
	    var population = new Population(n,l, fitness,range,generator);

		 int k = 0;
		 Individual mostFit;
		 do{
			var population2 = population.improvedElitistSelection(n/20);

			 //crossover with probability pCrossover
			for(int i = 1 ; i < n;i+=2){
				if(generator.nextDouble() >= pCrossover) continue;
				var children = population2.get(i-1).onePointCrossover(population2.get(i), generator);
				population2.set(i - 1, children[0]);
				population2.set(i, children[1]);
			}

			for(int i = 0; i < n;i++){
				population2.get(i).numberMutation(pMutation,generator);
			}

			mostFit = population2.get(0);
			//getFitness must be o(1) otherwise this will be slow, of course the individual should that memoized.
			for(var x: population2){
				mostFit = (mostFit.getFitness() < x.getFitness()) ? x : mostFit;
			}

	//		 System.out.println(String.format("%d: %.2f %.2f %.2f",k,population.getMax(),population.getAvg(),population.getMin()));

			 System.out.println(String.format("%d: %.2f %.2f %.2f",k,population.getMax(),population.getAvg(),population.getMin()));
			 ++k;

			population = new Population(population2,fitness,generator);
		 }while(mostFit.getFitness() < isFit);
		 return mostFit.toString();
	}

	public String orderBasedSwapMutationGA(int n, int l,int range,double pCrossover, double pMutation , IFitness fitness,double isFit){
		var population = Population.differentPopulation(n,l, fitness,range,generator);

		int k = 0;
		Individual mostFit;
		do{
			var population2 = population.tournamentSelNoRep(20);

			//crossover with probability pCrossover
			for(int i = 1 ; i < n;i+=2){
				if(generator.nextDouble() >= pCrossover) continue;
				var children = population2.get(i-1).Crossover(population2.get(i), generator);
				population2.set(i - 1, children[0]);
				population2.set(i, children[1]);
			}

			for(int i = 0; i < n;i++){
				population2.get(i).swapMutation(pMutation,generator);
			}

			mostFit = population2.get(0);
			//getFitness must be o(1) otherwise this will be slow, of course the individual should that memoized.
			for(var x: population2){
				mostFit = (mostFit.getFitness() < x.getFitness()) ? x : mostFit;
			}

			//		 System.out.println(String.format("%d: %.2f %.2f %.2f",k,population.getMax(),population.getAvg(),population.getMin()));
			++k;

			System.out.println(String.format("%d: %.2f %.2f %.2f",k,population.getMax(),population.getAvg(),population.getMin()));
			population = new Population(population2,fitness,generator);
		}while(mostFit.getFitness() < isFit);
		return mostFit.toString();
	}
}
//	result[k]= String.format("%d: %.2f %.2f %.2f",k,population.getMax(),population.getAvg(),population.getMin());
