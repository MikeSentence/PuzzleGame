package Puzzle;

import javax.swing.Icon;
import javax.swing.JButton;

/**
 * 
 * @author Mike Sentence
 *
 */
public class MyButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2524035003616283092L;
	// x与 y分别保存图片的横纵坐标
	private int x;
	private int y;

	public MyButton(Icon icon) {
		super(icon);
	}

	public MyButton() {
		super();
	}

	/**
	 * 设置横纵坐标
	 * 
	 * @param x 横坐标
	 * @param y 纵坐标
	 */
	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 返回横坐标
	 * 
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * 返回纵坐标
	 * 
	 * @return
	 */
	public int getY() {
		return y;
	}
}
