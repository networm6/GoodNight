package com.goodnight.everyone;
import android.kz.Base;
import android.os.Bundle;
import android.graphics.Color;
import android.widget.TextView;
import android.content.SharedPreferences;

public class old extends Base
{
TextView list;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		inlayout(R.layout.old);
        super.onCreate(savedInstanceState);
		Bar(R.id.oldView1).setBackgroundColor(Color.BLACK);
	list=findViewById(R.id.oldTextView1);
		loadArray();
	}
	public  void loadArray() {  //取出    
		SharedPreferences sharedPreferences= getSharedPreferences("goodnight", 0);
		int size = sharedPreferences.getInt("size", 0);    
		for(int i=0;i<size;i++) {  
			list.setText(list.getText().toString()+"\n"+sharedPreferences.getString("mark" + i, "获取失败"));
		}  
	}
}
