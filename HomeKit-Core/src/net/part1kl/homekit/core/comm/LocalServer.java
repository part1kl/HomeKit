/**
 * See README for licensing information
*/
package net.part1kl.homekit.core.comm;

import java.io.IOException;
import java.net.ServerSocket;



/** Socket server
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
 * 			<td> net.part1kl.homekit.core.comm </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Class: </th>
 * 			<td> LocalServer </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Version: </th>
 * 			<td> 0.1 </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Date Created: </th>
 * 			<td> Aug 16, 2018 </td>
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
public class LocalServer implements Runnable{

	private final int SERVER_PORT;
	private ServerSocket SERVER;
	
	public LocalServer(int PORT) {
		SERVER_PORT = PORT;
		try {
			SERVER = new ServerSocket(SERVER_PORT);
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public void run() {
		try {
			
		}catch(Exception e) {e.printStackTrace();}
	}
}
