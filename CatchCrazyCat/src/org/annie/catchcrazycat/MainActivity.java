package org.annie.catchcrazycat;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class MainActivity extends Activity {

	private GameView gameView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gameView = (GameView) findViewById(R.id.gameView);
	}
	//负责创建选项菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflator = new MenuInflater(this);
		//装载R.menu.my_menu对应的菜单，并添加到menu中
		inflator.inflate(R.menu.my_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	//菜单项被单击后的回调方法
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.first:
			GameView.BLOCKS = 20;
			updateView();
			break;
		case R.id.second:
			GameView.BLOCKS = 15;
			updateView();
			break;
		case R.id.third:
			GameView.BLOCKS = 10;
			updateView();
			break;
		case R.id.forth:
			GameView.BLOCKS = 5;
			updateView();
			break;
		case R.id.fifth:
			GameView.BLOCKS = 2;
			updateView();
			break;
		default:
			GameView.BLOCKS = 15;
			updateView();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	private void updateView(){
		gameView.init();
		gameView.redraw();
	}
}
