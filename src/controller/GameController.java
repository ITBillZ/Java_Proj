package controller;

import components.ChessGridComponent;
import components.ChessPiece;
import gameUtil.GameUtil;
import model.GameStack;
import view.game.*;
import view.home.HomeFrame;

import javax.swing.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Stack;

public class GameController {
	private ChessBoardPanel gamePanel;
	private StatusPanel statusPanel;
	private ScorePanel scorePanel;
	private ChessPiece currentPlayer;
	private ToolPanel toolPanel;
	private AIController aiController;

	public BoardController boardController; //改成public看看

	private int blackScore;
	private int whiteScore;

	private boolean cheatMode;
	private boolean machineMode;
	private ChessPiece manPiece; //玩家执棋色
	private ChessPiece machinePiece; //电脑执棋色

	private Runnable runnable;

	public GameController(ChessBoardPanel gamePanel, StatusPanel statusPanel, ScorePanel scorePanel) {
		this.gamePanel = gamePanel;
		this.statusPanel = statusPanel;
		this.scorePanel = scorePanel;
//		this.toolPanel = toolPanel;
		this.currentPlayer = ChessPiece.BLACK;
		this.aiController = new AIController();

		blackScore = whiteScore = 2;
	}

	public ChessPiece getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(ChessPiece currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public void swapPlayer() {
		currentPlayer = (currentPlayer == ChessPiece.BLACK) ? ChessPiece.WHITE : ChessPiece.BLACK;

	}

	public int getBlackScore() {
		return blackScore;
	}

	public int getWhiteScore() {
		return whiteScore;
	}

	public BoardController getBoardController() {
//		return boardController;
		//返回新的
		return new BoardController(boardController.board, currentPlayer.getColor());
	}

	public boolean isCheatMode() {
		return cheatMode;
	}

	public void setCheatMode(boolean cheatMode) {
		this.cheatMode = cheatMode;
	}

	public boolean isMachineMode() {
		return machineMode;
	}

	public void setMachineMode(boolean machineMode) {
		this.machineMode = machineMode;
	}

	public ChessPiece getManPiece() {
		return manPiece;
	}

	public void setManPiece(ChessPiece manPiece) {
		this.manPiece = manPiece;
	}

	/**
	 * @author Bill
	 * @description 初始化分数，执棋方
	 **/
	public void initController() {
		initController(2, 2, ChessPiece.BLACK);
	}

	public void initController(int black, int white, ChessPiece player) {
		blackScore = black;
		whiteScore = white;
		currentPlayer = player;
	}

	/**
	 * @author Bill
	 * @description 计算分数
	 **/
	public void countScore() {
		ChessGridComponent[][] chessGrids = gamePanel.getChessGrids();
		int black = 0, white = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (chessGrids[i][j].getChessPiece() == ChessPiece.BLACK) {
					black++;
				} else if (chessGrids[i][j].getChessPiece() == ChessPiece.WHITE) {
					white++;
				}
			}
		}
		blackScore = black;
		whiteScore = white;
		scorePanel.repaint();
	}

	/**
	 * @author Bill
	 * @description 和正常的set不一样，为了把正确的Board作为参数
	 **/
	public void setBoardController() {
		ChessGridComponent[][] chessGrids = gamePanel.getChessGrids();

		int[][] board = GameUtil.gridArrayToIntArray(chessGrids);
		boardController = new BoardController(board, currentPlayer.getColor());
	}

	/**
	 * @author Bill
	 * @description 是否可以点击某个格子
	 **/
	public boolean canClick(int row, int col) {
		return boardController.isLegalPlace(false, row, col);
	}

	public boolean hasLegalClick() {
		setBoardController();
		return boardController.getAllLegalPlaces();

	}

	public void flipPiece() {

		ChessGridComponent[][] chessGrids = gamePanel.getChessGrids();
		HashSet<Integer[]> revPlace = boardController.revPlace;

		for (Integer[] pos : revPlace) {
			chessGrids[pos[1]][pos[0]].setChessPiece(currentPlayer);
		}

		gamePanel.repaint();
	}

	/**
	 * @author Bill
	 * @description 判断游戏是否结束
	 **/
	public boolean gameOver() {
		//交换两次后，都没有棋走
		boolean noMove1 = hasLegalClick();
		swapPlayer();
		boolean noMove2 = hasLegalClick();
		//要交换回来
		swapPlayer();

		//交换两次后，都没有棋走，返回true
		return !noMove1 && !noMove2;
	}

	/**
	 * @author Bill
	 * @description 游戏结束时获取胜利者
	 **/
	public ChessPiece getWinner() {
		if (blackScore > whiteScore) {
			return ChessPiece.BLACK;
		} else if (blackScore < whiteScore) {
			return ChessPiece.WHITE;
		} else {
			return null;
		}

	}

	/**
	 * @author Bill
	 * @description 重玩，初始化所有的组件
	 **/
	public void restart() {
		//把Stack清空
		GameStack.oldBoard.clear();
		GameStack.oldPlayer.clear();

		GameFrame.chessBoardPanel.initialChessGrids();
		GameFrame.chessBoardPanel.initialGame();
		GameFrame.controller.initController();
		HomeFrame.gameFrame.repaint();

		ClockPanel.initTime();
		ClockPanel.timer.start();

		//如果为人机模式，而且先手，则先走一步
		if (machineMode && manPiece != ChessPiece.BLACK) {
			machineTurn();
		}
	}

//	public void readFileData(File file) {
//		BufferedReader reader = null;
//		try {
//			//清空栈
//			GameStack.oldBoard.clear();
//			GameStack.oldPlayer.clear();
//
//			reader = new BufferedReader(new FileReader(file));
//			int totalBoard = Integer.parseInt(reader.readLine());
//
//			for (int k = 0; k < totalBoard; k++) {
//				ChessPiece color = GameUtil.intToPiece(Integer.parseInt(reader.readLine()));
//				ChessPiece[][] pieces = new ChessPiece[8][8];
//				for (int i = 0; i < 8; i++) {
//					String[] str = reader.readLine().split("\\s+");
//					for (int j = 0; j < 8; j++) {
//						pieces[j][i] = GameUtil.intToPiece(Integer.parseInt(str[j]));
//					}
//				}
//				//压栈
//				if (k < totalBoard - 1) {
//					GameStack.oldBoard.push(pieces);
//					GameStack.oldPlayer.push(color);
//				} else {
//					//最后一个棋盘单元显示
//					GameUtil.updateGrids(gamePanel.getChessGrids(), pieces);
//				}
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			//程序可以报错，文件必须关闭
//			try {
//				if (reader != null) {
//					reader.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}

//	public void writeDataToFile(File file) {
//		BufferedWriter writer = null;
//		try {
//			writer = new BufferedWriter(new FileWriter(file));
//
//			//把当前棋盘放最底下
//			Stack<ChessPiece[][]> board = new Stack<>();
//			board.push(GameUtil.gridArrayToPieceArray(GameFrame.chessBoardPanel.getChessGrids()));
//			Stack<ChessPiece> color = new Stack<>();
//			color.push(currentPlayer);
//
//			//把之前的棋盘全部反转，加入board和color
//			board.addAll(GameStack.reverseStack(GameStack.oldBoard));
//			color.addAll(GameStack.reverseStack(GameStack.oldPlayer));
//
//			//文件第一行记录一共有多少个单元
//			writer.write(board.size() + "\n");
//
//			while (board.size() > 0) {
//				ChessPiece[][] b = board.pop();
//				ChessPiece c = color.pop();
//				//每个单元有9行，第一行是将要下棋的玩家
//				writer.write(c.getColor() + "\n");
//				for (int i = 0; i < 8; i++) {
//					StringBuilder s = new StringBuilder();
//					//剩余八行是棋盘
//					for (int j = 0; j < 8; j++) {
//						if (b[j][i] == null) {
//							s.append("0 ");
//						} else {
//							s.append(b[j][i].getColor()).append(" ");
//						}
//					}
//					writer.write(s + "\n");
//				}
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (writer != null) {
//				try {
//					writer.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}

	public void writeDataToFile(File file) {
		//等会再弹出一个
		GameStack.oldBoard.push(GameUtil.gridArrayToPieceArray(GameFrame.chessBoardPanel.getChessGrids()));
		GameStack.oldPlayer.push(currentPlayer);

		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(file));

			oos.writeObject(GameStack.oldPlayer);
			oos.flush();
			oos.writeObject(GameStack.oldBoard);
			oos.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		GameStack.oldPlayer.pop();
		GameStack.oldBoard.pop();

	}

	public void readFileData(File file) {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(file));

			Stack<ChessPiece> color = (Stack<ChessPiece>) ois.readObject();
			Stack<ChessPiece[][]> pieces = (Stack<ChessPiece[][]>) ois.readObject();

			GameStack.oldPlayer = color;
			GameStack.oldBoard = pieces;

			currentPlayer = GameStack.oldPlayer.pop();
			GameUtil.updateGrids(gamePanel.getChessGrids(), GameStack.oldBoard.pop());

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}


	public void click(int row, int col) {
		ChessPiece[][] old = GameUtil.gridArrayToPieceArray(gamePanel.getChessGrids());
		ClockPanel.timer.start();


		if (isCheatMode()) {

			GameStack.oldBoard.push(old);
			GameStack.oldPlayer.push(currentPlayer);

			gamePanel.getChessGrids()[col][row].setChessPiece(currentPlayer);

			HomeFrame.gameFrame.repaint();
			return;
		}


		if (canClick(row, col)) {
			//设置刚刚下的子的位置，便于画出
			int[] b = gamePanel.beforePlace;
			if (b != null) {
				gamePanel.getChessGrids()[b[0]][b[1]].setJustNow(false);
			}
			gamePanel.getChessGrids()[col][row].setJustNow(true);
			gamePanel.beforePlace = new int[]{col, row};


			//把前一步压栈
			GameStack.oldBoard.push(old);
			GameStack.oldPlayer.push(currentPlayer);

			flipPiece();
			//翻棋后交换玩家
			swapPlayer();
			countScore();
			System.out.println("Black:" + blackScore);
			System.out.println("White:" + blackScore);

//			HomeFrame.gameFrame.repaint();

			//下一位玩家是否可以下棋，若不能，再次交换
			if (!hasLegalClick()) {
				System.out.println(currentPlayer + "不能下棋！");
				swapPlayer();
			}

			//当前玩家下完之后，判断游戏是否结束
			if (gameOver()) {
				ClockPanel.timer.stop();
				String msg;
				if (getWinner() == null) {
					msg = "势均力敌，打成平手！是否重新开始？";
				} else {
					msg = getWinner() + "胜利！是否重新开始？";
				}
				int res = JOptionPane.showConfirmDialog(HomeFrame.gameFrame, msg, "游戏结束", JOptionPane.OK_CANCEL_OPTION);
				if (res == JOptionPane.OK_OPTION) {
					restart();
				}
			}


			System.out.printf("*******%s*******\n", currentPlayer);

			ClockPanel.initTime(); //重置时间

			if (isMachineMode() && currentPlayer != manPiece) {
				machineTurn();
			}

			HomeFrame.gameFrame.repaint();
		}
	}

	/**
	 * @author Bill
	 * @description 新线程，用于计算机器所走位置，防止组件刷新不及时
	 **/
	public void machineTurn() {
		runnable = new Thread(() -> {
			try {
				System.out.println("计算中");
//				Integer[] greedy = aiController.minMax(currentPlayer.getColor());
				Integer[] place = aiController.alphaBetaSearch(currentPlayer.getColor());

				System.out.println(Arrays.toString(place));
				click(place[0], place[1]);
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		SwingUtilities.invokeLater(runnable);

	}
}