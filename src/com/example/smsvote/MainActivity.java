package com.example.smsvote;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.R.integer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	final String SMS_URI_INBOX = "content://sms/inbox";
	static int num;
	static int taiNum;
	HashMap<String, Integer> map = new HashMap<String, Integer>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String msg = getSmsInPhone();
		//System.out.println(msg);
	}
	public String getSmsInPhone() {  
        final String SMS_URI_ALL = "content://sms/";  
        final String SMS_URI_INBOX = "content://sms/inbox";  
        final String SMS_URI_SEND = "content://sms/sent";  
        final String SMS_URI_DRAFT = "content://sms/draft";  
        final String SMS_URI_OUTBOX = "content://sms/outbox";  
        final String SMS_URI_FAILED = "content://sms/failed";  
        final String SMS_URI_QUEUED = "content://sms/queued";  
 
        num=0;
        taiNum=0;
        StringBuilder smsBuilder = new StringBuilder();  
  
        try {  
            Uri uri = Uri.parse(SMS_URI_INBOX);  
            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };  
            Cursor cur = getContentResolver().query(uri, projection, null, null, "date desc");      // 获取手机内部短信  
  
            if (cur.moveToFirst()) {
                int index_Body = cur.getColumnIndex("body");  
  
                do {   
                    String strbody = cur.getString(index_Body);  
                    if(strbody.startsWith("vote"))
                    {
                    	num+=1;
                    	if(strbody.length()>14){
                    		String cityString = strbody.substring(5, 7);
                        	String time = strbody.substring(13);
                        	if(cityString.endsWith("太原")) taiNum+=1;
                        	if(map.containsKey(time)){
                        		map.put(time, map.get(time)+1);
                        	}
                        	else {
								map.put(time, 1);
								
							}
                    	}
                    }
                } while (cur.moveToNext());
  
                if (!cur.isClosed()) {  
                    cur.close();  
                    cur = null;  
                }  
            } else {  
                smsBuilder.append("no result!");  
            } // end if  
  
             TextView cityLable = (TextView) findViewById(R.id.citylabel);
             double rate = (double)taiNum/(double)num;
             cityLable.setText("Total: "+num+"\n"+"Taiyuan:"+taiNum+"\n"+"Taiyuan rate:"+rate);
             
             TextView timeLabel = (TextView) findViewById(R.id.timelable);
             String msg ="";
             Iterator it = map.entrySet().iterator();
             while(it.hasNext()){
            	 HashMap.Entry entry = (HashMap.Entry) it.next(); 
            	 String key = (String) entry.getKey(); 
            	 int val = (Integer) entry.getValue(); 
            	 msg +="Date:"+key+" Count:"+val+"\n";
             }
             timeLabel.setText(msg);
  
        } catch (SQLiteException ex) {  
            Log.d("SQLiteException in getSmsInPhone", ex.getMessage());  
        }  
  
        return smsBuilder.toString();  
    }  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
