package com.example.smokeapp;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class OpenLog {
	public static final String PREF_KEY_OPEN_LOG= "prefKeyOpenLog";
	private Set<String> openLogSet;
	
	public OpenLog() {
		//openLogSet = utils.getPrefStringSet(PREF_KEY_OPEN_LOG);
		openLogSet = new HashSet<String>();
	}
	
	public void add(long minutes) {
		long timeStamp = System.currentTimeMillis();
		timeStamp -= minutes*60*1000;
		openLogSet.add(String.valueOf(timeStamp));
	}
	
	public void randTest(long max,long total) {
		openLogSet.clear();
		for (int i =0 ;i<total; i++) {
			long min = (long) (Math.random()*(max*24*60-0+1));
			add(min);
		}
	}
	
	public void save() {
		
	}
	
	public float[] get24() {
		float[] timeTable;
		long[] timeStampList = new long[512];
		int cnt = 0;
		long timeStampCur = System.currentTimeMillis();
		long timeStamp24  = timeStampCur-24*60*60*1000;
		float total = timeStampCur - timeStamp24;
		
		for(Iterator<String> iterator = openLogSet.iterator();iterator.hasNext();){  
			String strTime = iterator.next();
            //System.out.print("\n"+strTime);  
            long timeStamp = Long.parseLong(strTime);
            if(timeStamp > timeStamp24) {
            	timeStampList[cnt++] = timeStamp;
            	//System.out.println("\n"+String.valueOf(timeStamp));
            }
        } 
		timeTable = new float[cnt];
		for(int i=0;i<cnt;i++) {
			long tmp = timeStampList[i];
			timeTable[i] = 1 - (tmp - timeStamp24)/total;
			System.out.println("\n"+String.valueOf(timeTable[i]));
		}
		return timeTable;
	}
	
	public float[] get7() {
		float[] openFreq = new float[7];
		float[] openTimes = new float[7];
		long timeStampCur = System.currentTimeMillis();
		long timeStamp7  = timeStampCur-7*24*60*60*1000L;
		float total = 7*24*60*60*1000L;
		for(Iterator<String> iterator = openLogSet.iterator();iterator.hasNext();){  
			String strTime = iterator.next();
            long timeStamp = Long.parseLong(strTime);
            long timeFrom7 = timeStamp - timeStamp7;
            if(timeFrom7 >= 0) {
            	int day = 6 - (int) (timeFrom7/(total/7));
            	openTimes[day]++;
            	//System.out.println("\n"+String.valueOf(timeStamp));
            }
        }
		
		for (int i = 0; i<7; i++) {
			openFreq[i] = openTimes[i]/40;
			//System.out.println("\n"+String.valueOf(openFreq[i]));
		}
		
		return openFreq;
	}
	
	public float[] get90() {
		float[] openFreq = new float[90];
		float[] openTimes = new float[90];
		long timeStampCur = System.currentTimeMillis();
		long timeStamp90  = timeStampCur-90*24*60*60*1000L;
		long total = 90*24*60*60*1000L;
		for(Iterator<String> iterator = openLogSet.iterator();iterator.hasNext();){  
			String strTime = iterator.next();
            long timeStamp = Long.parseLong(strTime);
            long timeFrom90 = timeStamp - timeStamp90;
            if(timeFrom90 >= 0) {
            	int day = 89 - (int) (timeFrom90/(total/90.0));
            	openTimes[day]++;
            	//System.out.println("\n"+String.valueOf(timeStamp));
            }
        }
		
		for (int i = 0; i<90; i++) {
			openFreq[i] = openTimes[i]/40;
			if(openFreq[i] > 1) {
				openFreq[i] = 1;
			}
			if(openFreq[i] < 0) {
				openFreq[i] = 0;
			}
			//System.out.println(String.valueOf(openFreq[i]));
		}
		
		return openFreq;	
	}

}
