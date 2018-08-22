/**
 * See README for licensing information
*/
package net.part1kl.homekit.core.execution.jobs.templates;

import java.util.Date;

import net.part1kl.homekit.core.util.KitArrayList;

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
 * 			<td> net.part1kl.homekit.core.execution.jobs.templates </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Class: </th>
 * 			<td> ScheduledJobExecution </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Version: </th>
 * 			<td> 0.1 </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Date Created: </th>
 * 			<td> Aug 21, 2018 </td>
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
public abstract class ScheduledJobExecution extends JobExecution<Date>{

	private final Date SCHEDULED_TIME;
	public Date getScheduledTime() { return SCHEDULED_TIME; }
	
	public ScheduledJobExecution(KitArrayList params, Date scheduledTime) {
		super(params);
		SCHEDULED_TIME = scheduledTime;
	}
	
	@Override
	public Date get() {
		execute();
		return new Date();
	}

	@Override
	public abstract void execute();
	
	
}
