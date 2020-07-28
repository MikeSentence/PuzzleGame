package Puzzle;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * 
 * @author Mike Sentence
 *
 */
public class RedBluePuzzle extends PuzzleImpl implements ActionListener {

	public static void main(String[] args) {
		RedBluePuzzle puzzle = new RedBluePuzzle();
		puzzle.display();

	}

	@Override
	public void cutImg() {
		return;
	}

	@Override
	public void setButton() {
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++) {
				buttons[i][j] = new MyButton(null);
				buttons[i][j].setBackground(Color.blue);// 先对所有按钮设置成蓝色
				buttons[i][j].setLocation(i, j);// 保存按钮的位置
				buttons[i][j].addActionListener(this);// 对每个按钮添加点击事件
				buttons[i][j].setEnabled(false);// 先对所有按钮设置不可点击状态
			}
		buttons[0][0].setBackground(Color.red);// 左上角设置为红色
		buttons[0][1].setEnabled(true);// 红色按钮右边按钮设置为可点击状态
		buttons[1][0].setEnabled(true);// 红色按钮下边按钮设置为可点击状态
	}

	@Override
	public boolean isSuccessful() {
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton jb = (JButton) e.getSource();
		if (jb.getBackground() == Color.red)
			return;

		// dx与dy数组分别对相邻横纵坐标进行枚举
		int[] dx = { 0, 0, -1, 1 };
		int[] dy = { 1, -1, 0, 0 };

		int x = jb.getX();
		int y = jb.getY();

		// 寻找四周有无红色按钮
		for (int i = 0; i < 4; i++) {
			int xx = x + dx[i];
			int yy = y + dy[i];
			if (xx >= 0 && xx < 4 && yy >= 0 && yy < 4)

				// 若它旁边有红色按钮，则可以交换
				if (buttons[xx][yy].getBackground() == Color.red) {

					// 将旧红色块四周按钮设为不可用状态
					for (int j = 0; j < 4; j++) {
						int xxx = xx + dx[j];
						int yyy = yy + dy[j];
						if (xxx >= 0 && xxx < 4 && yyy >= 0 && yyy < 4) {
							buttons[xxx][yyy].setEnabled(false);
						}
					}

					// 将新当前的蓝色块四周按钮设为可用状态
					for (int j = 0; j < 4; j++) {
						int xxx = x + dx[j];
						int yyy = y + dy[j];
						if (xxx >= 0 && xxx < 4 && yyy >= 0 && yyy < 4) {
							buttons[xxx][yyy].setEnabled(true);
						}
					}

					// 改变这两个按钮的颜色
					buttons[x][y].setBackground(Color.red);
					buttons[xx][yy].setBackground(Color.blue);
					break;
				}
		}
	}
}
