package com.example.weixin6_0;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActionBar;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.os.Build;

public class MainActivity extends FragmentActivity implements android.view.View.OnClickListener,
	OnPageChangeListener
{
	
	private ViewPager viewPager;
	private List<Fragment> tabs = new ArrayList<Fragment>();
	private String []titles = new String[] {
			"First Fragment !", "Second Fragment !", "Third Fragment !",
			"Fourth Fragment !"
	};
	private FragmentPagerAdapter pagerAdapter;
	
	private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//��ʾoverflow��ť
		seOverflowButtonAlways();
		//����ActionBar�Ƿ���ʾӦ�ó����ͼ��,false����ʾ
		getActionBar().setDisplayShowHomeEnabled(false);
		
		initView();
		initDatas();
		viewPager.setAdapter(pagerAdapter);
		initEvent();
	}


	private void initDatas() {
		for(String title : titles){
			TabFragment tabFragment = new TabFragment();
			Bundle bundle = new Bundle();
			bundle.putString(TabFragment.TITLE, title);
			tabFragment.setArguments(bundle);
			tabs.add(tabFragment);
		}
		pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {			
			@Override
			public int getCount() {
				return tabs.size();
			}		
			@Override
			public Fragment getItem(int arg0) {
				return tabs.get(arg0);
			}
		};
		
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		
		ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.indicator_one);
		mTabIndicators.add(one);
		ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.indicator_two);
		mTabIndicators.add(two);
		ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.indicator_three);
		mTabIndicators.add(three);
		ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.indicator_four);
		mTabIndicators.add(four);
		
		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
		
		one.setIconAlpha(1.0f);
	}
	private void initEvent() {
		viewPager.setOnPageChangeListener(this);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/**
	 * ���ܣ�����overflow��ť��Զ�ɼ����ֻ���û��Menu����overflow��ť�ɼ����ֻ�����Menu����overflow��ť���ɼ���
	 * ��ViewConfiguration���У���̬����sHasPermanentMenuKey��ֵ���ж��ֻ���û������Menu��������ͨ�������޸�
	 * ����ֵ��������ԶΪfalse������overflow��ť��Զ�ɼ�
	 */
	private void seOverflowButtonAlways(){
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKey = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			menuKey.setAccessible(true);
			menuKey.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * ��д��һ��onMenuOpened()��������overflow��չ����ʱ��ͻ�ص��������
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
			if(menu.getClass().getSimpleName().equals("MenuBuilder")){
				try {
					Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible",
							Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public void onClick(View v) {
		
		clickTab(v);
	}

	/**
	 * ���Tab��ť
	 * @param v
	 */
	private void clickTab(View v) {
		resetOtherTabs();	
		switch (v.getId()) {
		case R.id.indicator_one:
			mTabIndicators.get(0).setIconAlpha(1.0f);
			viewPager.setCurrentItem(0,false);
			break;
		case R.id.indicator_two:
			mTabIndicators.get(1).setIconAlpha(1.0f);
			viewPager.setCurrentItem(1,false);
			break;
		case R.id.indicator_three:
			mTabIndicators.get(2).setIconAlpha(1.0f);
			viewPager.setCurrentItem(2,false);
			break;
		case R.id.indicator_four:
			mTabIndicators.get(3).setIconAlpha(1.0f);
			viewPager.setCurrentItem(3,false);
			break;
		}
	}

	/**
	 * ����������TabIndicator����ɫ
	 */
	private void resetOtherTabs() {
		
		for(int i=0;i<mTabIndicators.size();i++){
			mTabIndicators.get(i).setIconAlpha(0);
		}
	}


	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		//Log.e("TAG", "position="+position);
		if(positionOffset>0){
			ChangeColorIconWithText left = mTabIndicators.get(position);
			ChangeColorIconWithText right = mTabIndicators.get(position+1);
			left.setIconAlpha(1-positionOffset);
			right.setIconAlpha(positionOffset);
		}
	}


	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}

}

































