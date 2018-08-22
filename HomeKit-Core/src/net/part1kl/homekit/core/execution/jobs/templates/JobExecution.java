/**
 * See README for licensing information
*/
package net.part1kl.homekit.core.execution.jobs.templates;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
 * 			<td> JobExecution </td>
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
public abstract class JobExecution<R> implements Supplier<R>{
	
	public final KitArrayList PARAMS;
	public KitArrayList getParameters() { return PARAMS; }
	public Object getParameter(int index) {return PARAMS.get(index); }
	
	public JobExecution(KitArrayList params) {
		PARAMS = params;
	}
	
	@Override
	public abstract R get();
	
	public abstract void execute();
	
}
