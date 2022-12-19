import java.util.Arrays;

public class NQueens implements IFitness{
	int[] diagonal;
	int[] invDiagonal;
	int[] columns;
	int[] board;
	int size;

	private int getConflicts(int variable){
		int result = 0;
		if(columns[board[variable]] > 1) ++result;
		if(diagonal[variable+board[variable]] > 1) ++result;
		if(invDiagonal[variable+ size -1-board[variable]] > 1) ++result;

		return result;
	}
	private int getDifConflicts(int variable){
		int result = 0;
		if(columns[board[variable]] > 1) result+= columns[board[variable]] -1;
		if(diagonal[variable+board[variable]] > 1)result += diagonal[variable+board[variable]] -1;
		if(invDiagonal[variable+ size -1-board[variable]] > 1) result += invDiagonal[variable + size -1 -board[variable]]-1;

		return result;

	}
	@Override
	public double getFitness(Individual x) {
		board = x.toArray();
		size = board.length;

		diagonal = new int[size *2-1];
		for(int i = 0; i < size; i++)
			++diagonal[i+board[i]];

		invDiagonal = new int[size *2-1];
		for(int i = 0; i < size; i++)
			++invDiagonal[i+ size -1-board[i]];

		columns = new int[size];
		for(int i = 0; i < size; i++){
			++columns[board[i]];
		}

		double fitness = 0.0;
		for(int i =0; i < size;++i){
			fitness -= getDifConflicts(i);
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