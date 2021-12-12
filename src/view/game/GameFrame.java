package view.game;

import controller.AIController;
import controller.GameController;
import gameUtil.Path;
import view.home.HomeFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameFrame extends JFrame {
	public static GameController controller;
	public static ChessBoardPanel chessBoardPanel;
	public static StatusPanel statusPanel;
	public static BackgroundPanel bgPanel;
	public static HomeFrame homeFrame;

	public static ScorePanel scorePanel;
	private static BtnPanel btnPanel;

	private ToolPanel toolPanel;

	private AIController aiController;

	private BufferedImage bg;


	public GameFrame(int frameWidth, int frameHeight) {
		this.setTitle("Reversi");
		this.setLayout(null);


		//获取窗口边框的长度(窗口四周黑边)，
		//将这些值加到主窗口大小上，这能使窗口大小和预期相符
		Insets inset = this.getInsets();
		this.setSize(frameWidth + inset.left + inset.right,
						frameHeight + inset.top + inset.bottom);

		//此窗口将置于屏幕的中央
		this.setLocationRelativeTo(null);


		chessBoardPanel = new ChessBoardPanel(640);
		chessBoardPanel.setLocation(30, 40);

		statusPanel = new StatusPanel(200, chessBoardPanel.getHeight());
		statusPanel.setLocation(chessBoardPanel.getX() + chessBoardPanel.getWidth() + 30, chessBoardPanel.getY());

		scorePanel = new ScorePanel(500, 20);
		scorePanel.setLocation(10, 10);

		controller = new GameController(chessBoardPanel, statusPanel, scorePanel);
		this.add(chessBoardPanel);
		this.add(statusPanel);
		this.add(scorePanel);

		btnPanel = new BtnPanel(300, chessBoardPanel.getHeight());
		btnPanel.setLocation(statusPanel.getX() + statusPanel.getWidth(), statusPanel.getY());
		add(btnPanel);


		//设置背景
		bgPanel = new BackgroundPanel(frameWidth, frameHeight, Path.GAME_BG);
		bgPanel.setLocation(0, 0);
		this.add(bgPanel);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}
