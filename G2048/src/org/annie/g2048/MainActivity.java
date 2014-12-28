package org.annie.g2048;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity implements OnClickListener{

	private int score=0;
	private int maxs=0;
	private GameView gamePanel;
	private TextView tvScore;
	private TextView maxScore;
	private Button replay;
	private Button sound;
	private boolean soundOn; //音效打开
	private static MainActivity mainActivity=null;
	
	//1、保存用户的最高分数；2、保存用户对音效的设置
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	//播放音效的SoundPool
	SoundPool soundPool = new SoundPool(2,AudioManager.STREAM_SYSTEM,8);
	private int dis;
	
	public static MainActivity getMainActivity() {
		return mainActivity;
	}
	public static void setMainActivity(MainActivity mainActivity) {
		MainActivity.mainActivity = mainActivity;
	}
	public MainActivity(){
		mainActivity = this;
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //游戏开始
        //获取只能被本应用程序读、写的2048游戏的设置文件
        preferences = getSharedPreferences("setting2048", MODE_WORLD_READABLE);
        editor = preferences.edit();
        //读取最高分数，若不存在则为0
        maxs = preferences.getInt("maxscore", 0);
        //读取游戏音效状态，默认为开
        soundOn = preferences.getBoolean("soundon", true); 
        maxScore = (TextView) findViewById(R.id.maxScore);
        //设置最高分数的TextView的文本
        maxScore.setText(maxs+"");
        
        sound = (Button) findViewById(R.id.onoroff);
        //加载音效文件
        dis=soundPool.load(this, R.raw.dis, 1);
        
        gamePanel = (GameView) findViewById(R.id.gamePanel);
        //获取分数TextView，并设置初始值为0
        tvScore = (TextView) findViewById(R.id.score);
        tvScore.setText("0");
        replay = (Button) findViewById(R.id.replay);       
        //监听事件
        replay.setOnClickListener(this);
        sound.setOnClickListener(this);
    }
    /**
     * 当应用程序结束或销毁时，保存最高分数和音效的状态
     */
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	editor.putInt("maxscore", maxs);
    	editor.putBoolean("soundon", soundOn);
    	editor.commit();
    }
    /**
     * 当页面不可见时，保存最高分数和音效的状态
     */
    @Override
    protected void onStop() {
    	super.onStop();
    	editor.putInt("maxscore", maxs);
    	editor.putBoolean("soundon", soundOn);
    	editor.commit();
    }
    public void clearScore(){
    	score=0;
    	showScore();
    }
	public void showScore() {
		//若音效是打开的，则播放音效
		if(soundOn)
			soundPool.play(dis, 1, 1, 0, 0, 1);
		tvScore.setText(score+"");
		//如果最大分数小于当前分数，则把最大分数设为当前分数
		if(maxs<score){
			maxs=score;
			maxScore.setText(maxs+"");
		}
	}
	public void addScore(int s){
		score+=s;
		showScore();
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		//重新开始游戏
		case R.id.replay:
			gamePanel.startGame(); 
			break;
		case R.id.onoroff:
			soundOn = !soundOn; //改变音效状态
			if(soundOn)
				sound.setText("声音    开");
			else
				sound.setText("声音    关");
			break;
		}
	} 
	
}
