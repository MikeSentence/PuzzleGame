package Puzzle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 
 * @author Mike Sentence
 *
 */
public class FunnyPuzzle extends PuzzleImpl implements ActionListener {

	// 六个工具栏按钮
	private JButton recover = new JButton("恢复");
	private JButton start = new JButton("开始");
	private JButton suspend = new JButton("暂停");
	private JButton restart = new JButton("继续");
	private JButton tip = new JButton("提示");
	private JButton getImgSource = new JButton("选择图片");

	// 记录实时状态，步数及用时
	private int step = 0;
	private JTextArea steps = new JTextArea("步数:0");
	private int time = 0;
	private JTextArea timecnt = new JTextArea("用时:" + "0s");

	// 原创声明
	private JTextArea origin = new JTextArea("@作者：\n     Mike Sentence");

	// 状态判断量
	public boolean isTip = false;// 是否点开了提示
	public boolean isStarting = false;// 是否点击过开始
	public boolean isSuspending = false;// 是否点击了暂停

	/**
	 * 游戏计时器，如果开始且未暂停，计时器每秒+1，否则计时不变动
	 */
	private Timer timer = new Timer(1000, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (isStarting && !isSuspending)
				timecnt.setText("用时:" + (++time) + "s");
		}
	});

	public static void main(String[] args) {
		FunnyPuzzle puzzle = new FunnyPuzzle("1.png");
		puzzle.display();
	}

	public FunnyPuzzle(String filename) {
		try {
			image = ImageIO.read(new File(filename));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void cutImg() {
		// 子图的宽和高
		int w = image.getWidth() / 4;
		int h = image.getHeight() / 4;
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++) {
				imgs[i][j] = new MyImage(new ImageIcon(image.getSubimage(j * w, i * h, w, h)));
				imgs[i][j].setLocation(i, j);
			}
	}

	@Override
	public void setButton() {
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++) {
				buttons[i][j].setIcon(imgs[i][j].getIcon());
				buttons[i][j].setVisible(true);
			}
	}

	@Override
	public void createPuzzle() {
		super.createPuzzle();
		JPanel jp = new JPanel(new FlowLayout());// 游戏下方的工具栏
		JPanel jp1 = new JPanel(new GridBagLayout());// 游戏右方的工具栏
		jp.setSize(jf.getWidth(), jf.getHeight() - image.getHeight());
		jp1.setSize(jf.getWidth() - image.getWidth(), image.getHeight());
		// 添加事件
		recover.addActionListener(new Recover());
		start.addActionListener(new Start());
		suspend.addActionListener(new Suspend());
		restart.addActionListener(new Restart());
		// 游戏下方的工具栏
		jp.add(start);
		jp.add(recover);
		jp.add(suspend);
		jp.add(restart);
		jp.add(steps);
		jp.add(timecnt);

		// 游戏右方的工具栏
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		tip.addActionListener(new Tip());
		getImgSource.addActionListener(new GetImgSource());
		jp1.add(tip, gbc);
		jp1.add(getImgSource, gbc);
		jp1.add(origin, gbc);

		// 设置边框线
		jp.setBorder(BorderFactory.createLineBorder(Color.black));
		jp1.setBorder(BorderFactory.createLineBorder(Color.black));

		// 设置布局
		jf.add(jp, BorderLayout.SOUTH);
		jf.add(jp1, BorderLayout.EAST);
	}

	@Override
	public boolean isSuccessful() {
		// 当且仅当所有图片保存的位置和实际的位置一一相符才算成功
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++) {
				if (imgs[i][j].getX() != i || imgs[i][j].getY() != j)
					return false;
			}
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton jb = (JButton) e.getSource();
		// dx与dy数组分别对相邻横纵坐标进行枚举
		int[] dx = { 0, 0, -1, 1 };
		int[] dy = { 1, -1, 0, 0 };
		int x = jb.getX();
		int y = jb.getY();
		// 寻找四周有无不可见按钮
		for (int i = 0; i < 4; i++) {
			int xx = x + dx[i];
			int yy = y + dy[i];
			if (xx >= 0 && xx < 4 && yy >= 0 && yy < 4)
				// 若它周围有不可见按钮，则可以交换
				if (buttons[xx][yy].isVisible() == false) {
					// 交换按钮保存的图片
					Icon ic = buttons[xx][yy].getIcon();
					buttons[xx][yy].setIcon(buttons[x][y].getIcon());
					buttons[x][y].setIcon(ic);
					buttons[xx][yy].setVisible(true);
					buttons[x][y].setVisible(false);

					// 交换图片保存的位置
					int tmpx = imgs[x][y].getX();
					int tmpy = imgs[x][y].getY();
					imgs[x][y].setLocation(imgs[xx][yy].getX(), imgs[xx][yy].getY());
					imgs[xx][yy].setLocation(tmpx, tmpy);
					x = xx;
					y = yy;

					// 成功走了一步，步数增加
					steps.setText("步数:" + (++step));
					break;
				}
		}
		if (isSuccessful()) {
			if (buttons[3][3].isVisible() == false) {// 防止重复点击完成的拼图出来弹框
				buttons[3][3].setVisible(true);
				JOptionPane.showMessageDialog(null, "恭喜你，完成了拼图！用时：" + time + "s," + "步数为:" + step);
				isStarting = false;
			}
		}
	}

	/**
	 * 将图片打乱
	 */
	public void disrupt() {
		int[] dx = { 0, 0, 1, -1 };
		int[] dy = { 1, -1, 0, 0 };
		// 开始打乱图片
		// (x,y)保存缺口按钮的图片所在的位置
		int x = 3;
		int y = 3;
		for (int cnt = 0; cnt < 100; cnt++) {
			Random random = new Random();
			int r = random.nextInt(40) % 4;
			int xx = x + dx[r];
			int yy = y + dy[r];
			if (xx >= 0 && xx < 4 && yy >= 0 && yy < 4) {
				// 交换这俩位置的图片
				Icon ic = imgs[xx][yy].getIcon();
				imgs[xx][yy].setIcon(imgs[x][y].getIcon());
				imgs[x][y].setIcon(ic);
				int tmpx = imgs[x][y].getX();
				int tmpy = imgs[x][y].getY();
				imgs[x][y].setLocation(imgs[xx][yy].getX(), imgs[xx][yy].getY());
				imgs[xx][yy].setLocation(tmpx, tmpy);
				x = xx;
				y = yy;
			}
		}

		// 每张图片都显示到对应位置的JButton上
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				buttons[i][j].setIcon(imgs[i][j].getIcon());
				buttons[i][j].setLocation(i, j);// 保存按钮的位置
				buttons[i][j].addActionListener(this);// 对每个按钮添加点击事件
			}
		}
		buttons[x][y].setVisible(false);
	}

	public void setImg(BufferedImage image) {
		this.image = image;
	}

	/**
	 * 开始，点击开始按钮图片将被打乱，步数归零，计时器从零开始计时
	 *
	 */
	class Start implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isStarting) {
				JOptionPane.showMessageDialog(null, "游戏开始！");
				disrupt();
				isStarting = true;
				time = 0;
				// 计时器开始计时
				timer.start();
			} else
				JOptionPane.showMessageDialog(null, "请点击恢复再点击开始！");
		}

	}

	/**
	 * 恢复，点击恢复按钮图片还原为初始状态
	 *
	 */
	class Recover implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int f = JOptionPane.showConfirmDialog(null, "确定恢复吗？", "", JOptionPane.YES_NO_OPTION);
			if (f == JOptionPane.YES_OPTION) {
				cutImg();
				setButton();
				// 步数归零
				step = 0;
				steps.setText("步数:" + step);
				isTip = false;
				// 时间归零
				time = 0;
				isStarting = false;
				timecnt.setText("用时：0s");
			}
		}
	}

	/**
	 * 提示，点击提示按钮弹出完整图片的缩略图
	 * 
	 */
	class Tip implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (isTip)// 如果已经打开过，不作任何处理
				return;
			JFrame jframe = new JFrame("提示");
			JLabel jl = new JLabel();
			ImageIcon icon = new ImageIcon();
			icon.setImage(image.getScaledInstance(250, 250, Image.SCALE_DEFAULT));
			jl.setIcon(icon);
			jframe.add(jl);
			jframe.setResizable(false);
			jframe.pack();
			jframe.setVisible(true);
			jframe.addWindowListener(new WindowAdapter() {// 关闭提示后还能再打开
				@Override
				public void windowClosing(WindowEvent e) {
					isTip = false;
					super.windowClosing(e);
				}
			});
			isTip = true;
		}
	}

	/**
	 * 暂停，点击暂停按钮使计时器暂停，并且图片全部不显示
	 *
	 */
	class Suspend implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (isStarting && !isSuspending) {// 将所有按钮设为不可显示
				isSuspending = true;
				JOptionPane.showMessageDialog(null, "游戏暂停！");
				for (int i = 0; i < 4; i++)
					for (int j = 0; j < 4; j++)
						buttons[i][j].setVisible(false);
			}
		}
	}

	/**
	 * 继续，在暂停之后可以点击
	 *
	 */
	class Restart implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (isStarting && isSuspending) {// 将不是右下角隐藏块的其它块都设为显示
				for (int i = 0; i < 4; i++)
					for (int j = 0; j < 4; j++)
						if (imgs[i][j].getX() != 3 || imgs[i][j].getY() != 3)
							buttons[i][j].setVisible(true);
				JOptionPane.showMessageDialog(null, "游戏继续！");
				isSuspending = false;
			}
		}
	}

	/**
	 * 用于选择图片
	 */
	class GetImgSource implements ActionListener {
		// 参考资料来源于：https://blog.csdn.net/xietansheng/article/details/75948936
		@Override
		public void actionPerformed(ActionEvent e) {
			// 创建一个默认的文件选取器
			JFileChooser fileChooser = new JFileChooser();

			// 设置默认显示的文件夹为当前文件夹
			fileChooser.setCurrentDirectory(new File("."));
			// 设置文件选择的模式（只选文件、只选文件夹、文件和文件均可选）
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			// 设置是否允许多选
			fileChooser.setMultiSelectionEnabled(false);
			// 添加可用的文件过滤器（FileNameExtensionFilter 的第一个参数是描述, 后面是需要过滤的文件扩展名 可变参数）
			// 设置默认使用的文件过滤器
			fileChooser.setFileFilter(new FileNameExtensionFilter("image(*.jpg, *.png, *.jpeg)", "jpg", "png", "jpeg"));
			// 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
			int result = fileChooser.showOpenDialog(jf);

			if (result == JFileChooser.APPROVE_OPTION) {
				// 如果点击了"确定", 则获取选择的文件路径
				File file = fileChooser.getSelectedFile();
				// 如果允许选择多个文件, 则通过下面方法获取选择的所有文件
				// File[] files = fileChooser.getSelectedFiles();
				try {
					BufferedImage img = ImageIO.read(file);
					if (img.getWidth() != img.getHeight()) {
						JOptionPane.showMessageDialog(null, "选择的图片应为正方形");
						return;
					}
					// 将新的图片赋给image
					setImg(img);
					// 重新设置窗口大小
					jf.setSize(image.getWidth() + 100, image.getHeight() + 70);
					// 以下与恢复按钮功能一致
					cutImg();
					setButton();
					step = 0;
					steps.setText("步数:" + step);
					isTip = false;
					time = 0;
					isStarting = false;
					timecnt.setText("用时：0s");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
