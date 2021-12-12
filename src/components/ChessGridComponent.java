package components;

import controller.BoardController;
import view.game.GameFrame;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author Bill
 * @description 每个棋格 棋格组件类
 **/
public class ChessGridComponent extends BasicComponent {
	public static int chessSize; //棋子的直径
	public static final int gridSize = 79; //棋格的边长
	private static final Color LIGHT_COLOR = new Color(144, 186, 200); //棋格的颜色
	private static final Color DARK_COLOR = new Color(82, 138, 158); //棋格的颜色
	private final Color color;
	private ChessPiece chessPiece;

	private int row; //行 y
	private int col; //列 x

	private boolean justNow; //是否是刚刚下的子

	//	public ChessGridComponent(int row, int col) {
	public ChessGridComponent(int col, int row) {
		this.setSize(gridSize + 2, gridSize + 2); //设置棋格的边长
		this.row = row; //鼠标所在的行
		this.col = col; //鼠标所在的列
		//格子的颜色深浅不同
		if ((row + col) % 2 == 0) {
			color = LIGHT_COLOR;
		} else {
			color = DARK_COLOR;
		}

	}

	public ChessPiece getChessPiece() {
		return chessPiece;
	}


	public void setChessPiece(ChessPiece chessPiece) {
		this.chessPiece = chessPiece;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	/**
	 * @author Bill
	 * @description 画每个格子的颜色（如果有棋子也画）
	 **/
	public void drawPiece(Graphics g) {
		g.setColor(color);
		//是fill，不是draw！！
		g.fill3DRect(4, 4, gridSize, gridSize, true);

		//TODO 两个7是乱写的
		//画棋子
		if (chessPiece != null) {
			g.drawImage(chessPiece.getImage(), 7, 7, null);
		}

		//标记可以下的点
		//TODO 半透明圆圈
		GameFrame.controller.setBoardController();
		BoardController b = GameFrame.controller.getBoardController();
		g.setColor(Color.GREEN);
		if (b.isLegalPlace(true, row, col)) {
			g.fillOval(40, 40, 10, 10);
		}

		if (justNow) {
			g.setColor(Color.RED);
			System.out.println("justNow");
			g.fillOval(40, 40, 10, 10);
		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawPiece(g);
	}

	@Override
	public void onMouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 &&
						!(GameFrame.controller.isMachineMode() &&
										GameFrame.controller.getCurrentPlayer() != GameFrame.controller.getManPiece())) { //鼠标左键按下
			System.out.printf("%s clicked (%d, %d)\n", GameFrame.controller.getCurrentPlayer(), row, col);
			GameFrame.controller.click(row, col);
		}


//		HomeFrame.gameFrame.repaint();
	}

	@Override
	public void onMouseMoved() {
//		repaint();
		System.out.println("fhasdjhfkladjfklaj;fa");
	}

	public boolean isJustNow() {
		return justNow;
	}

	public void setJustNow(boolean justNow) {
		this.justNow = justNow;
	}
}
