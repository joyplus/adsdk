package com.pmp.sdk.mode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.pmp.sdk.data.HttpManager;
import com.pmp.sdk.mode.inf.iStateListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class ReportController{
	 public synchronized boolean addReport(Report report){
		 if(!tearDown && report != null && report.isAviable()){
			 if(report.getType() == Report.TYPE_CLKTRACKING && mCilckReportResource != null){
				 return mCilckReportResource.addReportUri(report);
			 }else if(report.getType() == Report.TYPE_IMPTRACKINT && mImpReportResource != null){
				 return mImpReportResource.addReportUri(report);
			 }
		 }
		 return false;
	 }
	 public synchronized boolean addReport(List<Report> reports){
		 if(reports != null && reports.size() >0){
			 ArrayList<Report> mcliReports = new ArrayList<Report>();
			 ArrayList<Report> mIList      = new ArrayList<Report>();
			 for(Report report : reports){
				 if(report.isAviable()){
					 if(report.getType() == Report.TYPE_CLKTRACKING){
						 mcliReports.add(report);
					 }else if(report.getType() == Report.TYPE_IMPTRACKINT){
						 mIList.add(report);
					 }
				 }
			 }
			 if(!tearDown && mcliReports.size() >0 && mCilckReportResource != null){
				 mCilckReportResource.addReportUri(mcliReports);
			 }
			 if(!tearDown && mIList.size() >0 && mImpReportResource != null){
				 mImpReportResource.addReportUri(mIList);
			 }
			 return true;
		 }
		 return false;
	 }
	 public void tearDown(){
		 if(tearDown)return;
		 tearDown = true;
		 sendMessage(MSG_TEARDOWN);
	 }
	 ////////////////////////////////////////////////////////////
	 private boolean tearDown = false;
	 public ReportController(){
		 HandlerThread ht = new HandlerThread("ReportController");
		 ht.start();
		 mHandler = new MessageHandler(ht.getLooper());
		 sendMessage(MSG_CREATE_REPORTRESOURCE_CONTROLLER);
	 }
	 public ReportController(Looper looper){}
	 
	 private CilckTrackingController mCilckTrackingController;
	 private ImpTrackingController   mImpTrackingController;
	 private ReportResource mCilckReportResource;
	 private ReportResource mImpReportResource;
	 
	 private void tearDownController(Controller controller){
		 if(controller != null){
			 try{
				 controller.changeState(safeThread.THREAD_STATE_STOP);
			 }catch(Throwable e){}
		 }
		 controller = null;
	 }
	 private void tearDownResportResource(ReportResource reportResource){
		 if(reportResource != null){
			 try{
				 reportResource.tearDown();
			 }catch(Throwable e){}
		 }
		 reportResource = null;
	 }
	 private synchronized void createCilckTrackController(){
		 if(mCilckReportResource != null){//it must have resource.
				if(mCilckTrackingController == null
						|| mCilckTrackingController.isNeedReCreate()){
					tearDownController(mCilckTrackingController);
					mCilckTrackingController = new CilckTrackingController(mCilckReportResource);
					mCilckTrackingController.start();
				}
			}
	 }
	 private synchronized void createImpTrackController(){
		 if(mImpReportResource != null){//it must have resource.
			  if(mImpTrackingController == null
					|| mImpTrackingController.isNeedReCreate()){
				 tearDownController(mImpTrackingController);
				 mImpTrackingController = new ImpTrackingController(mImpReportResource);
				 mImpTrackingController.start();
			  }
		  }
	 }
	 private void createController(int type){
		 sendMessage(MSG_CREATE_CONTROLLER, type);
	 }
	 //////////////////////////////////////////////////////////////////
	 // 
	 private MessageHandler mHandler;
	 private synchronized void sendMessage(int what){
		 if(tearDown)return;
		 mHandler.removeMessages(what);
		 mHandler.sendEmptyMessage(what);
	 }
	 private synchronized void sendMessage(int what, int param){
		 if(tearDown)return;
		 mHandler.removeMessages(what);
		 Message m = mHandler.obtainMessage(what);
		 m.arg1 = param;
		 m.sendToTarget();
	 }
	 private final static int MSG_CREATE_REPORTRESOURCE_CONTROLLER = 1;
	 private final static int MSG_CREATE_CONTROLLER     = 2;
	 private final static int MSG_TEARDOWN              = 3;
	 private class MessageHandler extends Handler{
		 public MessageHandler(Looper loop){
			 super(loop);
		 }
		 @Override
		 public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(tearDown) return;//we don't response any commend here.
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_CREATE_REPORTRESOURCE_CONTROLLER:{
				tearDownResportResource(mCilckReportResource);
				tearDownResportResource(mImpReportResource);
				mCilckReportResource = new ReportResource();
				mImpReportResource   = new ReportResource();
				createCilckTrackController();
				createImpTrackController();
			}break;
			case MSG_CREATE_CONTROLLER:{
				int type = msg.arg1;
				if(Report.TYPE_CLKTRACKING == type){
					createCilckTrackController();
				} else if(Report.TYPE_IMPTRACKINT == type){
					createImpTrackController();
				}
			}break;
			case MSG_TEARDOWN:{
				tearDown = true;
				tearDownController(mCilckTrackingController);
				tearDownController(mImpTrackingController);
				tearDownResportResource(mCilckReportResource);
				tearDownResportResource(mImpReportResource);
			}break;
			}
		 }
	 };
	 //////////////////////////////////////////////////////////////
	 private class ImpTrackingController extends Controller{
		 public ImpTrackingController(ReportResource reportResource){
			 super(reportResource);
		 }
		 @Override
		 int getControllerType() {
			// TODO Auto-generated method stub
			return Report.TYPE_IMPTRACKINT;
		 }
	 }
	 private class CilckTrackingController extends Controller{
		 public CilckTrackingController(ReportResource reportResource){
			 super(reportResource);
		 }
		 @Override
		 int getControllerType() {
			// TODO Auto-generated method stub
			return Report.TYPE_CLKTRACKING;
		 }
	 }
	 //////////////////////////////////////////////////////////////////////////////////
	 private abstract class Controller extends safeThread implements iStateListener{
		 abstract int getControllerType();//its same like Report.
		 private ReportResource mReportResource;
		 public Controller(ReportResource reportResource){
			 super();
			 addListener(this);
			 mReportResource = reportResource;
		 }
		 @Override
		 public boolean progress() {
			 // TODO Auto-generated method stub
			 while(getsafeThreadState() == THREAD_STATE_RUNNING){
				 Report report = mReportResource.getReortUri();
				 if(report != null){
					 report.setReported(true);//make sure it can be remove
					 if(getControllerType() == report.getType()){
						 report(report.getUri());//now report it
					 }
				 }
			 }
			 return true;
		 }
		 @Override
		 public boolean onStateChange(int laststate, int newstate) {
			// TODO Auto-generated method stub
			if(newstate == THREAD_STATE_STOP){
				mReportResource.notifyReport();//notify thread this for stop.
				createController(getControllerType());//safethread it will be recreate.
			}
			return false;
		 }
		 @Override
		 public boolean notifyStateChange(int laststate, int newstate) {
			// TODO Auto-generated method stub
			return false;//nothing to do here.
		 } 
		 private boolean report(String string){
			 try{//we don't want to know response
				 android.util.Log.d("PMP","report-->"+string);
				 HttpManager.getHttpResponse(string);
			 }catch(Throwable e){
				 android.util.Log.d("PMP","failed to send tracking");
				 android.util.Log.d("PMP", e.toString());
			 }
			 return true;
		 }
		 public synchronized boolean isNeedReCreate(){
			 int state= getsafeThreadState();
			 return !(state == THREAD_STATE_PENDING || state==THREAD_STATE_RUNNING);
		 }
	 }
	 //////////////////////////////////////////////////////////////
	 // for report resource
	 public class ReportResource{
		 private List<Report> mReportUri = new ArrayList<Report>();
		 private boolean      isUseable  = true;
		 //////////////////////////////////////////////////////////////
		 private Object       mWaitObject  = new Object();
		 private boolean      waitReport(){
			synchronized (mWaitObject) {
				try{
					mWaitObject.wait();
				}catch(Throwable e){
					return false;
				}
				return true;
			}
		 }
		 public void notifyReport(){
			synchronized (mWaitObject) {
				try{
					mWaitObject.notify();
				}catch(Throwable e){}
			}
		 }
		 ////////////////////////////////////////////////////////////////
		 public boolean addReportUri(final Report report){
			 if(report==null)return false;
			 return addReportUri(new ArrayList<Report>(){{add(report);}});
		 }
		 public boolean addReportUri(List<Report> Reports){
			 if(Reports != null && Reports.size() >0){ 
				 for(Report report : Reports){
					 if(report.isAviable() && !report.isReported() && isUseable){
						 mReportUri.add(report);
					 }
				 }
				 notifyReport();
			 }
			 return false;
		 }
		 public synchronized Report getReortUri(){
			 Report report = null;
			 while(isUseable && (report = getOneReportUri())==null){
				 waitReport();
			 }
			 return report;
		 }
		 private Report getOneReportUri(){
			 if(!isUseable)return null;
			 for(Iterator<Report> iterator = mReportUri.iterator(); iterator.hasNext(); ){
				 Report report = iterator.next();
				 if(report.isReported()){
					 iterator.remove();
					 continue;
				 }
				 return report;
			 }
			 return null;
		 }
		 public synchronized void tearDown(){
			 isUseable = false;
			 notifyReport();
		 }
	 }
}
