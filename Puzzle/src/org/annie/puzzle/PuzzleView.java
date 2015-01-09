package org.annie.puzzle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

public class PuzzleView extends GridLayout {
	private static final int axisX = 4;  //行数
	private static final int axisY = 4;  //列数
	private int[] matrix = new int[]{
			R.drawable.p01,R.drawable.p02,R.drawable.p03,R.drawable.p04,
			R.drawable.p05,R.drawable.p06,R.drawable.p07,R.drawable.p08,
			R.drawable.p09,R.drawable.p10,R.drawable.p11,R.drawable.p12,
			R.drawable.p13,R.drawable.p14,R.drawable.p15,R.drawable.p16
		};
	private int whiteImage = R.drawable.p16; //空白图形
	private ImageCell[][] images = new ImageCell[axisX][axisY];
	private ImageCell target = new ImageCell(getContext());
	private int cellWidth = 70;  //设置初始的宽度
	private int cellHeight = 100;  //设置初始的高度
	public PuzzleView(Context context) {
		super(context);
		init();
	}
	public PuzzleView(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
	}
	public void init(){
		//设置GridLayout的行数和列数
		setRowCount(axisX);
		setColumnCount(axisY);
		System.out.println("PuzzleView.init()");
		//识别手势
		setOnTouchListener(new OnTouchListener() {
			private float startX,startY,offsetX,offsetY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					offsetX = event.getX() - startX;
					offsetY = event.getY() - startY;
					//如果手指在X轴方向移动
					if(Math.abs(offsetX) > Math.abs(offsetY)){
						if(offsetX < -5){
							//手指向左移动
							//如果空白块不在最右边，则可以向左移动
							if(target.getCol()<axisY-1){
								target.setSrc(images[target.getRow()][target.getCol()+1].getSrc());
								target = images[target.getRow()][target.getCol()+1];
								target.setSrc(whiteImage);
							}
						}else if(offsetX > 5){
							//手指向右移动
							if(target.getCol()>0){
								target.setSrc(images[target.getRow()][target.getCol()-1].getSrc());
								target = images[target.getRow()][target.getCol()-1];
								target.setSrc(whiteImage);
							}
						}
					}else{
						if(offsetY <-5){
							//向上移动
							if(target.getRow()<axisX-1){
								target.setSrc(images[target.getRow()+1][target.getCol()].getSrc());
								target = images[target.getRow()+1][target.getCol()];
								target.setSrc(whiteImage);
							}
						}else if(offsetY > 5){
							//向下移动
							if(target.getRow()>0){
								target.setSrc(images[target.getRow()-1][target.getCol()].getSrc());
								target = images[target.getRow()-1][target.getCol()];
								target.setSrc(whiteImage);
							}
						}
					}
				}
				return true;
			}
		});	
	}
	/**
	 * 当屏幕大小发生改变时，调用onSizeChanged方法
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		System.out.println("PuzzleView.onSizeChanged()");
		cellWidth = (Math.min(w, h))/4;
		if(cellWidth*10/7 <= Math.max(w, h)){			
			cellHeight = cellWidth*10/7;
		}else{
			cellHeight = Math.max(w, h);
			cellWidth = cellHeight*7/10;
		}
		System.out.println("("+cellWidth+","+cellHeight+")");
		addCells();
	}
	private void addCells(){
		//生成0~14的随机数数组，其中每个数字出现一次，第15格放空白图
		int[] subs = new int[matrix.length];
		boolean exist;
		int i=0;
		while(true){
			exist=false;
			int sub=(int) (Math.random()*(matrix.length-1));
			for(int j=0;j<i;j++){
				if(sub == subs[j]){
					exist=true;
					break;
				}
			}
			if(!exist){
				subs[i]=sub;
				i++;
				if(i>=matrix.length-1)
					break;
			}
		}
		subs[matrix.length-1] = matrix.length-1;
		
		ImageCell ic;
		for(int x=0;x<axisX;x++){
			for(int y=0;y<axisY;y++){				
				ic = new ImageCell(getContext());
				ic.setRow(x);
				ic.setCol(y);
				ic.setSrc(matrix[subs[x*axisX+y]]);
				addView(ic, cellWidth, cellHeight);
				images[x][y]=ic;
			}
		}
		target = images[axisX-1][axisY-1];
	}
}
