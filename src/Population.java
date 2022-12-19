import java.util.*;

public class Population implements Cloneable, Iterable<Individual> {
	private ArrayList<Individual> population = new ArrayList<>();
	private IFitness fitness;
	private Random generator;
	private double total= 0.0;

	static Comparator<Individual> comparator = new Comparator<Individual>() {
		@Override
		public int compare(Individual x, Individual y) {
			if(x.getFitness() > y.getFitness()) return -1;
			else if(x.getFitness() < y.getFitness()) return 1;
			return 0;
		}
	};

	Population(ArrayList<Individual> population,IFitness fitness,Random r){
		this.generator = r;
		this.population = population;
		this.fitness = fitness;
		Individual.fitnessFunction = fitness;
		for(var x:population) {
			total += x.getFitness();
		}
	}

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


	public double getMax(){
		double max = Double.MIN_NORMAL;
		for(var x: population)
			max = Math.max(max, x.getFitness());
		return max;
	}

	public double getMin(){
		double min= Double.MAX_VALUE;
		for(var x: population)
			min = Math.min(min, x.getFitness());
		return min;
	}

	public double getAvg(){
		return total/size();
	}

	public double getFitness(){
		return total;
	}

	public Individual binaryTournamentSelection(){
		int x = (int) Math.round(generator.nextDouble() * (population.size()-1));
		int y = (int) Math.round(generator.nextDouble() * (population.size()-1));
		return (population.get(x).getFitness() >= population.get(y).getFitness()) ? population.get(x) : population.get(y);
	}

	public ArrayList<Individual> elitistSelection(int n){
		PriorityQueue<Individual> heap = new PriorityQueue<Individual>(n, new Comparator<Individual>() {
			@Override
			public int compare(Individual x, Individual y) {
				if(x.getFitness() > y.getFitness()) return -1;
				else if(x.getFitness() < y.getFitness()) return 1;
				return 0;
			}
		});

		heap.addAll(population);

		ArrayList<Individual> elitists = new ArrayList<>();
		for(int i = 0; i < n;i++)
			elitists.add(heap.poll());

		ArrayList<Individual> result = new ArrayList<>();
		for(int i = 0; i < size()/n;i++){
			for(var individual : elitists){
				result.add(individual.clone());
			}
		}

		for(int i =0;i < size()%n;i++){
			result.add(elitists.get(i).clone());
		}

		return result;
	}


	private void permutation(ArrayList<Individual> a){
		int n = a.size()-1;
		for(int i = 0; i < n;i++) {
			int r = i + generator.nextInt(n - i);
			var temp = a.get(r);
			a.set(r,a.get(i));
			a.set(i,temp);
		}
	}

	public void permutation(){
		int n = population.size()-1;
		for(int i = 0; i < n;i++) {
			int r = i + generator.nextInt(n - i);
			var temp = population.get(r);
			population.set(r,population.get(i));
			population.set(i,temp);
		}
	}

	public ArrayList<Individual> rouletteWheelSelection(int n){
		var prefixSum = weightedBy();
		var result = new ArrayList<Individual>(n);
		for(int i = 0;i<n;i++)
			result.add(population.get(weightedRandomChoices(prefixSum)));
		return result;
	}

	private int weightedRandomChoices(double[] prefixSum) {
		var u = generator.nextDouble();
		return lowerBound(u,prefixSum);
	}


	private double[] weightedBy() {
		int n = size();
		double[] prefixSum = new double[n];
		var it = iterator();
		prefixSum[0] = it.next().getFitness()/total;
		for(int i = 1; i < n;i++)
			prefixSum[i] = prefixSum[i-1] + (it.next().getFitness()/total);
		prefixSum[n-1] = 1;

		return prefixSum;
	}

	private static int lowerBound(double key,double[] a){
		int lo = 0; int hi = a.length-1;
		int mid;

		while(lo < hi){
			mid = lo + (hi-lo)/2;
			if(key <= a[mid])
				hi = mid;
			else
				lo = mid+1;
		}

		if (lo < a.length && a[lo] < key) //a lower bound to the key doesn't exist
			return -1;

		return lo;
	}

	public void sort(){
		population.sort(comparator);
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

