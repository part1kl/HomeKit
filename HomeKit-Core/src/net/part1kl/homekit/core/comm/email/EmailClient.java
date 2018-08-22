/**
 * See README for licensing information
*/
package net.part1kl.homekit.core.comm.email;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.mail.Message;
import javax.mail.MessagingException;

import net.part1kl.homekit.core.comm.email.messageTypes.AsyncMessage;




/** Email client for sending and receiving messages using Javamail
 * <br> Email is used as a central system for logging asynchronous communication,
 * as well as communication from the user via text.
 * <br> All email clients connect to two emails, one for user commands, the other for asynchronous communication.
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
 * 			<td> EmailClient </td>
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
public class EmailClient {
	
	private String DEVICE_ID;

	
	/**Generic Recipient ID for ASYNC messages only */
	public static final String ALL = "ALL", NEXUS_ONLY = "NEXUS", SUB_NEXUS_ONLY = "SUB_NEXUS", DEPS_ONLY = "DEPLOYMENTS";
	
	public static final String ASYNC = "ASYNC", USER = "USER";
	
	private Email EMAIL_ASYNC, EMAIL_USER;
	
	private HashMap<Date, Message> ASYNC_MESSAGES = new HashMap<Date, Message>();
	
	private AsyncMessageInterpreter ASYNC_MESSAGE_READER;
	
	public EmailClient(String deviceID, String emailProfilePath) throws FileNotFoundException, MessagingException {
		DEVICE_ID = deviceID;
		EMAIL_ASYNC = new Email(ASYNC, emailProfilePath);
		EMAIL_USER = new Email(USER, emailProfilePath);
		ASYNC_MESSAGE_READER = new AsyncMessageInterpreter(EMAIL_ASYNC, ASYNC_MESSAGES);
	}

//MESSAGE SENDINGE	
	/**Sends a message to all devices connected to the ASYNC account.
	 * 
	 * @param deviceID DEVICE_ID of intended recipient. EmailClient static variables ALL, NEXUS_ONLY, SUB_NEXUS_ONLY, or DEPS_ONLY can also be put here if they apply.
	 * @param message Actual text content of the message. Plain text only
	 * @throws MessagingException Catches in the event of a failure to use the ASYNC email address, or in setup of the message content. Also catches if the email failed to send.
	 * @throws IOException Catches if one of the attachment files can't be read.
	 */
	public void sendAsyncMessage(String deviceID, String message) throws MessagingException, IOException {
		new AsyncMessage(DEVICE_ID, EMAIL_ASYNC, null, deviceID, message).sendMessage();
	}
	
//MESSAGE READING
	/**Checks to see if there are new messages in the ASYNC inbox.
	 * 
	 * @throws MessagingException Catches if the client is unable to update the inbox.
	 */
	public void checkAsyncInbox() throws MessagingException {
		
	}
	
	public void cleanup() throws MessagingException {
		EMAIL_ASYNC.cleanup();
		EMAIL_USER.cleanup();
	}
	
	

}
