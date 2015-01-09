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
	private static final int axisX = 4;  //����
	private static final int axisY = 4;  //����
	private int[] matrix = new int[]{
			R.drawable.p01,R.drawable.p02,R.drawable.p03,R.drawable.p04,
			R.drawable.p05,R.drawable.p06,R.drawable.p07,R.drawable.p08,
			R.drawable.p09,R.drawable.p10,R.drawable.p11,R.drawable.p12,
			R.drawable.p13,R.drawable.p14,R.drawable.p15,R.drawable.p16
		};
	private int whiteImage = R.drawable.p16; //�հ�ͼ��
	private ImageCell[][] images = new ImageCell[axisX][axisY];
	private ImageCell target = new ImageCell(getContext());
	private int cellWidth = 70;  //���ó�ʼ�Ŀ��
	private int cellHeight = 100;  //���ó�ʼ�ĸ߶�
	public PuzzleView(Context context) {
		super(context);
		init();
	}
	public PuzzleView(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
	}
	public void init(){
		//����GridLayout������������
		setRowCount(axisX);
		setColumnCount(axisY);
		System.out.println("PuzzleView.init()");
		//ʶ������
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
					//�����ָ��X�᷽���ƶ�
					if(Math.abs(offsetX) > Math.abs(offsetY)){
						if(offsetX < -5){
							//��ָ�����ƶ�
							//����հ׿鲻�����ұߣ�����������ƶ�
							if(target.getCol()<axisY-1){
								target.setSrc(images[target.getRow()][target.getCol()+1].getSrc());
								target = images[target.getRow()][target.getCol()+1];
								target.setSrc(whiteImage);
							}
						}else if(offsetX > 5){
							//��ָ�����ƶ�
							if(target.getCol()>0){
								target.setSrc(images[target.getRow()][target.getCol()-1].getSrc());
								target = images[target.getRow()][target.getCol()-1];
								target.setSrc(whiteImage);
							}
						}
					}else{
						if(offsetY <-5){
							//�����ƶ�
							if(target.getRow()<axisX-1){
								target.setSrc(images[target.getRow()+1][target.getCol()].getSrc());
								target = images[target.getRow()+1][target.getCol()];
								target.setSrc(whiteImage);
							}
						}else if(offsetY > 5){
							//�����ƶ�
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
	 * ����Ļ��С�����ı�ʱ������onSizeChanged����
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
		//����0~14����������飬����ÿ�����ֳ���һ�Σ���15��ſհ�ͼ
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
