/**
 * See README for licensing information
*/
package net.part1kl.homekit.nexus.executables;

import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import net.part1kl.homekit.core.comm.email.EmailClient;

/** This class is for Nexus testing only.
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
 * 			<td> HomeKit-Nexus </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Package: </th>
 * 			<td> net.part1kl.homekit.nexus.executables </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Class: </th>
 * 			<td> NexusTesting </td>
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
public class NexusTesting {
	
	public static String DEVICE_ID = "Nexus_Testing";

	public static void main(String[] args) throws Exception {
		EmailClient client = new EmailClient(DEVICE_ID, "C:\\Users\\Ricoc\\OneDrive\\Programming\\Java\\HomeKit-non-github-resources\\email-profiles\\");
		
		client.checkAsyncInbox(); //TODO: create custom listener Runnable that will send EmailClient new messages if they apply to the current DEVICE_ID.
		
		client.sendAsyncMessage(EmailClient.ALL, "Test All");
		
		client.checkAsyncInbox();
		
		client.cleanup();
		
		System.out.println("Done.");
	}
	
	private void socketStuff() throws Exception{
		Thread clientThread = new Thread(()->{
			try {
				Socket client = new Socket("127.0.0.1", 25565);
				PrintStream clientOut = new PrintStream(client.getOutputStream(), true);
				Scanner clientIn = new Scanner(client.getInputStream());
				int clientSendNum = 10;
				int clientGetNum = 0;
				clientOut.println(clientSendNum);
				clientGetNum = clientIn.nextInt();
				System.out.println(clientGetNum);
			} catch(Exception e) {e.printStackTrace();}
		});
		Thread serverThread = new Thread(()->{
			try {
				ServerSocket server = new ServerSocket(25565);
				Socket connectedClient = server.accept();
				Scanner clientIn = new Scanner(connectedClient.getInputStream());
				int received = 0;
				received = clientIn.nextInt();
				int sent = received * 2;
				PrintStream clientOut = new PrintStream(connectedClient.getOutputStream());
				clientOut.println(sent);
				
			}catch(Exception e) {e.printStackTrace();}
		});
		
		serverThread.start();
		clientThread.start();
		
	}
}
