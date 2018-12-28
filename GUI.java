import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.*;

public class GUI extends JPanel implements ActionListener
{
	private int[][] solvedBoard;
	private int[][] board;
	private int[][] answers = new int[9][9];
	private JButton resetButton = new JButton("New Puzzle");
	private JButton answerButton = new JButton("Solution");
	private JButton checkButton = new JButton("Check Errors");
	private JButton reset = new JButton("Start Over");
	private int selectedX = -1;
	private int selectedY = -1;
	private boolean showSolution = false;
	private boolean errorState = false;

	private JPanel drawingPanel = new JPanel()
	{
		public void paint(Graphics g)
		{
			Graphics2D g2d = (Graphics2D) g;
			g2d.clearRect(0, 0, 603, 603);
			g2d.setFont(new Font("Serif", Font.BOLD, 60));

			if(selectedX >= 0 && !showSolution)
			{
				g2d.setColor(Color.YELLOW);
				g2d.fillRect(selectedX*67, selectedY*67, 67, 67);
			}

			g2d.setColor(Color.BLACK);

			for(int x = 0; x < 9; x++)
			{
				for(int y = 0; y < 9; y++)
				{
					g2d.drawRect(x*67, y*67, 67, 67);

					if(board[x][y] > 0)
						g2d.drawString(String.valueOf(board[x][y]), x*67+20, y*67+60);
					else if(showSolution)
					{
						g2d.setColor(Color.MAGENTA);
						g2d.drawString(String.valueOf(solvedBoard[x][y]), x*67+20, y*67+60);
						g2d.setColor(Color.BLACK);
					}
					else if(answers[x][y] > 0)
					{
						if(errorState)
						{
							if(isAllowed(x, y))
								g2d.setColor(Color.GREEN);
							else
								g2d.setColor(Color.RED);
						}
						else
						{
							g2d.setColor(Color.BLUE);
						}

						g2d.drawString(String.valueOf(answers[x][y]), x*67+20, y*67+60);
						g2d.setColor(Color.BLACK);
					}
				}
			}

			g2d.fillRect(67*3-7, 0, 14, 603);
			g2d.fillRect(67*6-7, 0, 14, 603);
			g2d.fillRect(0, 67*3, 603, 14);
			g2d.fillRect(0, 67*6, 603, 14);
		}

		private boolean isAllowed(int posX, int posY)
		{
			int[][] errorBoard = new int[9][9];
			int num = answers[posX][posY];
			
			for(int x = 0; x < 9; x++)
			{
				for(int y = 0; y < 9; y++)
				{
					errorBoard[x][y] = Math.max(answers[x][y], + board[x][y]);
				}
			}
			
			errorBoard[posX][posY] = 0;
			return isValid(num, posX, posY, errorBoard);
		}
		
		private boolean isValid(int i, int x, int y, int[][] board) 
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
	};

	public GUI()
	{
		addMouseWheelListener(new MouseWheelListener()
		{
			public void mouseWheelMoved(MouseWheelEvent arg0) 
			{
				if(selectedX >= 0 && !showSolution)
				{
					if(arg0.getWheelRotation() < 0)
						answers[selectedX][selectedY] = (answers[selectedX][selectedY]+1)%10;
					else if(arg0.getWheelRotation() > 0)
					{
						if(answers[selectedX][selectedY] == 0)
							answers[selectedX][selectedY] = 9;
						else
							answers[selectedX][selectedY] = (answers[selectedX][selectedY]-1);
					}
				}

				repaint();
			}

		});

		addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent arg0)
			{
				int x = arg0.getX()/67;
				int y = arg0.getY()/67;

				if(x < 9 && y < 9 && board[x][y] == 0 && !showSolution)
				{
					selectedX = x;
					selectedY = y;
					repaint();
				}
			}

			public void mouseEntered(MouseEvent arg0){}
			public void mouseExited(MouseEvent arg0){}
			public void mousePressed(MouseEvent arg0){}
			public void mouseReleased(MouseEvent arg0){}
		});

		resetButton.addActionListener(this);
		answerButton.addActionListener(this);
		checkButton.addActionListener(this);
		reset.addActionListener(this);
		solvedBoard = Generator.getBoard();
		board = Parser.parse(solvedBoard);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(603, 653));
		drawingPanel.setPreferredSize(new Dimension(603, 603));
		add(drawingPanel);
		add(Box.createVerticalStrut(10));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(603, 40));
		Box panelBox = Box.createHorizontalBox();
		panelBox.add(resetButton);
		panelBox.add(Box.createHorizontalStrut(30));
		panelBox.add(checkButton);
		panelBox.add(Box.createHorizontalStrut(30));
		panelBox.add(reset);
		panelBox.add(Box.createHorizontalStrut(30));
		panelBox.add(answerButton);
		buttonPanel.add(panelBox);
		add(buttonPanel);
	}

	public void paint(Graphics g)
	{
		super.paint(g);
		drawingPanel.repaint();
	}

	public void actionPerformed(ActionEvent evt) 
	{
		if(evt.getSource() == resetButton)
		{
			selectedX = -1;
			selectedY = -1;
			answers = new int[9][9];
			showSolution = false;
			solvedBoard = Generator.getBoard();
			board = Parser.parse(solvedBoard);
			repaint();
		}
		else if(evt.getSource() == answerButton)
		{
			showSolution = !showSolution;
			repaint();
		}
		else if(evt.getSource() == checkButton)
		{
			errorState = !errorState;
			checkButton.setText(errorState ? "Hide Errors" : "Check Errors");
			repaint();
		}
		else if(evt.getSource() == reset)
		{
			answers = new int[9][9];
			selectedX = -1;
			selectedY = -1;
			repaint();
		}
	}

	public static void main(String[] args) 
	{
		JFrame frame = new JFrame();
		GUI gui = new GUI();
		frame.add(gui);
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}