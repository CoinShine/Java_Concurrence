package com.coinshine.contextswitch;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * DESCRIPTION:线程上下文切换
 * 参考方腾飞老师的《Java并发编程的艺术》第一章
 * 结论两个线程并行：当循环次数在1亿级别，并行才比较占优势
 * （和cup性能有关，测试机器i7-6700HQ）
 *
 * @author Coins
 * @create 2019-07-10 15:17
 */
public class ContextSwitch {


	private static final long COUNT = 100000000L;
	private static ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("thread-call-runner-%d").build();
	private static ExecutorService executorService = new ThreadPoolExecutor(1, 1,
			0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), threadFactory);

	public static void main(String[] args) {
		concurrency();
		serial();
	}

	/**
	 * 两个线程并行
	 *
	 */
	private static void concurrency(){
		long start = System.currentTimeMillis();

		executorService.execute(() -> {
			int a = 0;
			for (int i = 0; i < COUNT; i++) {
				a += 1;
			}
			System.out.println("a=" + a);
		});

		int b = 0;
		for (long i = 0; i < COUNT; i++) {
			b--;
		}
		executorService.shutdown();

		long time = System.currentTimeMillis() - start;
		System.out.println("Concurrency：" + time + "ms, b = " + b);
	}

	/**
	 * 串行
	 */
	private static void serial() {
		long start = System.currentTimeMillis();

		int a = 0;
		for (long i = 0; i < COUNT; i++) {
			a += 1;
		}
		int b = 0;
		for (int i = 0; i < COUNT; i++) {
			b--;
		}

		long time = System.currentTimeMillis() - start;
		System.out.println("Serial：" + time + "ms, b = " + b + ", a = " + a);
	}

}
