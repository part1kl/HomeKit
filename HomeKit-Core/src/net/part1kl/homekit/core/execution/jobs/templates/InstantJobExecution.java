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
 * 			<td> net.part1kl.homekit.core.execution.jobs </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Class: </th>
 * 			<td> InstantJobExecution </td>
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
public abstract class InstantJobExecution extends JobExecution<Integer>{

	private int RETURN_VALUE = 0;
	private void setReturnValue(int val) { RETURN_VALUE = val; }
	
	public InstantJobExecution(KitArrayList paramParams) {
		super(paramParams);
	}
	
	@Override
	public Integer get() {
		execute();
		return RETURN_VALUE;
	}
	
	@Override
	public abstract void execute();

}
