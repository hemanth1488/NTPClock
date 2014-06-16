package com.example.ntpclock.util;

import java.util.Date;

public class NtpTime {
private static NtpTime uniqInstance;
int hours;
int min;
int secs;
public void setHours() {
	if(hours>23){
    	
    	hours=0;
    }
}

public void setMin() {
	
	if(min>59){
    	min=0;
    	hours++;
    	
    }
}

public void setSecs() {
	secs++;
	if(secs>59){
    	secs=0;
    	min++;
    	
    }
}


	  private NtpTime() {
	  }

	  public static synchronized NtpTime getInstance() {
	    if (uniqInstance == null) {
	      uniqInstance = new NtpTime();
	    }
	    return uniqInstance;
	  }
	  // other useful methods here
	  public void settime(long date){
		  
		  Date d = new Date(date);
		hours=d.getHours();
		min=d.getMinutes();
		secs=d.getSeconds();
	  }
	  public int getHours(){
		  return hours;
	  }
	  public int getMin(){
		  return min;
	  }
	  public int getSecs(){
		  return secs;
	  }

	@Override
	public String toString() {
		return String.format("%02d", hours)+":"+String.format("%02d", min)+":"+String.format("%02d", secs);
	}
	  
	  
	} 


