package org.annie.g2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 游戏中卡片类
 * @author lenovo
 *
 */
public class Card extends FrameLayout {
	private int cardWidth = 0;
	private int cardHeight = 0;
	private TextView label;
	private int num = 0;
	private int bgcolor = 0x33ffffff;
	private int[] bgcolors = new int[]{
			0xfffeefdc,0xfffee7bb,0xffff8f6a,0xfffe7251,0xffff6241,
			0xfffe4600,0xffeedf82,0xfff2d04a,0xfff0c950,0xfff0c441,
			0xffebc330,0xffff0000
	};
	
	
	public Card(Context context) {
		super(context);
		
		label = new TextView(getContext());
		label.setTextSize(32);
		label.setBackgroundColor(bgcolor);
		label.setGravity(Gravity.CENTER);
		
		//填充满整个父容器
		LayoutParams lp = new LayoutParams(-1, -1); 
		//左，上部留有10dp间距
		lp.setMargins(10, 10, 0, 0); 
		addView(label, lp);
		
		setNum(0);
	}

	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
		
		if(num<=0){
			label.setText("");
		}else{
			label.setText(num+"");
		}
	}
	public int getCardWidth() {
		return cardWidth;
	}

	public void setCardWidth(int cardWidth) {
		this.cardWidth = cardWidth;
	}

	public int getCardHeight() {
		return cardHeight;
	}

	public void setCardHeight(int cardHeight) {
		this.cardHeight = cardHeight;
	}

	public int getBgcolor() {
		return bgcolor;
	}

	public void setBgcolor(int index) {
		//对于大于2048的网格，背景颜色统一为红色
		if(index==-1){
			label.setBackgroundColor(this.bgcolor);
		}else{
			if(index>=11)
				index=11;
			label.setBackgroundColor(bgcolors[index]);
		}
	}
	
	/**
	 * 判断两张卡片是否可以折叠
	 * @param other
	 * @return
	 */
	public boolean equals(Card other){
		return getNum() == other.getNum(); //数字相等，即可折叠
	}
}
