import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.*;

/**
 * Genetic algorithm, that supports row and columns based problems with an invariant for all the columns and rows
 * being different
 */
public class SimpleGeneticAlgorithm {
	/**
	 * The random generator that will be used
	 */
	static Random generator;
	static int k;

	/**
	 * Constructs a SimpleGeneticAlgorithm
	 */
	SimpleGeneticAlgorithm() {
		SimpleGeneticAlgorithm.generator = new Random();
	}

	SimpleGeneticAlgorithm(Random generator){
		SimpleGeneticAlgorithm.generator = generator;
	}

	/**
	 * Resolves the task with an order based GA
	 * @param n The population size
	 * @param l The individual length
	 * @param range the range of the chromosomes
	 * @param pCrossover the crossover chance
	 * @param pMutation the mutation chance
	 * @param fitness the fitness function
	 * @param isFit the fitness goal
	 * @return A string representing the task solution
	 */
	public String orderBasedSwapMutationGA(int n, int l,int range,double pCrossover, double pMutation , IFitness fitness,double isFit){
		var population = Population.differentPopulation(n,l, fitness,range,generator);

		Individual mostFit;
		do{
			var population2 = population.tournamentSelNoRep(n/5);

			//crossover with probability pCrossover
			for(int i = 1 ; i < n;i+=2){
				if(generator.nextDouble() >= pCrossover) continue;
				//var children = population2.get(i-1).Crossover(population2.get(i), generator);
				var children = population2.get(i-1).cxCrossover(population2.get(i));
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

			//	 System.out.println(String.format("%d: %.2f %.2f %.2f",k,population.getMax(),population.getAvg(),population.getMin()));
			++k;

	//		System.out.println(String.format("%d: %.2f %.2f %.2f",k,population.getMax(),population.getAvg(),population.getMin()));
			population = new Population(population2,fitness,generator);
		}while(mostFit.getFitness() < isFit);
		return mostFit.toString();
	}
}
//	result[k]= String.format("%d: %.2f %.2f %.2f",k,population.getMax(),population.getAvg(),population.getMin());
