package Puzzle;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * 
 * @author Mike Sentence
 *
 */
public class MyImage extends ImageIcon {

	// x 与 y分别保存图片的横纵坐标
	private int x;
	private int y;
	private Icon icon;
	private static final long serialVersionUID = 1L;

	public MyImage(Icon icon) {
		this.icon = icon;
	}

	/**
	 * 设置图片
	 */
	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	/**
	 * 返回它的图片
	 * 
	 * @return
	 */
	public Icon getIcon() {
		return icon;
	}

	/**
	 * 设置横纵坐标
	 * 
	 * @param x 横坐标
	 * @param y 纵坐标
	 */
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
