import java.util.Random;

public class Individual implements Cloneable{
	public static IFitness fitnessFunction;
	private String representation;
	private double fitnessValue;
	public static int range; //the range that a chromosome from the dna can take from [0,range[;

	Individual(String representation){
		if(fitnessFunction == null) throw new IllegalStateException("The fitnessFunction must be initialized before hand");
		this.representation = representation;
		fitnessValue = fitnessFunction.getFitness(this);
	}

	public double getFitness() {return fitnessValue;}

	public Individual[] onePointCrossover(Individual partner,Random generator){
		int l = representation.length();
		System.out.println(l);
		String parent1 = representation, parent2 = partner.representation;
		int c = 1 +  generator.nextInt(l-2); //points were the "dna" is split

		var children = new Individual[2];
		children[0] = new Individual(parent1.substring(0,c) + parent2.substring(c,l));
		children[1] = new Individual(parent2.substring(0,c) + parent1.substring(c,l));
		return children;
	}

	public Individual[] uniformCrossover(Individual partner,Random generator) {
		int l = representation.length();
		var children = new Individual[2];
		var child1 = new StringBuilder();
		var child2 = new StringBuilder();
		var parent1 = representation;
		var parent2 = partner.representation;

		for(int i = 0; i < l;i++){
			if(generator.nextDouble() < 0.5){
				child1.append(parent2.charAt(i));
				child2.append(parent1.charAt(i));
			}
			else{
				child1.append(parent1.charAt(i));
				child2.append(parent2.charAt(i));
			}
		}
		children[0] = new Individual(child1.toString());
		children[1] = new Individual(child2.toString());
		return children;
	}

	public void numberMutation(double p,Random generator){
		var s = new StringBuilder();
		for(int i = 0; i < representation.length();i++)
			if(generator.nextDouble() < p) {
				s.append(generator.nextInt(range));
			} else
				s.append(representation.charAt(i));
		representation = s.toString();
		fitnessValue = fitnessFunction.getFitness(this);
	}

	@Override
	public String toString() {
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
}