/**
 * See README for licensing information
*/
package net.part1kl.homekit.core.execution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/** TODO Put type description here
 * 
 * 
 * <br><br>
 * <style>
 *		table {
 *  		border-collapse: collapse;
 *		}
 *		table, tr {
 *			border: 1px solid black;
 *		}
 *	</style>
 * <table>
 * 		<tr>
 * 			<th> Project: </th>
 * 			<td> HomeKit-Core </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Package: </th>
 * 			<td> net.part1kl.homekit.core.execution </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Class: </th>
 * 			<td> JobHandler </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Version: </th>
 * 			<td> 0.1 </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Date Created: </th>
 * 			<td> Aug 20, 2018 </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Author: </th>
 * 			<td> part1kl </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Year: </th>
 * 			<td> 2018 </td>
 * 		</tr>
 * </table>
 */
public class JobHandler implements ExecutorService, Runnable{
	
	private volatile boolean running, acceptingNewJobs;
	
	public volatile ConcurrentLinkedQueue<Job> JOBS_AWAITING_EXECUTION;
	public synchronized int addJob(Job job) {
		if(acceptingNewJobs)
			JOBS_AWAITING_EXECUTION.offer(job);
		else
			return -1;
		return 0;
	}
	
	public volatile AtomicInteger ACTIVE_JOB_COUNT, SCHEDULED_JOB_COUNT, LOOPING_JOB_COUNT;
	
	private int MAX_ACTIVE_JOBS;
	
	public JobHandler(int maxActiveJobs) { //CONSTRUCTOR
		JOBS_AWAITING_EXECUTION = new ConcurrentLinkedQueue<Job>();
		ACTIVE_JOB_COUNT = new AtomicInteger(0);
		SCHEDULED_JOB_COUNT = new AtomicInteger(0);
		LOOPING_JOB_COUNT = new AtomicInteger(0);
		MAX_ACTIVE_JOBS = maxActiveJobs;
		running = true;
		acceptingNewJobs = true;
	}
	

	@Override
	public void run() { //RUNNABLE - CHECK FOR SUBMITTED TASKS
		
		while(running) {
			if(!JOBS_AWAITING_EXECUTION.isEmpty() && ACTIVE_JOB_COUNT.get() < MAX_ACTIVE_JOBS) {
				Job currentJob = JOBS_AWAITING_EXECUTION.poll();
				new Thread(()->{ stageJob(currentJob); }).start();
			}
			else {
				sleep(1000);
			}
		}
	}
	
	public void stageJob(Job job) {
		Future wait;
		switch(job.JOB_TYPE) {
		case NOW:
			ACTIVE_JOB_COUNT.incrementAndGet();
			wait = submit(job);
			while(!wait.isDone()) sleep(500);
			ACTIVE_JOB_COUNT.decrementAndGet();
			break;
		case SCHEDULED:
			Thread scheduleWaiter = new Thread(()->{
				SCHEDULED_JOB_COUNT.incrementAndGet();
				while(new Date().getTime()<job.getScheduledJob().getScheduledTime().getTime()) sleep(1000);
				SCHEDULED_JOB_COUNT.decrementAndGet();
				ACTIVE_JOB_COUNT.incrementAndGet();
				Future waiter = submit(job);
				while(!waiter.isDone()) sleep(500);
				ACTIVE_JOB_COUNT.decrementAndGet();
			});
			scheduleWaiter.start();
			break;
		case LOOPING:
			ACTIVE_JOB_COUNT.incrementAndGet();
			Thread looper = new Thread(()->{
				while(true) {
					long currentTime = System.currentTimeMillis();
					Future l = submit(job);
					while(!l.isDone()) sleep(5);
					if(job.getBreakLoop()) break;
					while(System.currentTimeMillis()-currentTime < job.getLoopJob().LOOP_UPDATE_TIME) sleep(5);
				}
			});
			looper.start();
			ACTIVE_JOB_COUNT.decrementAndGet();
			break;
		}
	}
	
	public synchronized void sleep(int millis) { try { wait(millis); } catch (Exception e) { e.printStackTrace(); } }
	
	public Object getFutureValue(Future f) { try { return f.get(); } catch (InterruptedException | ExecutionException e) { e.printStackTrace(); } return null; }
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Future<?> submit(Runnable job) { //EXECUTORSERVICE - EXECUTE A RUNNABLE AND RETURN A FUTURE OBJECT
		FutureTask returned = new FutureTask(job, 0);
		Thread thread = new Thread(returned);
		thread.start();
		return returned;
	}
	
	@Override
	public <T> Future<T> submit(Callable<T> paramTask) { //EXECUTORSERVICE - EXECUTE CALLABLES AND RETURN THE RESULT AS A FUTURE OBJECT
		return null;
	}
	
	@Override
	public void execute(Runnable job) { //EXECUTORSERVICE - EXECUTE RUNNABLES
		
	}

	@Override
	public <T> Future<T> submit(Runnable paramTask, T paramResult) { //EXECUTORSERVICE - EXECUTE RUNNABLES AND SET THE SECOND PARAM TO THE SAME VALUE AS THAT WHICH IS RETURNED
		// TODO Auto-generated method stub
		return null;
	}

	

	
	
	
	
	@Override
	public void shutdown() {
		acceptingNewJobs = false;
		while(!JOBS_AWAITING_EXECUTION.isEmpty()) sleep(500);
		running = false;
	}

	@Override
	public List<Runnable> shutdownNow() {
		running = false;
		sleep(50);
		return new ArrayList<Runnable>(JOBS_AWAITING_EXECUTION.size());
	}
	
	@Override
	public boolean isShutdown() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isTerminated() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean awaitTermination(long paramTimeout, TimeUnit paramUnit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> paramTasks) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> paramTasks, long paramTimeout,
			TimeUnit paramUnit) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> paramTasks)
			throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> paramTasks, long paramTimeout, TimeUnit paramUnit)
			throws InterruptedException, ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
