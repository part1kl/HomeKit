/**
 * See README for licensing information
*/
package net.part1kl.homekit.core.comm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;




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

	private Email ASYNC, USER;
	public String getAsyncUsername() {return ASYNC.getProperty("USERNAME");}
	
	public EmailClient(String emailProfilePath) {
		ASYNC = new Email("async", emailProfilePath);
		USER = new Email("user", emailProfilePath);
	}
	
	
	
	
	
	/**
	 * For use in storing data about email accounts
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
	 * 			<td> Email </td>
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
	public class Email extends Properties{
		
		/** Manual constructor for Email
		 * 
		 * @param from email address
		 * @param fromName name to be used when sending emails
		 * @param username username for email login. sometimes the same as the email address
		 * @param smtp_host smtp URL for sending emails
		 * @param imap_host imap URL for reading emails
		 * @param smtp_port smtp port for sending emails
		 * @param imap_port smtp port for reading emails
		 */
		public Email(String from, String fromName, String username, String password, String smtp_host, String imap_host, int smtp_port, int imap_port) {
			put("FROM", from);
			put("FROMNAME", fromName);
			put("USERNAME", username);
			put("PASSWORD", password);
			put("SMTP_HOST", smtp_host);
			put("IMAP_HOST", imap_host);
			put("SMTP_PORT", smtp_port);
			put("IMAP_PORT", imap_port);
		}
		
		/** Auto constructor for Email. reads the email config files in resources/email
		 * 
		 * @param type use "async" for the device-to-device communication account, and "user" for the account that will be receiving commands from the user.
		 */
		public Email(String type, String profilePath) {
			if(type.equals("async") || type.equals("user"))
				makeFromEmailConfigFile(type, profilePath);
		}

		private void makeFromEmailConfigFile(String type, String path) {
			try {
				Scanner read = new Scanner(new FileInputStream(new File(path+"email_"+type+".config")));
				ArrayList<String> lines = new ArrayList<String>();
				while(read.hasNext()) {
					lines.add(read.nextLine());
				}
				read.close();
				Scanner data;
				for(String s : lines) {
					data = new Scanner(s).useDelimiter("=");
					String key = data.next(), value = data.next();
					if(key.contains("PORT"))
						put(key, Integer.parseInt(value));
					else
						put(key, value);
					data.close();
				}
			} catch (FileNotFoundException e) { e.printStackTrace(); }
		}
	}
}
