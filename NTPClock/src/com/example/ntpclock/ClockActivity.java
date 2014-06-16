package com.example.ntpclock;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ntpclock.util.CustomAnalogClock;
import com.example.ntpclock.util.NtpTime;
import com.example.ntpclock.util.SntpClient;

public class ClockActivity extends Activity {
	    long now;
	    TextView digclock;
	    TextView countdown;
	    
	    CustomAnalogClock clock;
	    static int cnthours;
	    static int cntmin;
	    static int cntsec;
	    private Handler handler = new Handler();
	    NtpTime time = NtpTime.getInstance( );
	    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        countdown=(TextView)findViewById(R.id.counttime);
        
        handler.post(updateServerRunnable);
       
        
      
    }
      
    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
        	SntpClient client = new SntpClient();
        	  if (client.requestTime("0.ubuntu.pool.ntp.org",5000)&&(isOnline())) {
        		  
        		
        	      now = client.getNtpTime() + SystemClock.elapsedRealtime() - client.getNtpTimeReference();
        	     
        	  }
        	  else{
        		  
        		  
        		  now=System.currentTimeMillis() % 1000;
        	  }
        	  time.settime(now);
        	  
        	  
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
                     digclock=(TextView)findViewById(R.id.digitime);
            
            handler.post(updateTextRunnable);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.clock, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_sync) {
        	handler.removeCallbacks(updateServerRunnable);
        	handler.post(updateServerRunnable);
        	
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

  
    private void  updatetime()
    {
        
        time.setSecs();
        time.setMin();
        time.setHours();
           digclock.setText(time.toString());
        }        

    Runnable updateTextRunnable = new Runnable() {
		public void run() {
			updatetime();
			updatecountdown();
			handler.postDelayed(this, 1000);
		}

		
	};
	private void updatecountdown() {
		// TODO Auto-generated method stub
		cntsec--;
        if(cntsec<0){
        	cntsec=59;
        	cntmin--;
        	
        }
        if(cntmin<0){
        	cntmin=59;
        	cnthours--;
        	
        }
        if(cnthours<0){
        	
        	cnthours=0;
        }
           String time=String.format("%02d", cnthours)+":"+String.format("%02d", cntmin)+":"+String.format("%02d", cntsec);
           countdown.setText(time);
           
           clock=(CustomAnalogClock)findViewById(R.id.clock);
           clock.changetime();
		
	}
	
	Runnable updateServerRunnable = new Runnable() {
		public void run() {
			handler.removeCallbacks(updateTextRunnable);
			int interval=Integer.parseInt(getResources().getString(R.string.sync_interval));
			cnthours=((interval / (1000*60*60)) % 24);
			cntmin=((interval / (1000*60)) % 60);
			cntsec=(interval/1000)%60;
			 new LongOperation().execute("");
			handler.postDelayed(this, interval);
		}
	};
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
}
