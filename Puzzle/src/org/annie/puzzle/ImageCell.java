package org.annie.puzzle;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ImageCell extends FrameLayout {

	private int row;  //�к�
	private int col;  //�к�
	private int src = R.drawable.p16;  //ͼƬ��Դ,Ĭ��Ϊ��ɫ
	private ImageView image; //ͼƬ
	public ImageCell(Context context) {
		super(context);
		
		image = new ImageView(getContext());
		image.setBackgroundResource(src);
		
		//���������������
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.setMargins(2, 2, 0, 0);
		addView(image,lp);
		
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getSrc() {
		return src;
	}
	public void setSrc(int src) {
		this.src = src;
		image.setBackgroundResource(src); //���ñ���ͼƬ
	}
}
