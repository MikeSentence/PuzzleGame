package Puzzle;

/**
 * 
 * @author Mike Sentence
 *
 */
public interface Puzzle {

	/**
	 * 将image切割为4*4的16张图片
	 */
	public void cutImg();

	/**
	 * 将打乱后拼图放到对应的按钮上
	 */
	public void setButton();

	/**
	 * 利用分解的16张图片或自定义按钮颜色生成一个拼图
	 */
	public void createPuzzle();

	/**
	 * 将当前拼图显示出来供玩家玩耍
	 */
	public void display();

	/**
	 * 判断拼图是否成功
	 * 
	 * @return
	 */
	public boolean isSuccessful();
}
