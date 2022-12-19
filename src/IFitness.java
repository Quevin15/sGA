public interface IFitness {
	/**
	 * Calculates the fitness of a given individual
	 * @param x The individual
	 * @return the individual's fitness
	 */
	public double getFitness(Individual x);
}