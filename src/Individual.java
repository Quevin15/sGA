import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Individual implements Cloneable{
	public static IFitness fitnessFunction;
	private int[] representation;
	private double fitnessValue;
	public static int range; //the range that a chromosome from the dna can take from [0,range[;

	Individual(int[] representation){
		if(fitnessFunction == null) throw new IllegalStateException("The fitnessFunction must be initialized before hand");
		this.representation = representation;
		fitnessValue = fitnessFunction.getFitness(this);
	}

	public double getFitness() {return fitnessValue;}

	public Individual[] Crossover(Individual partner, Random generator){
		int l = representation.length;
		int[] parent1 = representation, parent2 = partner.representation;
		int c = l/2 +  generator.nextInt(l/2); //points were the "dna" is split
		int c1 = c - l/2;
		HashSet<Integer> set = new HashSet<>(l/2);
		for(int i = c1; i<c; i++) set.add(parent1[i]);
		var children = new Individual[2];
		int[] aux = new int[l];
		for(int i = c1; i<c; i++){
			aux[i] = parent1[i];
		}
		int r = 0;
		for(int i = 0; i<l; i++){
			if(set.contains(parent2[i])){
				continue;
			}
			if(r == c1) r+=l/2 - 1;
			aux[r++] = parent2[i];
		}
		children[0] = new Individual(aux);

		set = new HashSet<>(l);
		for(int i = c1; i<c; i++) set.add(parent2[i]);
		//stores the children array
		aux = new int[l];
		for(int i = c1; i<c; i++){
			aux[i] = parent2[i];
		}
		 r = 0;
		for(int i = 0; i<l; i++){
			if(set.contains(parent1[i])){
				continue;
			}
			if(r == c1) r+=l/2;
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

	public void swapMutation(double p,Random generator) {
		if(generator.nextDouble() >= p) return;
		exchangeOnArray(representation,generator.nextInt(range),generator.nextInt(range-1));
		this.fitnessValue = fitnessFunction.getFitness(this);
	}

	private static void exchangeOnArray(int[] a, int i, int j)
	{
		int t = a[i];
		a[i] = a[j];
		a[j] = t;
	}
}