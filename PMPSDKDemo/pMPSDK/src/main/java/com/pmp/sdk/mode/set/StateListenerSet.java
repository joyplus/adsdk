package com.pmp.sdk.mode.set;

import com.pmp.sdk.mode.inf.iStateListener;

public class StateListenerSet extends ModeListenerSet implements iStateListener{
	
	public final static int FLAG_STATE_CHANGE = 10000;
	
	@Override
	public boolean notifyListener(int flag, Object listener, Object... objects) {
		// TODO Auto-generated method stub
		switch(flag){
		case FLAG_STATE_CHANGE:{
			return ((iStateListener)listener).onStateChange((Integer)objects[0], (Integer)objects[1]);
		}
		}
		return false;
	}

	@Override
	public boolean onStateChange(int laststate, int newstate) {
		// TODO Auto-generated method stub
		return false;//nothing todo here.
	}

	@Override
	public synchronized boolean notifyStateChange(int laststate, int newstate) {
		// TODO Auto-generated method stub
		pendNotifyListener(FLAG_STATE_CHANGE, laststate, newstate);
		return true;
	}

}
