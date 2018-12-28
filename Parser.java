import java.util.ArrayList;
import java.util.Stack;

public class Parser 
{
	public static int[][] parse(int[][] solvedBoard) 
	{
		ArrayList<int[]> list = new ArrayList<int[]>();
		int[][] newBoard = new int[9][9];
		
		for(int x = 0; x < 9; x++)
		{
			for(int y = 0; y < 9; y++)
			{
				newBoard[x][y] = solvedBoard[x][y];
				list.add(new int[]{x, y});
			}
		}

		Shuffler.shuffle(list);
		
		for(int i = 0; i < list.size(); i++)
		{
			int x = list.get(i)[0];
			int y = list.get(i)[1];
			newBoard[x][y] = 0;
			
			if(!unique(newBoard))
				newBoard[x][y] = solvedBoard[x][y];
		}
		
		return newBoard;
	}
	
	private static boolean unique(int[][] oldBoard) 
	{
		boolean solutionFound = false;
		
		int[][] board = new int[9][9];
		ArrayList<int[]> list = new ArrayList<int[]>();

		for(int x = 0; x < 9; x++)
		{
			for(int y = 0; y < 9; y++)
			{
				board[x][y] = oldBoard[x][y];
				
				if(board[x][y] == 0)
				{
					list.add(new int[]{x,y});
				}
			}
		}

		Stack<Integer>[] tries = new Stack[list.size()];
		
		for(int i = 0; i < list.size(); i++)
		{
			Stack<Integer> nums = new Stack<Integer>();

			for(int n = 1; n < 10; n++)
				nums.add(n);

			tries[i] = nums;
		}
		
		int counter = 0;
		
		while(true)
		{
			if(counter == -1)
				return true;
			
			int x = list.get(counter)[0];
			int y = list.get(counter)[1];
			
			while(true)
			{
				board[x][y] = 0;
				
				if(tries[counter].isEmpty())
				{
					for(int n = 1; n < 10; n++)
						tries[counter].add(n);

					Shuffler.shuffle(tries[counter]);
					counter-=2;
					break;
				}
				else if(isValid(tries[counter].peek(), x, y, board))
				{
					board[x][y] = tries[counter].pop();
					
					if(counter == list.size()-1)
					{
						if(solutionFound)
							return false;
						else
							solutionFound = true;
												
						counter--;
					}
					
					break;
				}
				else
				{
					tries[counter].pop();
				}
			}
			
			counter++;
		}
	}

	private static boolean isValid(int i, int x, int y, int[][] board) 
	{		
		for(int check = 0; check < 9; check++)
		{
			if(board[x][check] == i || board[check][y] == i)
				return false;
		}

		ArrayList<Integer> invalidNumbers = new ArrayList<Integer>();
		int chunkX = x/3;
		int chunkY = y/3;

		for(int chunkIndexX = chunkX*3; chunkIndexX < chunkX*3+3; chunkIndexX++)
			for(int chunkIndexY = chunkY*3; chunkIndexY < chunkY*3+3; chunkIndexY++)
				invalidNumbers.add(board[chunkIndexX][chunkIndexY]);

		return !invalidNumbers.contains(i);
	}	
}