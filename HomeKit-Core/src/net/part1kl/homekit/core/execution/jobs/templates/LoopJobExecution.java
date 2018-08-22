/**
 * See README for licensing information
*/
package net.part1kl.homekit.core.execution.jobs.templates;

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
 * 			<td> LoopJobExecution </td>
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
public abstract class LoopJobExecution extends JobExecution<Boolean>{

	public final long LOOP_UPDATE_TIME;
	
	private boolean BREAK_LOOP =  false;
	public void breakLoop() { BREAK_LOOP = true; }
	
	public LoopJobExecution(KitArrayList params, long loopUpdateTime) {
		super(params);
		LOOP_UPDATE_TIME = loopUpdateTime;
	}
	
	@Override
	public Boolean get() {
		execute();
		return BREAK_LOOP;
	}

	@Override
	public abstract void execute();

}
