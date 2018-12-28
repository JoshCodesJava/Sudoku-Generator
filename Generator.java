import java.util.ArrayList;
import java.util.Stack;

public class Generator
{
	public static int[][] getBoard() 
	{
		int[][] board = new int[9][9];
		Stack<Integer>[] tries = new Stack[81];

		for(int i = 0; i < 81; i++)
		{
			Stack<Integer> nums = new Stack<Integer>();

			for(int n = 1; n < 10; n++)
				nums.add(n);

			Shuffler.shuffle(nums);
			tries[i] = nums;
		}

		for(int i = 0; i < 81; i++)
		{

			int x = i%9;
			int y = i/9;

			while(true)
			{
				board[x][y] = 0;
				
				if(tries[i].isEmpty())
				{
					for(int n = 1; n < 10; n++)
						tries[i].add(n);

					Shuffler.shuffle(tries[i]);
					i-=2;
					break;
				}
				else if(isValid(tries[i].peek(), x, y, board))
				{
					board[x][y] = tries[i].pop();
					break;
				}
				else
				{
					tries[i].pop();
				}
			}
		}

		return board;
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