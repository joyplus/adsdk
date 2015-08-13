package com.pmp.sdk.mode.inf;

public interface iStateListener {
	
	public boolean onStateChange(int laststate, int newstate);

	public boolean notifyStateChange(int laststate, int newstate);
}
