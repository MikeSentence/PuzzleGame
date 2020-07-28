package Puzzle;

import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

/**
 * 
 * @author Mike Sentence
 *
 */
public abstract class PuzzleImpl implements Puzzle {
	protected BufferedImage image;// 当前拼图的图片，用BufferedImage对象利于裁切
	protected MyImage[][] imgs = new MyImage[4][4];// 显示在按钮上的图片
	protected MyButton[][] buttons = new MyButton[4][4];// 拼图的按钮
	protected JFrame jf = new JFrame("4*4拼图小游戏");// 建立一个名为"4*4拼图小游戏"的窗口

	public PuzzleImpl() {
		// 初始化拼图按钮
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++) {
				buttons[i][j] = new MyButton();
				buttons[i][j].setLocation(i, j);// 保存按钮的位置
			}
	}

	@Override
	public void createPuzzle() {
		if (image != null)
			jf.setSize(image.getWidth() + 100, image.getHeight() + 70);// 设置窗口大小
		else
			jf.setSize(500, 500);// 这一步用于红蓝按钮游戏
		jf.setResizable(false);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// 网格布局添加按钮
		Panel p1 = new Panel(new GridLayout(4, 4));
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++) {
				p1.add(buttons[i][j]);
			}
		jf.add(p1);
	}

	@Override
	public void display() {
		this.cutImg();
		this.setButton();
		this.createPuzzle();
		// 设置关闭窗口的事件
		jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jf.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int f = JOptionPane.showConfirmDialog(null, "确定退出游戏吗？", "", JOptionPane.YES_NO_OPTION);
				if (f == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		jf.setVisible(true);
	}
}
