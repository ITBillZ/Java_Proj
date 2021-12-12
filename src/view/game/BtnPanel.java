package view.game;

import components.ChessPiece;
import components.MyButton;
import gameUtil.GameUtil;
import model.GameStack;
import view.home.HomeFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class BtnPanel extends JPanel {
	private MyButton restartBtn;
	private MyButton loadGameBtn;
	private MyButton saveGameBtn;
	private MyButton backBtn;
	private MyButton homeBtn;
	private JToggleButton cheatBtn;
	private MyButton blackBtn;
	private MyButton whiteBtn;
	private final int space = 50;

	public BtnPanel(int width, int height) {
		setSize(width, height);
		setOpaque(false);
		setLayout(null);
		//重玩按钮
		restartBtn = new MyButton(new AbstractAction("重玩") {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				//弹出对话框，让玩家确认
				ClockPanel.timer.stop();
				int result = JOptionPane.showConfirmDialog(HomeFrame.gameFrame,
								"是否确定重新开始？", "重新开始",
								JOptionPane.OK_CANCEL_OPTION);

				if (result == JOptionPane.OK_OPTION) {
					GameFrame.controller.restart();
				}

			}
		});
		restartBtn.setIcon(new ImageIcon("Othello_7/images/restart2.jpeg"));
		restartBtn.setLocation(0, GameFrame.statusPanel.getY());
		add(restartBtn);


		//载入文件按钮
		loadGameBtn = new MyButton(new AbstractAction("载入对局") {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				ClockPanel.timer.stop();
				//使用JFileChooser
				JFileChooser chooser = new JFileChooser(".");

				if (chooser.showOpenDialog(HomeFrame.gameFrame) == 0) {
					//选择了目标文件
					File file = chooser.getSelectedFile();

					//要判断，不然一点载入就不能悔棋了
					//把Stack清空
					GameStack.oldBoard.clear();
					GameStack.oldPlayer.clear();

					GameFrame.controller.readFileData(file);
					HomeFrame.gameFrame.repaint();
					JOptionPane.showMessageDialog(HomeFrame.gameFrame, "载入对局成功",
									"提示信息", JOptionPane.PLAIN_MESSAGE);
					ClockPanel.initTime();

				}
				ClockPanel.timer.start();
			}
		});
		loadGameBtn.setLocation(0, GameFrame.statusPanel.getY() + space);
		add(loadGameBtn);


		//保存文件按钮
		saveGameBtn = new MyButton(new AbstractAction("保存对局") {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				ClockPanel.timer.stop();
				//使用JFileChooser
				JFileChooser chooser = new JFileChooser(".");
				if (chooser.showSaveDialog(HomeFrame.gameFrame) == 0) {
					//选择了目标文件
					File file = chooser.getSelectedFile();
					GameFrame.controller.writeDataToFile(file);
				}
				ClockPanel.timer.start();
			}
		});
		saveGameBtn.setLocation(0, GameFrame.statusPanel.getY() + 2 * space);
		add(saveGameBtn);


		//悔棋按钮
		backBtn = new MyButton(new AbstractAction("悔棋") {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				//弹出栈
				if (GameStack.oldBoard.size() == 0) {
					//没有历史记录
					JOptionPane.showMessageDialog(HomeFrame.gameFrame, "已经是第一步！",
									"悔棋失败", JOptionPane.WARNING_MESSAGE);
				} else {
					ClockPanel.initTime();

					//必须遍历，不能直接ChessGrids赋值
					GameUtil.updateGrids(GameFrame.chessBoardPanel.getChessGrids(), GameStack.oldBoard.pop());
					GameFrame.controller.setCurrentPlayer(GameStack.oldPlayer.pop());

					//人机模式下，一次回退两步
					if (GameFrame.controller.isMachineMode()) {
						GameUtil.updateGrids(GameFrame.chessBoardPanel.getChessGrids(), GameStack.oldBoard.pop());
						GameFrame.controller.setCurrentPlayer(GameStack.oldPlayer.pop());
					}

					GameFrame.controller.countScore(); //重新设置比分

					HomeFrame.gameFrame.repaint();
				}
			}
		});
		backBtn.setBackground(new Color(81, 139, 241));
		backBtn.setLocation(0, GameFrame.statusPanel.getY() + 3 * space);
		add(backBtn);


		//返回主界面按钮
		homeBtn = new MyButton(new AbstractAction("返回主界面") {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				ClockPanel.timer.stop();
				int result = JOptionPane.showConfirmDialog(HomeFrame.gameFrame,
								"确认回到主菜单吗？", "确认对话框",
								JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {

					GameFrame.homeFrame.setVisible(true);
					HomeFrame.gameFrame.setVisible(false);
				} else {
					ClockPanel.timer.start();
				}
			}
		});
		homeBtn.setBackground(new Color(81, 139, 241));
		homeBtn.setLocation(0, GameFrame.statusPanel.getY() + 4 * space);
		add(homeBtn);


		cheatBtn = new JToggleButton("作弊模式", false);
		cheatBtn.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		cheatBtn.setSize(130, 40);
		cheatBtn.setLocation(0, GameFrame.statusPanel.getY() + 5 * space);
		add(cheatBtn);

		blackBtn = new MyButton(new AbstractAction("黑棋") {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				GameFrame.controller.setCurrentPlayer(ChessPiece.BLACK);

				HomeFrame.gameFrame.repaint();
			}
		});
		blackBtn.setSize(90, 40);
		blackBtn.setLocation(cheatBtn.getX() + cheatBtn.getWidth(), cheatBtn.getY() - 20);
		blackBtn.setVisible(false);
		add(blackBtn);

		whiteBtn = new MyButton(new AbstractAction("白棋") {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				GameFrame.controller.setCurrentPlayer(ChessPiece.WHITE);

				HomeFrame.gameFrame.repaint();
			}
		});
		whiteBtn.setSize(90, 40);
		whiteBtn.setLocation(cheatBtn.getX() + cheatBtn.getWidth(), cheatBtn.getY() + 20);
		whiteBtn.setVisible(false);
		add(whiteBtn);

		cheatBtn.addActionListener(e -> {
			boolean isCheat = cheatBtn.isSelected();
			GameFrame.controller.setCheatMode(isCheat);

			blackBtn.setVisible(isCheat);
			whiteBtn.setVisible(isCheat);

			if (isCheat) {
				ClockPanel.timer.stop();
			} else {
				ClockPanel.timer.start();
			}

		});

	}
}
