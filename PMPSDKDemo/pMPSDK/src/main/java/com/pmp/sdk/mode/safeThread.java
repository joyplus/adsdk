package com.pmp.sdk.mode;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.atomic.AtomicInteger;

import com.pmp.sdk.mode.inf.iListener;
import com.pmp.sdk.mode.inf.iStateListener;
import com.pmp.sdk.mode.set.StateListenerSet;

public abstract class safeThread extends Thread implements UncaughtExceptionHandler, iListener{
	private StateListenerSet mStateListenerSet = new StateListenerSet();
	public final static int THREAD_STATE_IDLE    = 10000;
	public final static int THREAD_STATE_PENDING = 10001;
	public final static int THREAD_STATE_RUNNING = 10002;
	public final static int THREAD_STATE_WAITING = 10003;
	public final static int THREAD_STATE_STOP    = 10004;
	private AtomicInteger mCurrentState = new AtomicInteger(THREAD_STATE_IDLE);
	public safeThread(){
		safeThread.this.setUncaughtExceptionHandler(safeThread.this);
		changeState(THREAD_STATE_PENDING);
	}
	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		changeState(THREAD_STATE_STOP);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		changeState(THREAD_STATE_RUNNING);
		progress();
		changeState(THREAD_STATE_STOP);
	}
	abstract public boolean progress();
	@Override
	public boolean addListener(Object listener) {
		// TODO Auto-generated method stub
		if(listener instanceof iStateListener){
			return mStateListenerSet.addListener(listener);
		}
		return false;
	}
	@Override
	public boolean removeListener(Object listener) {
		// TODO Auto-generated method stub
		if(listener instanceof iStateListener){
			return mStateListenerSet.removeListener(listener);
		}
		return false;
	}
	public synchronized boolean changeState(int state){
		if(mCurrentState.get() == state)return false;
		int laststate = mCurrentState.get();
		mCurrentState.set(state);
		return mStateListenerSet.notifyStateChange(laststate, state);
	}
	public int getsafeThreadState(){
		return mCurrentState.get();
	}
}
