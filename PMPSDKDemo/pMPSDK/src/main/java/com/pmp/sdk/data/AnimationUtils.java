package com.pmp.sdk.data;

import java.util.Random;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

public class AnimationUtils {
	public final static int ANIMATION_FADE_IN = 1;
	public final static int ANIMATION_FLIP_IN = 2;
    public final static int ANIMATION_SLIDE_IN_BOTTOM = 3;
    public final static int ANIMATION_SLIDE_IN_LEFT = 4;
    public final static int ANIMATION_SLIDE_IN_RIGHT  = 5;
    public final static int ANIMATION_SLIDE_IN_TOP = 6;
    
	
	public static AnimationSet getEnterAnimationSet(int animation) {
		AnimationSet set = new AnimationSet(false);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f,1.0f);
		alphaAnimation.setDuration(3000);
		set.addAnimation(alphaAnimation);
		TranslateAnimation translateAnimation;
		//    	TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
		//    	translateAnimation.setDuration(3000);
		switch (animation) {
		case ANIMATION_FADE_IN:
			return set;
		case ANIMATION_FLIP_IN:
			return set;
		case ANIMATION_SLIDE_IN_BOTTOM:
			translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF,0.0f);
			translateAnimation.setDuration(1000);
			set.addAnimation(translateAnimation);
			return set;
		case ANIMATION_SLIDE_IN_LEFT:
			translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f);
			translateAnimation.setDuration(1000);
			set.addAnimation(translateAnimation);
			return set;
		case ANIMATION_SLIDE_IN_RIGHT:
			translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f);
			translateAnimation.setDuration(1000);
			set.addAnimation(translateAnimation);
			return set;
		case ANIMATION_SLIDE_IN_TOP:
			translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,-1.0f, Animation.RELATIVE_TO_SELF,0.0f);
			translateAnimation.setDuration(1000);
			set.addAnimation(translateAnimation);
			return set;
		default:
			return null;
		}
	}

	public static AnimationSet getExitAnimationSet(int animation) {
		AnimationSet set = new AnimationSet(false);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
		alphaAnimation.setDuration(3000);
		set.addAnimation(alphaAnimation);
		TranslateAnimation translateAnimation;
		//    	TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
		//    	translateAnimation.setDuration(3000);
		switch (animation) {
		case ANIMATION_FADE_IN:
			return set;
		case ANIMATION_FLIP_IN:
			return set;
		case ANIMATION_SLIDE_IN_BOTTOM:
			translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,1.0f);
			translateAnimation.setDuration(1000);
			set.addAnimation(translateAnimation);
			return set;
		case ANIMATION_SLIDE_IN_LEFT:
			translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,-1.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f);
			translateAnimation.setDuration(1000);
			set.addAnimation(translateAnimation);
			return set;
		case ANIMATION_SLIDE_IN_RIGHT:
			translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f);
			translateAnimation.setDuration(1000);
			set.addAnimation(translateAnimation);
			return set;
		case ANIMATION_SLIDE_IN_TOP:
			translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,-1.0f);
			translateAnimation.setDuration(1000);
			set.addAnimation(translateAnimation);
			return set;
		default:
			return null;
		}
	}

	//add by Jas
	public enum TranslateAnimationType{
		RANDOM , LIFT , RIGHT , UP , DOWN , ALAPH, NOAN
	}
	private static TranslateAnimationType GetRandom(){
		Random m = new Random();
		int r = (m.nextInt(10))%4;
		switch(r){
		case 0: return TranslateAnimationType.LIFT;
		case 1: return TranslateAnimationType.RIGHT;
		case 2: return TranslateAnimationType.UP;
		case 3: return TranslateAnimationType.DOWN;
		}
		return TranslateAnimationType.UP;
	}
	public static TranslateAnimation GetTranslateAnimation(TranslateAnimationType m){
		TranslateAnimation fadeInAnimation = null;
		TranslateAnimationType mTranslateAnimationType = m;
		if(mTranslateAnimationType == null)mTranslateAnimationType=TranslateAnimationType.RANDOM;
		if(mTranslateAnimationType == TranslateAnimationType.RANDOM){
			mTranslateAnimationType = GetRandom();
		}
		if(mTranslateAnimationType==TranslateAnimationType.DOWN){
			fadeInAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, -1.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
		}else if(mTranslateAnimationType==TranslateAnimationType.LIFT){
			fadeInAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, +1.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
		}else if(mTranslateAnimationType==TranslateAnimationType.RIGHT){
			fadeInAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, -1.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
		}else {
			fadeInAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, +1.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
		}
		fadeInAnimation.setDuration(1000);
		return fadeInAnimation;
	}
	public static TranslateAnimation GetExitTranslateAnimation(TranslateAnimationType m){
		TranslateAnimation fadeInAnimation = null;
		TranslateAnimationType mTranslateAnimationType = m;
		if(mTranslateAnimationType == null)mTranslateAnimationType=TranslateAnimationType.DOWN;
		if(mTranslateAnimationType == TranslateAnimationType.RANDOM){
			mTranslateAnimationType = GetRandom();
		}
		if(mTranslateAnimationType==TranslateAnimationType.DOWN){
			fadeInAnimation = new TranslateAnimation(/*UP*/
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, -1.0f);
		}else if(mTranslateAnimationType==TranslateAnimationType.LIFT){
			fadeInAnimation = new TranslateAnimation(/*RIGHT*/
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 1.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
		}else if(mTranslateAnimationType==TranslateAnimationType.RIGHT){
			fadeInAnimation = new TranslateAnimation(/*LIFT*/
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, -1.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
		}else{
			fadeInAnimation = new TranslateAnimation(/*DOWN*/
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 1.0f);
		}
		fadeInAnimation.setDuration(1000);
		return fadeInAnimation;
	}
}
