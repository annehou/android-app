package org.annie.catchcrazycat;

/**
 * �����ϵĵ����
 * @author lenovo
 *
 */
public class Dot {
	private int x,y; //x�����У�y������(�������᲻�غ�)
	private int status; //ÿ�����״̬����·�ϣ���·�ϣ�����è��
	
	public static final int COLOR_ON = 0xFFFF835D; //·�Ͽ���ʱ�������ɫ
	public static final int COLOR_OFF = 0xFFB5B5B5; //·�Ϲر�ʱ�������ɫ
	public static final int STATUS_ON = 1; //����·�ϣ������ߣ�
	public static final int STATUS_OFF = 0; //�ر�·�ϣ����ߣ�
	public static final int STATUS_IN = 9; //��è��λ��
	
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
