import java.util.Arrays;

public class NQueens implements IFitness{
	int[] diagonal;
	int[] invDiagonal;
	int[] board;
	int size;

	private int getConflicts(int variable){
		if(diagonal[variable+board[variable]] > 1) return 1;
		if(invDiagonal[variable+ size -1-board[variable]] > 1) return 1;
		return 0;
	}
	private int getDifConflicts(int variable){
		int result = 0;
		if(diagonal[variable+board[variable]] > 1)result += diagonal[variable+board[variable]] -1;
		if(invDiagonal[variable+ size -1-board[variable]] > 1) result += invDiagonal[variable + size -1 -board[variable]]-1;

		return result;

	}
	@Override
	public double getFitness(Individual x) {
		board = x.toArray();
		size = board.length;

		invDiagonal = new int[size *2-1];
		diagonal = new int[size *2-1];
		for(int i = 0; i < size; i++) {
			++diagonal[i + board[i]];
			++invDiagonal[i + size - 1 - board[i]];
		}

		double fitness = 0.0;
		for(int i =0; i < size;++i){
			fitness -= getConflicts(i);
		}

/*
		System.out.println(Arrays.toString(diagonal));
		System.out.println(Arrays.toString(invDiagonal));
		System.out.println(Arrays.toString(columns));
		System.out.println(fitness);
*/

		return fitness;
	}
}