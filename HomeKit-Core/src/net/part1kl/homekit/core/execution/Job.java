/**
 * See README for licensing information
*/
package net.part1kl.homekit.core.execution;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

import net.part1kl.homekit.core.execution.jobs.templates.InstantJobExecution;
import net.part1kl.homekit.core.execution.jobs.templates.JobExecution;
import net.part1kl.homekit.core.execution.jobs.templates.LoopJobExecution;
import net.part1kl.homekit.core.execution.jobs.templates.ScheduledJobExecution;
import net.part1kl.homekit.core.util.KitArrayList;

/** Runnable type that holds jobs that need to be done.
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
 * 			<td> net.part1kl.homekit.core.execution.jobs </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Class: </th>
 * 			<td> Job </td>
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
public class Job implements Runnable {

	public final JobType JOB_TYPE;
	
	@SuppressWarnings("rawtypes")
	private InstantJobExecution INSTANT_JOB;
	private ScheduledJobExecution SCHEDULED_JOB;
	private LoopJobExecution LOOP_JOB;
	public InstantJobExecution getInstantJob() { return INSTANT_JOB; }
	public ScheduledJobExecution getScheduledJob() { return SCHEDULED_JOB; }
	public LoopJobExecution getLoopJob() { return LOOP_JOB; }
	
	private volatile int RETURNED_VALUE;
	public int getInstantValue() { return RETURNED_VALUE; }
	private volatile Date COMPLETION;
	public Date getCompletionDate() { return COMPLETION; }
	private volatile boolean BREAK_LOOP = false;
	public boolean getBreakLoop() { return BREAK_LOOP; }
	
	
	/**Creates a job that will be immediately added to the execution queue.
	 * 
	 * @param job The job to be done, represented as a Function.
	 * @param jobParameters Parameters for the job at execution.
	 */
	public Job(InstantJobExecution job) {
		JOB_TYPE = JobType.NOW;
		INSTANT_JOB = job;
	}
	
	/**Creates a job that will be executed at a scheduled time.
	 * 
	 * @param job The job to be done, represented as a Function.
	 * @param jobParameters Parameters for the job at execution.
	 * @param whenToSchedule The Date object representing when the job should be executed.
	 */
	public Job(ScheduledJobExecution job) {
		JOB_TYPE = JobType.SCHEDULED;
		SCHEDULED_JOB = job;
	}
	
	/**Creates a job that will be looped with a provided wait time.
	 * 
	 * @param job The job to be done, represented as a Function. The loop will be broken if the returned value from the Function is an integer valued at -1.
	 * @param jobParameters Parameters for the job at execution.
	 * @param loopUpdateTime The amount of time in between executions of the job. If <b>canOverlap</b> is set to <u>false</u>, this may take longer depending
	 * on the time it takes to complete one iteration.
	 * @param canOverlap If set to <u>true</u>, the JobHandler will not wait for one execution of the loop to finish before starting another iteration.
	 */
	public Job(LoopJobExecution job) {
		JOB_TYPE = JobType.LOOPING;
		LOOP_JOB = job;
	}
	
	
	
	@Override
	public void run() {
		switch(JOB_TYPE) {
		case NOW:
			RETURNED_VALUE = INSTANT_JOB.get();
			break;
		case SCHEDULED:
			COMPLETION = SCHEDULED_JOB.get();
			break;
		case LOOPING:
			BREAK_LOOP = LOOP_JOB.get();
			break;
		}
	}
}
