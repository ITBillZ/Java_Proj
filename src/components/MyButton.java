package components;

import javax.swing.*;
import java.awt.*;

public class MyButton extends JButton {
	public MyButton(AbstractAction act) {
		setAction(act);
//		setIcon(icon);

		setFont(new Font(Font.SERIF, Font.BOLD, 20));
		setSize(130, 40);
	}
}
