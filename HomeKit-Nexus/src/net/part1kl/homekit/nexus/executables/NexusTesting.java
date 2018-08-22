/**
 * See README for licensing information
*/
package net.part1kl.homekit.nexus.executables;

import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.function.Function;

import net.part1kl.homekit.core.execution.Job;
import net.part1kl.homekit.core.execution.JobHandler;
import net.part1kl.homekit.core.execution.jobs.templates.InstantJobExecution;
import net.part1kl.homekit.core.execution.jobs.templates.LoopJobExecution;
import net.part1kl.homekit.core.execution.jobs.templates.ScheduledJobExecution;
import net.part1kl.homekit.core.util.KitArrayList;



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
//		EmailClient client = new EmailClient(DEVICE_ID, "C:\\Users\\Ricoc\\OneDrive\\Programming\\Java\\HomeKit-non-github-resources\\email-profiles\\");
//		
//		client.checkAsyncInbox(); //TODO: create custom listener Runnable that will send EmailClient new messages if they apply to the current DEVICE_ID.
//		
//		client.sendAsyncMessage(EmailClient.ALL, "Test All");
//		
//		client.checkAsyncInbox();
//		
//		client.sendAsyncMessage(EmailClient.NEXUS_ONLY, "Test Nexus Only");
//		
//		client.checkAsyncInbox();
//		
//		client.cleanup();
//		
//		System.out.println("Done.");
		
		JobHandler JOB_HANDLER = new JobHandler(3);
		Thread JOB_HANDLER_THREAD = new Thread(JOB_HANDLER);
		JOB_HANDLER_THREAD.start();
		
		Function<ArrayList<String>, Integer> testFunc = (ArrayList<String> s) ->{
			for(String p : s)
				System.out.println(p);
			NexusTesting.Data.counter--;
			if(NexusTesting.Data.counter==0) return -1;
			return 0;
		};
		KitArrayList
			instant = new KitArrayList<String>("Immediate Job"),
			scheduled = new KitArrayList<String>("Scheduled Job"),
			looped = new KitArrayList<String>("Looped Job");
		
		InstantJobExecution instantJob = new InstantJobExecution(instant) {
			@Override
			public void execute() {
				System.out.println(getParameters().get(0));
			}
		};
		ScheduledJobExecution scheduledJob = new ScheduledJobExecution(scheduled, new Date(new Date().getTime()+5000)) {
			@Override
			public void execute() {
				System.out.println(getParameter(0));
			}
		};
		LoopJobExecution loopedJob = new LoopJobExecution(looped, 2000) {
			@Override
			public void execute() {
				System.out.println(getParameters().get(0));
				Data.counter--;
				if(Data.counter==0) breakLoop();
			}
		};
		
		Job testNowJob = new Job(instantJob);
		Job testScheduledJob = new Job(scheduledJob);
		Job testLoopedJob = new Job(loopedJob);
		
		System.out.println(JOB_HANDLER.addJob(testNowJob));
		System.out.println(JOB_HANDLER.addJob(testScheduledJob));
		System.out.println(JOB_HANDLER.addJob(testLoopedJob));
		
		Thread.sleep(500);
		JOB_HANDLER.shutdown();
	}
	
	public static class Data {
		public static volatile int counter = 6;
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
