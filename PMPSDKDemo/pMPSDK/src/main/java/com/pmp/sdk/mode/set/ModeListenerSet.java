package com.pmp.sdk.mode.set;

import java.util.Iterator;


public abstract class ModeListenerSet extends ListenerSet{
	
	public void pendNotifyListener(int flag,Object...objects){
        synchronized (mListeners){
            for(Iterator<Object> iterator=mListeners.iterator(); iterator.hasNext(); ){
                 if(notifyListener(flag,iterator.next(),objects))break;
            }
        }
    }
    public abstract boolean notifyListener(int flag, Object listener, Object...objects);
}
