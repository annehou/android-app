package org.annie.catchcrazycat;

/**
 * 画板上的点对象
 * @author lenovo
 *
 */
public class Dot {
	private int x,y; //x代表行，y代表列(和坐标轴不重合)
	private int status; //每个点的状态（有路障，无路障，有神经猫）
	
	public static final int COLOR_ON = 0xFFFF835D; //路障开启时，点的颜色
	public static final int COLOR_OFF = 0xFFB5B5B5; //路障关闭时，点的颜色
	public static final int STATUS_ON = 1; //开启路障（不可走）
	public static final int STATUS_OFF = 0; //关闭路障（可走）
	public static final int STATUS_IN = 9; //神经猫的位置
	
	public Dot(int x,int y){
		super();
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setXY(int x,int y){
		this.x = x;
		this.y = y;
	}
}
