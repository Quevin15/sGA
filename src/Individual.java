import java.util.*;

/**
 * An individual of a population
 */
public class Individual implements Cloneable{
	public static IFitness fitnessFunction;
	/**
	 * The array representation of the individual
	 */
	private int[] representation;
	/**
	 * The fitness of the individual
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
	 * @param generator random generator that will be used to perform the crossover
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
		if(c1 - c >= 0) System.arraycopy(parent1, c, aux, c, c1 - c);
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
		if(c1 - c >= 0) System.arraycopy(parent2, c, aux, c, c1 - c);

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

	public Individual[] cxCrossover(Individual Partner){
		int[] parent1 = representation, parent2 = Partner.representation;
		int[] mapParent2 = new int[representation.length];
		boolean[] indexesNavigated = new boolean[representation.length];
		Individual[] children = new Individual[2];
		var children1 = new int[parent1.length];
		var children2 = new int[parent1.length];

		for(int i = 0;i < parent1.length;i++)
			mapParent2[parent2[i]] = i;

		int i = 0;

		var flag = true;
		while(i < representation.length){
			if(indexesNavigated[i]){
				++i;
				continue;
			}

			int currentIndex = i;

			int[] parentOne;
			int[] parentTwo;
			if(flag) {
				parentOne = parent1;
				parentTwo = parent2;
			}
			else{
				parentOne = parent2;
				parentTwo = parent1;
			}

			do{
				int cycleValue = parent1[currentIndex];
				currentIndex = mapParent2[cycleValue];
				children1[currentIndex] = parentOne[currentIndex];
				children2[currentIndex] = parentTwo[currentIndex];
				indexesNavigated[currentIndex] = true;
			} while(i != currentIndex);

			flag = !flag;
		}

		children[0] = new Individual(children1);
		children[1] = new Individual(children2);

		return children;
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
			clone.representation = representation.clone();
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
		if(r < p) {
			exchangeOnArray(representation, generator.nextInt(range), generator.nextInt(range - 1));
			this.fitnessValue = fitnessFunction.getFitness(this);
		}
	}

	/**
	 * Exchange elements in an array
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