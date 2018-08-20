package com.shuweikeji.qrcode.qrcode.decoding;

import android.app.Activity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Author:   dfl
 * Date :    2015-08-03
 * Description:延迟0.3秒执行任务
 */
public final class InactivityTimer {
    /** 延迟时间0.3秒 */
	private static final int INACTIVITY_DELAY_SECONDS = 5 * 60;
	
    /**
     * 执行延迟任务的线程池，并且对线程进行守护
     */
	private final ScheduledExecutorService inactivityTimer = Executors
			.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
	/** Activity上下文实例 */
	private final Activity activity;
	/** 延迟任务的返回结果Future */
	private ScheduledFuture<?> inactivityFuture = null;
    /**
     * 构造方法，根据Activity实例，初始化InactivityTimer，初始化时执行延迟异步任务。
     * @param activity Activity实例
     */
	public InactivityTimer(Activity activity) {
		this.activity = activity;
		//执行延迟异步任务
		onActivity();
	}
    
	/**
	 * InactivityTimer被初始化时开始执行
	 */
	public void onActivity() {
		cancel();
		//提交延迟异步任务，并得到异步任务返回结果inactivityFuture
		inactivityFuture = inactivityTimer.schedule(
				new FinishListener(activity), INACTIVITY_DELAY_SECONDS,
				TimeUnit.SECONDS);
	}
    
	/**
	 * 通过延迟异步任务的执行结果Future，来取消异步任务。
	 */
	private void cancel() {
		if (inactivityFuture != null) {
			inactivityFuture.cancel(true);
			inactivityFuture = null;
		}
	}
    
	/**
	 * 结束线程池中正在执行的延迟异步任务，并退出线程池。
	 */
	public void shutdown() {
		cancel();
		inactivityTimer.shutdown();
	}
    
	/**
	 * Author:   ljl
	 * Date :    2015-07-26
	 * Description:守护线程的线程工厂Factory
	 */
	private static final class DaemonThreadFactory implements ThreadFactory {
		public Thread newThread(Runnable runnable) {
			Thread thread = new Thread(runnable);
			thread.setDaemon(true);
			return thread;
		}
	}

}
