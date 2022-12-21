import java.util.*;

/**
 * The population of individuals.
 */
public class Population implements Cloneable, Iterable<Individual> {
	/**
	 * The population
	 */
	private ArrayList<Individual> population = new ArrayList<>();
	/**
	 * The fitness function that will be used
	 */
	private IFitness fitness;
	/**
	 * The random generator that will be used
	 */
	private Random generator;
	/**
	 * The total fitness of the population
	 */
	private double total= 0.0;

	/**
	 * The comparator of individuals
	 */

	static Comparator<Individual> comparator = new Comparator<Individual>() {
		@Override
		public int compare(Individual x, Individual y) {
			if(x.getFitness() > y.getFitness()) return -1;
			else if(x.getFitness() < y.getFitness()) return 1;
			return 0;
		}
	};

	/**
	 * Constucts  population
	 */
	Population(){}

	/**
	 * Constucts a population with a list of individuals, a fitness function, and a random generator
	 * @param population the population
	 * @param fitness the fitness function
	 * @param r the random generator
	 */
	Population(ArrayList<Individual> population,IFitness fitness,Random r){
		this.generator = r;
		this.population = population;
		this.fitness = fitness;
		Individual.fitnessFunction = fitness;
		for(var x:population) {
			total += x.getFitness();
		}
	}

	/**
	 * Constructs a population with 'n' individuals of size 'l' with a fitness function, a chromossome range and a random
	 * generator.
	 * @param n the population size
	 * @param l the individuals length
	 * @param fitness the fitness function
	 * @param range the chromosome range
	 * @param generator the random generator
	 */

	Population(int n, int l, IFitness fitness,int range,Random generator){
		this.generator = generator;
		Individual.range = range;
		Individual.fitnessFunction = fitness;
		for(int i = 0; i < n;i++){
			var a = new int[l];
			for(int j = 0;j< l;j++){
				a[j] = generator.nextInt(range);
			}
			var ind = new Individual(a);
			total += ind.getFitness();
			population.add(ind);
		}
	}

	//range == l

	/**
	 * Generates a population in witch no queens will be in the same column
	 * @param n the population sie
	 * @param l the individuals length
	 * @param fitness the fitness function
	 * @param range the chromosome range
	 * @param generator the random generator
	 * @return a new population
	 */
	public static Population differentPopulation(int n, int l, IFitness fitness,int range,Random generator){
		if(l != range) throw new IllegalArgumentException("The length of the dna and the range of it's chromosomes must match");
		Population result= new Population();
		result.generator = generator;
		Individual.range = range;
		Individual.fitnessFunction = fitness;
		for(int i = 0; i < n;i++){
			var a = new int[l];
			for(int j = 0;j< l;j++){
				a[j] = j;
			}
			shuffle(a,generator);
			var ind = new Individual(a);
			result.total += ind.getFitness();
			result.population.add(ind);
		}
		return result;
	}

	/**
	 * Shuffles an array
	 * @param a the array
	 * @param generator the random generator that will be used
	 */
	private static void shuffle(int[] a,Random generator){
		for(int i = a.length -1 ; i > 0; i--) {
			int j = generator.nextInt(i + 1);
			exchangeOnArray(a, i, j);
		}
	}

	/**
	 * Exchanges elements in an array
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

	/**
	 * Tournament Selection without Replacement
	 * Selects the bests individuals by performing 's' tournaments amongst population.size()/s individuals.
	 * @param s the number of tournaments
	 * @return the lists with the best individuals
	 */
	public ArrayList<Individual> tournamentSelNoRep(int s){
		int n = population.size()/s;
		var winners = new ArrayList<Individual>(size());
		ArrayList<Individual> a = new ArrayList<>(population);
		for(int k = 0 ; k < s;k++) {
			permutation(a);
			var it = a.iterator();
			for(int i = 0; i < n; i++) {
				double mostFitValue = -Double.MAX_VALUE;
				Individual mostFitInd = null;
				int m = i * s + s;
				for(int j = i * s; j < m; j++) {
					var ind = it.next();
					if(ind.getFitness() > mostFitValue) {
						mostFitInd = ind;
						mostFitValue = ind.getFitness();
					}
				}
				winners.add(mostFitInd.clone());
			}
		}
		return winners;
	}

	/**
	 * Gets the biggest fitness amongst the population
	 * @return the biggest fitness
	 */
	public double getMax(){
		double max = -Double.MAX_VALUE;
		for(var x: population)
			max = Math.max(max, x.getFitness());
		return max;
	}

	/**
	 * Gets the smallest fitness amongst the population
	 * @return the smallest fitness
	 */
	public double getMin(){
		double min= Double.MAX_VALUE;
		for(var x: population)
			min = Math.min(min, x.getFitness());
		return min;
	}

	/**
	 * Gets the average fitness of the population
	 * @return the avarage fitness of the population
	 */
	public double getAvg(){
		return total/size();
	}

	public double getFitness(){
		return total;
	}

	/**
	 * Permutes a list of individuals
	 * @param a a list of individuals
	 */
	private void permutation(ArrayList<Individual> a){
		int n = a.size()-1;
		for(int i = 0; i < n;i++) {
			int r = i + generator.nextInt(n - i);
			var temp = a.get(r);
			a.set(r,a.get(i));
			a.set(i,temp);
		}
	}

	public ArrayList<Individual> toArray(){
		return population;
	}



	@Override
	public Population clone(){
		try {
			Population clone = (Population) super.clone();
			clone.total = total;
			clone.population = new ArrayList<>();
			for(var x:population){
				clone.population.add(x.clone());
			}
			return clone;
		} catch(CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the population size
	 * @return the population size
	 */
	public int size(){ return population.size();}

	@Override
	public String toString() {
		return population.toString();
	}

	@Override
	public Iterator<Individual> iterator(){
		return population.iterator();
	}
}

