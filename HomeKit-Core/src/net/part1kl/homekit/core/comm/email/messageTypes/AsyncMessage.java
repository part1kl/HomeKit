/**
 * See README for licensing information
*/
package net.part1kl.homekit.core.comm.email.messageTypes;

import java.io.File;
import java.io.IOException;

import javax.mail.MessagingException;

import net.part1kl.homekit.core.comm.email.Email;

/** Extends EmailMessage. Used for all asynchronous system messages.
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
 * 			<td> net.part1kl.homekit.core.comm.email.messageTypes </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Class: </th>
 * 			<td> AsyncMessage </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Version: </th>
 * 			<td> 0.1 </td>
 * 		</tr>
 * 		<tr>
 * 			<th> Date Created: </th>
 * 			<td> Aug 19, 2018 </td>
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
public class AsyncMessage extends EmailMessage {

	/**
	 * 
	 * @param deviceID DEVICE_ID of sending machine.
	 * @param from Email object for ASYNC account.
	 * @param attachments Attachments to be sent with the email, if any.
	 * @param to DEVICE_ID of intended recipient. EmailClient static variables ALL, NEXUS_ONLY, SUB_NEXUS_ONLY, or DEPS_ONLY can also be put here if they apply.
	 * @param message Actual text content of the message. Plain text only
	 * @throws MessagingException Catches in the event of a failure to set up the email address or the message content.
	 * @throws IOException Catches during setup if one of the attachment files can't be read.
	 */
	public AsyncMessage(String deviceID, Email from, File[] attachments, String to, String message) throws MessagingException, IOException {
		super(from, attachments, deviceID+" to "+to, message, false, RecipientType.TO, new String[]{from.getProperty("FROM")});
	}
}
