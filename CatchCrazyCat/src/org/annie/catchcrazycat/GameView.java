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
 * ʵ����è��Χ�¹���
 * @author lenovo
 *
 */
public class GameView extends SurfaceView implements OnTouchListener{
	private static int WIDTH = 40;//ÿ��Ԫ�صĿ��Ϊ��ʮ������
	private static final int ROW = 9;
	private static final int COL = 9;
	public static int BLOCKS = 15;//Ĭ����ӵ�·������
	
	private Dot[][] dots; //9*9�ĵ�����
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
				//��ȫ����ʼ��Ϊ�ر�·��
				dot.setStatus(Dot.STATUS_OFF);
				dots[x][y] = dot;
						
			}
		}	
		cat = new Dot(4,4); //��è�ĳ�ʼ��λ��
		cat.setStatus(Dot.STATUS_IN); //��è��״̬
		dots[4][4]=cat;
		//���ó�ʼ��·��
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
	 * ������Ļ�Ǵ����˷���
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//����ͷ�
		if(event.getAction() == MotionEvent.ACTION_UP){
			int x,y;
			x=(int) (event.getY()/WIDTH);
			//������
			if(x%2==0){
				y=(int) (event.getX()/WIDTH);
			}else{
				y=(int) ((event.getX()-WIDTH/2)/WIDTH);
			}
			//�����������棬���ʼ����Ϸ
			if(x+1>COL || y+1>ROW){
				init();
			}else if(dots[x][y].getStatus()==Dot.STATUS_OFF){
				dots[x][y].setStatus(Dot.STATUS_ON);
				move();
			}
			//�ػ�
			redraw();
		}
		return true;
	}
	/**
	 * �߲�����
	 */
	private void move() {
		//���è�ڱ߽磬��Ϸʧ��
		if(isAtEdge(cat)){
			alert("��Ϸʧ��",R.drawable.lost,"���¿�ʼ");
			return;
		}
		Vector<Dot> avaliable = new Vector<Dot>(); //���ϰ��ڽӵ�
		Vector<Dot> positive = new Vector<Dot>(); //���߽����ϰ��ڽӵ�
		HashMap<Dot,Integer> al = new HashMap<Dot, Integer>();
		//��è�߲��ķ����1~6
		for(int d=1;d<7;d++){
			Dot neib = getNeighbour(cat,d);
			if(neib.getStatus()==Dot.STATUS_OFF){
				int dis = getDistance(cat, d); //��ȡ��è��d����Ŀ��߲���
				if(bestPath)
					positive.add(neib);
				else
					avaliable.add(neib);
				al.put(neib, dis);
			}
		}
		Dot best=null;
		//û�п���·�����ɹ���èΧס
		if(avaliable.size()==0&&positive.size()==0){
			alert("��Ϸ�ɹ�",R.drawable.success,"���¿�ʼ");
		}else if(positive.size()!=0){
			int min=999;
			//��ȡ�����Ե·������̵�һ��
			for(int i=0;i<positive.size();i++){
				int a=al.get(positive.get(i)); //��ȡ���߽����̾���
				if(a<min){
					min=a;
					best=positive.get(i);
				}
			}
		}else if(avaliable.size()!=0){
			//���з��򶼴���·��
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
	 * ��è�´��ƶ����ĵ�
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
		//���è�ڱ߽磬��Ϸʧ��
		if(isAtEdge(nextDot)){
			alert("��Ϸʧ��",R.drawable.lost,"���¿�ʼ");
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
	 * ��ȡ��ǰ�㵽�߽���ϰ���ľ���
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
	 * ��õ�ǰ�����ڽӵ�
	 * @param one 
	 * @param dir �ڽӵ�ķ���
	 * @return �ھӽ��
	 */
	private Dot getNeighbour(Dot one, int dir) {
		switch (dir) {
		case 1:
			if(one.getX()<0)
				return null;
			//������
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
			//������
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
			//������
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
			//������
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
	 * �ж�dot�Ƿ����߲�����ı߽�
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
	 * �ػ�SurfaceView
	 */
	public void redraw(){
		Canvas c = getHolder().lockCanvas();//��������SurfaceView��Canvas
		c.drawColor(0xff666666); //���ñ���ɫΪ��ɫ
		Paint paint = new Paint();
		paint.setAntiAlias(true); //�����
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
		//����������������canvas�����ݸ��µ�������
		getHolder().unlockCanvasAndPost(c);
	}
}
