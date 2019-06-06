package com.goodnight.everyone;

import android.os.Bundle;
import android.view.View;
import java.util.List;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.EditText;
import android.graphics.Color;
import android.kz.Base;
import android.kz.Toastkeeper;
import android.kz.KzPermissions;
import android.kz.Permission;
import android.kz.OnPermission;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import android.widget.Button;
import android.os.Handler;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.animation.ObjectAnimator;

public class MainActivity extends Base 
{
	WaveView wv;
	TextView t1,t2;
	Button wn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

		inlayout(R.layout.main);
        super.onCreate(savedInstanceState);
		Bar(R.id.mainView1).setBackgroundColor(Color.BLACK);
		//注意上面的顺序
		screenOff();
		t1=findViewById(R.id.mainTextView1);
		t2=findViewById(R.id.mainTextView2);
		if(getLocation()==null){
			toa("location空了",Toastkeeper.GRAVITY_BOTTOM);
		}else{
			httpGet(0,t1,getpath(getLocation()));
			
		}
		httpGet(1,t2,"https://api.lwl12.com/hitokoto/v1");
		wv=findViewById(R.id.mainWaveView1);
		wn=findViewById(R.id.mainButton1);
		wv.start();
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(findViewById(R.id.mainTextView3), "alpha", 1f, 0f);
		objectAnimator.setDuration(3000);
		objectAnimator.start();
		}
		public void inold(View v){
			Intent in=new Intent(MainActivity.this,old.class);
			startActivity(in);
		}
		public void innight(View v){
			wnstub();
		}
	public void wnstub(){
		
					if(wv.isStarting()){
						wv.stop();
						t1.setAlpha(0);
	                    t2.setAlpha(0);
						wn.setAlpha(0);
						//隐藏一切，变成黑屏
						toa("早睡早起身体好",Toastkeeper.GRAVITY_CENTER);
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									SharedPreferences sharedPreferences= getSharedPreferences("goodnight",Context.MODE_PRIVATE);
									SharedPreferences.Editor editor = sharedPreferences.edit();
									SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//获取当前时间
									Date date = new Date(System.currentTimeMillis());



									//3s后息屏
									uk();
									if(sharedPreferences.contains("size")){
										int newsize=(sharedPreferences.getInt("size",0))+1;
										editor.remove("size");
										editor.commit();
										editor.putInt("size",newsize);
										editor.commit();
										editor.putString(("mark"+newsize),simpleDateFormat.format(date));
										editor.commit();
									}else{
										editor.putInt("size",0);
										editor.commit();
										editor.putString(("mark"+0),simpleDateFormat.format(date));
										editor.commit();


									}
								}
							}, 3000);

						Handler then = new Handler();
						then.postDelayed(new Runnable() {
								@Override
								public void run(){
									//4s后返回桌面，当然这时已经息屏了
									//3s的时候就息屏了，息屏1s后再执行
									//为了防止返回桌面时出现白光影响用户
									rehome(MainActivity.this);
								}
							}, 4000);



					}else{
						//这里是已经动画已经不运行
						finish();
					}
				
		
		
	}
	private void uk(){
		DevicePolicyManager policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		policyManager.lockNow();
	}
	public static void rehome(Base a)	{
		Intent intent = new Intent();
		//返回桌面
		intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
		intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
		a.startActivity(intent);
	}
	
	private void httpGet(final int t,final TextView inn,final String in) {
        //开子线程网络请求
        new Thread(new Runnable() {
				@Override
				public void run() {
					HttpURLConnection connection=null;
					BufferedReader reader=null;
					String urls = in;

					try {
						URL url=new URL(urls);
						connection=(HttpURLConnection) url.openConnection();
						connection.setRequestMethod("GET");
						connection.setReadTimeout(5000);
						connection.setConnectTimeout(5000);
						InputStream in=connection.getInputStream();

						reader = new BufferedReader( new InputStreamReader(in));
						StringBuilder response =new StringBuilder();
						String line;
						while ((line=reader.readLine())!=null){
							response.append(line);
						}
						showResponse2(t,inn,response.toString());
					} catch (IOException e) {
						e.printStackTrace();
					}finally {
						if (reader!=null){
							try {
								reader.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (connection!=null){
							connection.disconnect();
						}
					}


				}
			}).start();
    }

    //切换为主线程
    private void showResponse2(final int t,final TextView in,final String response){
        runOnUiThread(new Runnable() {
				@Override
				public void run() {
if(t==0){
	String ty;
	try {
		ty=new String(response.getBytes(),"UTF-8");
		String neww=getjson((getjson(ty,"regeocode")),"addressComponent");
		String aa=/*getaddress.getjson(neww,"province")+getaddress.getjson(neww,"city")+*/getjson(neww,"district");
		in.setText("晚安 "+aa);
		//title.setText("晚安 "+addressnow());
		//把获取的网页数据赋值给变量response，并设置给TextView控件
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
}else{
			in.setText(""+response);
}
				
				}
			});
    }
	
	public static String getjson(String jd,String in) {
		String b = null;
		try
		{ JSONObject jsonObject = new JSONObject(jd);
			b = jsonObject.getString(in);
			return b;
		}
		catch (Exception e)
		{  e.printStackTrace();
			return e.getMessage();
		}
	}
	
	
	public  Location getLocation() {
        String locationProvider;
        //获取地理位置管理器
        LocationManager locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers == null) return null;
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            //Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
toa("没gps，也没网",Toastkeeper.GRAVITY_RIGHT);
            return null;
        }
        //获取Location
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
			return location;
        } else {
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
            locationManager.requestLocationUpdates(locationProvider, 1000, 0, locationListener);
            location = locationManager.getLastKnownLocation(locationProvider);
            if (location != null) {
                //不为空,显示地理位置经纬度
			}
            return location;
        }
    }
	
	
	
	private String getpath(Location location) {
        String path = "http://restapi.amap.com/v3/geocode/regeo?output=json&location=" + location.getLongitude() + "," + location.getLatitude() + "&key="+"481d6d4a16e4853285f1c3ec4db85b4e";
		return path;
    }
	//判断息屏
	private void screenOff(){
        DevicePolicyManager policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminReceiver = new ComponentName(MainActivity.this, ScreenOffAdminReceiver.class);
        boolean admin = policyManager.isAdminActive(adminReceiver);
        if (admin) {


        } else {
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminReceiver);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启后就可以使用锁屏功能了...");//显示位置见图二
			startActivityForResult(intent, 0);

            toa("没有息屏权限",Toastkeeper.GRAVITY_CENTER);
        }
    }
	void toa(String in,int ty){
		Toastkeeper.getInstance()
			.createBuilder(this)
			.setMessage(in)
			.setGravity(ty)
			.show();
	}
	
	private void show(final String in){
		runOnUiThread(new Runnable() {
                @Override
                public void run() {
					TextView a=findViewById(R.id.mainTextView1);
					a.setText(in);

                }
            });
	}


	public void requestPermission(View view) {
        KzPermissions.with(this)
			//targetSdkVersion要注意，有的权限要大于23，有的要大于26，不满足的话，会log显示的
			.constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
			.permission(Permission.ACCESS_COARSE_LOCATION,Permission.ACCESS_FINE_LOCATION) //支持请求6.0悬浮窗权限8.0请求安装权限
			.permission(Permission.Group.STORAGE, Permission.Group.CALENDAR) //不指定权限则自动获取清单中的危险权限
			.request(new OnPermission() {

				@Override
				public void hasPermission(List<String> granted, boolean isAll) {
					if (isAll) {
						toa("获取权限成功",Toastkeeper.GRAVITY_RIGHT);
					}else {
						toa("获取权限成功，部分权限未正常授予",Toastkeeper.GRAVITY_LEFT);
					}
				}

				@Override
				public void noPermission(List<String> denied, boolean quick) {
					if(quick) {
						toa("被永久拒绝授权，请手动授予权限",Toastkeeper.GRAVITY_CENTER);
						//如果是被永久拒绝就跳转到应用权限系统设置页面
						KzPermissions.gotoPermissionSettings(MainActivity.this);
					}else {
						toa("获取权限失败",Toastkeeper.GRAVITY_BOTTOM);
					}
				}
			});
    }

  
}
