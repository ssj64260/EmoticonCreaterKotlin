package com.android.enoticoncreaterkotlin.util

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class ThreadPoolUtil private constructor() {

    companion object {
        private var maxThread = 5

        val instance: ThreadPoolUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ThreadPoolUtil()
        }

        //newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
        private var cachedThreadPool: ExecutorService? = null
        //newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
        private var fixedThreadPool: ExecutorService? = null
        //newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
        private var scheduledThreadPool: ScheduledExecutorService? = null
        //newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)
        private var singleThreadExecutor: ExecutorService? = null
    }

    //设置最大线程数（建议在application里设置）
    fun init(maxThread: Int) {
        ThreadPoolUtil.maxThread = maxThread
    }

    //缓存线程池
    fun cachedExecute(runnable: Runnable) {
        if (cachedThreadPool == null || cachedThreadPool!!.isShutdown) {
            cachedThreadPool = Executors.newCachedThreadPool()
        }
        cachedThreadPool!!.execute(runnable)
    }

    //定长线程池
    fun fixedExecute(runnable: Runnable) {
        if (fixedThreadPool == null || fixedThreadPool!!.isShutdown) {
            fixedThreadPool = Executors.newFixedThreadPool(maxThread)
        }
        fixedThreadPool!!.execute(runnable)
    }

    /**
     * 延迟执行线程池
     *
     * @param runnable 线程操作
     * @param initialDelay 延迟时间
     * @param timeUnit 时间类型（时，分，秒）
     */
    fun scheduled(runnable: Runnable, initialDelay: Long, timeUnit: TimeUnit) {
        if (scheduledThreadPool == null || scheduledThreadPool!!.isShutdown) {
            scheduledThreadPool = Executors.newScheduledThreadPool(maxThread)
        }
        scheduledThreadPool!!.schedule(runnable, initialDelay, timeUnit)
    }

    /**
     * 初始延迟+周期线程池
     *
     * @param runnable 线程操作
     * @param initialDelay 延迟时间
     * @param period 周期时间
     * @param timeUnit 时间类型（时，分，秒）
     */
    fun scheduledRate(runnable: Runnable, initialDelay: Long, period: Long, timeUnit: TimeUnit) {
        if (scheduledThreadPool == null || scheduledThreadPool!!.isShutdown) {
            scheduledThreadPool = Executors.newScheduledThreadPool(maxThread)
        }
        scheduledThreadPool!!.scheduleAtFixedRate(runnable, initialDelay, period, timeUnit)
    }


    /**
     * 初始延迟+每次延迟线程池
     *
     * @param runnable 线程操作
     * @param initialDelay 延迟时间
     * @param delay 每次执行后延迟时间
     * @param timeUnit 时间类型（时，分，秒）
     */
    fun scheduledDelay(runnable: Runnable, initialDelay: Long, delay: Long, timeUnit: TimeUnit) {
        if (scheduledThreadPool == null || scheduledThreadPool!!.isShutdown) {
            scheduledThreadPool = Executors.newScheduledThreadPool(maxThread)
        }
        scheduledThreadPool!!.scheduleWithFixedDelay(runnable, initialDelay, delay, timeUnit)
    }

    //单线程线程池
    fun singleExecute(runnable: Runnable) {
        if (singleThreadExecutor == null || singleThreadExecutor!!.isShutdown) {
            singleThreadExecutor = Executors.newSingleThreadExecutor()
        }
        singleThreadExecutor!!.execute(runnable)
    }

    fun cachedShutDown(awaitTime: Long) {
        setShutDown(cachedThreadPool, awaitTime)
    }

    fun fixedShutDown(awaitTime: Long) {
        setShutDown(fixedThreadPool, awaitTime)
    }

    fun scheduledShutDown(awaitTime: Long) {
        setShutDown(scheduledThreadPool, awaitTime)
    }

    fun singleShutDown(awaitTime: Long) {
        setShutDown(singleThreadExecutor, awaitTime)
    }

    /**
     * @param executorService 需要终止的线程池类型
     * @param awaitTime 线程总执行时间，超时则终止（传入0则立刻终止）
     */
    private fun setShutDown(executorService: ExecutorService?, awaitTime: Long) {
        if (executorService != null) {
            try {
                executorService.shutdown()

                // (所有的任务都结束的时候，返回TRUE)
                if (!executorService.awaitTermination(awaitTime, TimeUnit.MILLISECONDS)) {
                    // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                    executorService.shutdownNow()
                }
            } catch (e: InterruptedException) {
                // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
                e.printStackTrace()
                executorService.shutdownNow()
            }

        }
    }
}
