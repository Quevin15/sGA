import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Individual implements Cloneable{
	/**
	 * The fitness function that will calculate the fitness of the individual
	 */
	public static IFitness fitnessFunction;
	/**
	 * The array representation of the individual
	 */
	private int[] representation;
	/**
	 * The fitness of the indidual
	 */
	private double fitnessValue;
	/**
	 * The range that a chromosome from the dna can take from [0,range[
	 */
	public static int range;

	/**
	 * Constructs an individual based in an array representation of an individual.
	 * @param representation the representation of an individual.
	 */

	Individual(int[] representation){
		if(fitnessFunction == null) throw new IllegalStateException("The fitnessFunction must be initialized before hand");
		this.representation = representation;
		fitnessValue = fitnessFunction.getFitness(this);
	}

	/**
	 * Get the individual's fitness
	 * @return the individual's fitness
	 */
	public double getFitness() {return fitnessValue;}

	/**
	 * Performs a crossover of individuals
	 * @param partner partner for the crossover
	 * @param generator random generator that will be used to peform the crossover
	 * @return two new offsprings
	 */
	public Individual[] Crossover(Individual partner, Random generator){
		int l = representation.length;
		int[] parent1 = representation, parent2 = partner.representation;
		int c =  generator.nextInt(l); //points were the "dna" is split
		int c1 = c + generator.nextInt(l-c);
		HashSet<Integer> set = new HashSet<>(l);
		for(int i = c; i<c1; i++) set.add(parent1[i]);
		var children = new Individual[2];
		int[] aux = new int[l];
		for(int i = c; i<c1; i++){
			aux[i] = parent1[i];
		}
		int r = 0;
		for(int i = 0; i<l; i++){
			if(set.contains(parent2[i])){
				continue;
			}
			if(r == c) r=c1;
			aux[r++] = parent2[i];
		}
		children[0] = new Individual(aux);

		set = new HashSet<>(l);
		for(int i = c; i<c1; i++) set.add(parent2[i]);
		//stores the children array
		aux = new int[l];
		for(int i = c; i<c1; i++){
			aux[i] = parent2[i];
		}
		 r = 0;
		for(int i = 0; i<l; i++){
			if(set.contains(parent1[i])){
				continue;
			}
			if(r == c) r=c1;
			aux[r++] = parent1[i];
		}
		children[1] = new Individual(aux);

		return children;
	}
	
	public Individual[] onePointCrossover(Individual partner,Random generator){
		int l = representation.length;

		int[] parent1 = representation, parent2 = partner.representation;
		int c = 1 +  generator.nextInt(l-2); //points were the "dna" is split
		var children = new Individual[2];

		//stores the children array
		int[] childrenArray1 = new int[l];

		System.arraycopy(parent1, 0, childrenArray1, 0, c);
		System.arraycopy(parent2,c,childrenArray1,c,l-c);
		children[0] = new Individual(childrenArray1);


		int[] childrenArray2 = new int[l];
		System.arraycopy(parent1, 0, childrenArray2, 0, c);
		System.arraycopy(parent2,c,childrenArray2,c,l-c);
		children[1] = new Individual(childrenArray2);

		return children;
	}

	public Individual[] uniformCrossover(Individual partner,Random generator) {
		int l = representation.length;
		var children = new Individual[2];
		var child1 = new int[l];
		var child2 = new int[l];
		var parent1 = representation;
		var parent2 = partner.representation;

		for(int i = 0; i < l;i++){
			if(generator.nextDouble() < 0.5){
				child1[i] = parent2[i];
				child2[i] = parent1[i];
			}
			else{
				child1[i] = parent1[i];
				child2[i] = parent2[i];
			}
		}
		children[0] = new Individual(child1);
		children[1] = new Individual(child2);
		return children;
	}

	public void numberMutation(double p,Random generator) {
		for(int i = 0; i < representation.length; i++)
			if(generator.nextDouble() < p)
				representation[i] = generator.nextInt(range);
		fitnessValue = fitnessFunction.getFitness(this);
	}

	@Override
	public String toString() {
		return Arrays.toString(representation);
	}

	/**
	 * Array representation of the individual
	 * @return the Array representation of the individual
	 */
	int[] toArray(){
		return representation;
	}

	@Override
	public Individual clone() {
		try {
			Individual clone = (Individual) super.clone();
			clone.representation = representation;
			clone.fitnessValue = fitnessValue;
			return clone;
		} catch(CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

	/**
	 * Performs a mutation by exchanging individuals indexes.
	 * @param p the mutation chance
	 * @param generator the random generator that will be used for the mutation
	 */
	public void swapMutation(double p,Random generator) {
		double r = generator.nextDouble();
		if(r < p/2) {
			exchangeOnArray(representation, generator.nextInt(range), generator.nextInt(range - 1));
			exchangeOnArray(representation, generator.nextInt(range), generator.nextInt(range - 1));
			this.fitnessValue = fitnessFunction.getFitness(this);
			return;
		}
		if(r < p) {
			exchangeOnArray(representation, generator.nextInt(range), generator.nextInt(range - 1));
			this.fitnessValue = fitnessFunction.getFitness(this);
		}
	}

	/**
	 * Exchange elements in a array
	 * @param a the array
	 * @param i an index
	 * @param j an index
	 */
	private static void exchangeOnArray(int[] a, int i, int j)
	{
		int t = a[i];
		a[i] = a[j];
		a[j] = t;
	}
}