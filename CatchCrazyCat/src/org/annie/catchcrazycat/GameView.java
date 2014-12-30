package org.annie.catchcrazycat;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * 实现神经猫的围堵功能
 * @author lenovo
 *
 */
public class GameView extends SurfaceView implements OnTouchListener{
	private static int WIDTH = 40;//每个元素的宽度为四十个像素
	private static final int ROW = 9;
	private static final int COL = 9;
	public static int BLOCKS = 15;//默认添加的路障数量
	
	private Dot[][] dots; //9*9的点数组
	private Dot cat;
	private boolean bestPath=false;

	public GameView(Context context) {
		super(context);	
		getHolder().addCallback(callback);
		dots = new Dot[ROW][COL];
		setOnTouchListener(this);
		init();
	}
	public GameView(Context context,AttributeSet attrs){
		super(context,attrs);
		getHolder().addCallback(callback);
		dots = new Dot[ROW][COL];
		setOnTouchListener(this);
		init();
	}
	public void init(){			
		Dot dot=null;
		for(int x=0;x<ROW;x++){
			for(int y=0;y<COL;y++){
				dot = new Dot(x,y);
				//点全部初始化为关闭路障
				dot.setStatus(Dot.STATUS_OFF);
				dots[x][y] = dot;
						
			}
		}	
		cat = new Dot(4,4); //神经猫的初始化位置
		cat.setStatus(Dot.STATUS_IN); //神经猫的状态
		dots[4][4]=cat;
		//设置初始的路障
		Random random = new Random();
		for(int i=0;i<BLOCKS;){
			int x = random.nextInt(ROW);
			int y = random.nextInt(COL);
			if(dots[x][y].getStatus() == Dot.STATUS_OFF){
				dots[x][y].setStatus(Dot.STATUS_ON);
				i++;
			}
		}
	}
	Callback callback = new Callback() {		
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
		}	
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			redraw();
		}		
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			WIDTH = width/(ROW+1);
			redraw();
		}
	};

	/**
	 * 触碰屏幕是触发此方法
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//点击释放
		if(event.getAction() == MotionEvent.ACTION_UP){
			int x,y;
			x=(int) (event.getY()/WIDTH);
			//奇数行
			if(x%2==0){
				y=(int) (event.getX()/WIDTH);
			}else{
				y=(int) ((event.getX()-WIDTH/2)/WIDTH);
			}
			//如果点击到外面，则初始化游戏
			if(x+1>COL || y+1>ROW){
				init();
			}else if(dots[x][y].getStatus()==Dot.STATUS_OFF){
				dots[x][y].setStatus(Dot.STATUS_ON);
				move();
			}
			//重绘
			redraw();
		}
		return true;
	}
	/**
	 * 走步函数
	 */
	private void move() {
		//如果猫在边界，游戏失败
		if(isAtEdge(cat)){
			alert("游戏失败",R.drawable.lost,"重新开始");
			return;
		}
		Vector<Dot> avaliable = new Vector<Dot>(); //无障碍邻接点
		Vector<Dot> positive = new Vector<Dot>(); //到边界无障碍邻接点
		HashMap<Dot,Integer> al = new HashMap<Dot, Integer>();
		//神经猫走步的方向从1~6
		for(int d=1;d<7;d++){
			Dot neib = getNeighbour(cat,d);
			if(neib.getStatus()==Dot.STATUS_OFF){
				int dis = getDistance(cat, d); //获取神经猫在d方向的可走步数
				if(bestPath)
					positive.add(neib);
				else
					avaliable.add(neib);
				al.put(neib, dis);
			}
		}
		Dot best=null;
		//没有可走路径，成功把猫围住
		if(avaliable.size()==0&&positive.size()==0){
			alert("游戏成功",R.drawable.success,"重新开始");
		}else if(positive.size()!=0){
			int min=999;
			//获取到达边缘路径中最短的一条
			for(int i=0;i<positive.size();i++){
				int a=al.get(positive.get(i)); //获取到边界的最短距离
				if(a<min){
					min=a;
					best=positive.get(i);
				}
			}
		}else if(avaliable.size()!=0){
			//所有方向都存在路障
			int max=0;
			for(int i=0;i<avaliable.size();i++){
				int k=al.get(avaliable.get(i));
				if(k>max){
					max=k;
					best=avaliable.get(i);
				}
			}			
		}
		moveTo(best);
	}
	/**
	 * 神经猫下次移动到的点
	 * @param dot
	 */
	private void moveTo(Dot nextDot) {
		if(nextDot==null){
			return;
		}
		//System.out.println(nextDot.getX()+","+nextDot.getY());
		nextDot.setStatus(Dot.STATUS_IN);
		cat.setStatus(Dot.STATUS_OFF);
		cat=nextDot;
		//如果猫在边界，游戏失败
		if(isAtEdge(nextDot)){
			alert("游戏失败",R.drawable.lost,"重新开始");
			return;
		}
	}
	private void alert(String title,int iconId,String content){
		new AlertDialog.Builder(getContext())
		.setTitle(title)
		.setIcon(iconId)
		.setPositiveButton(content, new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				init();
				redraw();
			}
		}).show();
	}
	/**
	 * 获取当前点到边界或障碍点的距离
	 * @param neib
	 * @param dir
	 * @return
	 */
	private int getDistance(Dot cat2, int dir) {
		bestPath=false;
		int distance=0;
		Dot pre=cat2;
		Dot next;
		while(true){
			next=getNeighbour(pre, dir);
			if(next.getStatus()==Dot.STATUS_ON)
				return distance;
			if(isAtEdge(next)&&next.getStatus()==Dot.STATUS_OFF){
				bestPath=true;
				distance++;
				return distance;
			}
			distance++;
			pre = next;
		}
	}
	/**
	 * 获得当前结点的邻接点
	 * @param one 
	 * @param dir 邻接点的方向
	 * @return 邻居结点
	 */
	private Dot getNeighbour(Dot one, int dir) {
		switch (dir) {
		case 1:
			if(one.getX()<0)
				return null;
			//奇数行
			if(one.getX()%2==0){
				if(one.getY()-1<0)
					return null;
				return dots[one.getX()-1][one.getY()-1];
			}
			else{
				return dots[one.getX()-1][one.getY()];
			}
		case 2:
			if(one.getX()<0)
				return null;
			//奇数行
			if(one.getX()%2==0)
				return dots[one.getX()-1][one.getY()];
			else{
				if(one.getY()+1>=COL)
					return null;
				return dots[one.getX()-1][one.getY()+1];
			}
		case 3:
			if(one.getY()+1>=COL)
				return null;
			return dots[one.getX()][one.getY()+1];
		case 4:
			if(one.getX()+1>=ROW)
				return null;
			//奇数行
			if(one.getX()%2==0)
				return dots[one.getX()+1][one.getY()];
			else{
				if(one.getY()+1>=COL)
					return null;
				return dots[one.getX()+1][one.getY()+1];
			}
		case 5:
			if(one.getX()+1>=ROW)
				return null;
			//奇数行
			if(one.getX()%2==0){
				if(one.getY()-1<0)
					return null;
				return dots[one.getX()+1][one.getY()-1];
			}
			else
				return dots[one.getX()+1][one.getY()];
		case 6:
			if(one.getY()-1<0)
				return null;
			return dots[one.getX()][one.getY()-1];
		}
		return null;
	}
	/**
	 * 判断dot是否在走步矩阵的边界
	 * @param d
	 * @return
	 */
	private boolean isAtEdge(Dot d){
		if(d.getX()*d.getY()==0 || d.getX()+1==ROW ||
				d.getY()+1==COL){
			return true;
		}
		return false;
	}
	/**
	 * 重绘SurfaceView
	 */
	public void redraw(){
		Canvas c = getHolder().lockCanvas();//锁定整个SurfaceView的Canvas
		c.drawColor(0xff666666); //设置背景色为灰色
		Paint paint = new Paint();
		paint.setAntiAlias(true); //抗锯齿
		for(int x=0;x<ROW;x++){
			int offset=0;
			if(x%2!=0)
				offset = WIDTH/2;
			for(int y=0;y<COL;y++){
				switch (dots[x][y].getStatus()) {
				case Dot.STATUS_OFF:
					paint.setColor(Dot.COLOR_OFF);
					break;
				case Dot.STATUS_ON:
					paint.setColor(Dot.COLOR_ON);
					break;
				case Dot.STATUS_IN:
					paint.setColor(0xFFFFFFFF);
					break;
				}
				c.drawOval(new RectF(y*WIDTH+offset, x*WIDTH, (y+1)*WIDTH+offset, (x+1)*WIDTH), paint);
			}
		}
		//解除界面的锁定，把canvas的内容更新到界面上
		getHolder().unlockCanvasAndPost(c);
	}
}
