package com.coinshine.visibility;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * DESCRIPTION:并发可见性问题
 *
 * @author Coins
 * @create 2019-07-10 12:54
 */
public class VisibleProblem {
	private static ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("thread-call-runner-%d").build();
	private static ExecutorService executorService = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), threadFactory);
	private static long count = 0;

	private void add10k() {
		int stepValue = 10000;
		int idx = 0;
		while (idx++ < stepValue) {
			count += 1;
		}
	}

	private static long calc() {
		VisibleProblem visibleProblem = new VisibleProblem();
		executorService.execute(visibleProblem::add10k);
		executorService.execute(visibleProblem::add10k);
		//保证两个线程执行完成
		executorService.shutdown();
		return count;
	}

	public static void main(String[] args){
		System.out.println(calc());
	}
}
