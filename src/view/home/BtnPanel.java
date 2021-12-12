package view.home;

import components.ChessPiece;
import components.MyButton;
import model.GameStack;
import view.game.ClockPanel;
import view.game.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BtnPanel extends JPanel {
	private MyButton two;
	private JToggleButton manMachine;
	private MyButton blackBtn;
	private MyButton whiteBtn;

	public BtnPanel(int width, int height) {
		setSize(width, height);
		setLayout(null);

		two = new MyButton(new AbstractAction("双人游戏") {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (GameStack.oldPlayer.size() > 0) {
					int result = JOptionPane.showConfirmDialog(HomeFrame.gameFrame, "检测到历史对局！是否重新开始？",
									"提示", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
					if (result == JOptionPane.OK_OPTION) {
						GameFrame.controller.restart();
					}
				}

				ClockPanel.initTime();
				ClockPanel.timer.start();
				GameFrame.homeFrame.setVisible(false);
				HomeFrame.gameFrame.setVisible(true);
				ClockPanel.timer.start();
				GameFrame.controller.setMachineMode(false);
			}
		});
		two.setLocation(0, 0);
		this.add(two);


		manMachine = new JToggleButton(new AbstractAction("人机对战") {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				boolean isMachine = manMachine.isSelected();
				blackBtn.setVisible(isMachine);
				whiteBtn.setVisible(isMachine);
			}
		});
		manMachine.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		manMachine.setSize(130, 40);
		manMachine.setLocation(0, 50);
		add(manMachine);


		blackBtn = new MyButton(new AbstractAction("我方执黑") {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {

				GameFrame.homeFrame.setVisible(false);
				HomeFrame.gameFrame.setVisible(true);

				GameFrame.controller.restart();

				//设置为人机模式
				GameFrame.controller.setMachineMode(true);
				GameFrame.controller.setManPiece(ChessPiece.BLACK);

				blackBtn.setVisible(false);
				whiteBtn.setVisible(false);
				manMachine.setSelected(false);
			}
		});
		blackBtn.setLocation(manMachine.getX() + manMachine.getWidth(), manMachine.getY() - 20);
		blackBtn.setVisible(false);
		add(blackBtn);

		whiteBtn = new MyButton(new AbstractAction("我方执白") {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				GameFrame.homeFrame.setVisible(false);
				HomeFrame.gameFrame.setVisible(true);



				//设置为人机模式
				GameFrame.controller.setMachineMode(true);
				GameFrame.controller.setManPiece(ChessPiece.WHITE);
				//电脑先下一步
				HomeFrame.gameFrame.repaint();

				GameFrame.controller.restart();

				blackBtn.setVisible(false);
				whiteBtn.setVisible(false);
				manMachine.setSelected(false);
			}
		});
		whiteBtn.setLocation(manMachine.getX() + manMachine.getWidth(), manMachine.getY() + 20);
		whiteBtn.setVisible(false);
		add(whiteBtn);


	}

}
