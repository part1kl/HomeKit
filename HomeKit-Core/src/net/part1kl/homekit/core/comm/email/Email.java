/**
 * See README for licensing information
*/
package net.part1kl.homekit.core.comm.email;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

/**
	 * For use in storing data about email accounts.
	 * <br> If loading accounts from config files, EmailClient loads an ASYNC and a USER account from email_async.config and email_user.config located in the directory path you provide.
	 * <br> Config files require these keys as entries: FROM, FROMNAME, USERNAME, PASSWORD, SMTP_HOST, SMTP_PORT, IMAP_HOST, IMAP_PORT
	 * <br> Config file entries have this syntax: [KEY]=[VALUE]
	 * <br> There should be no whitespace between the [KEY], the equals sign, or the [VALUE].
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
		private static final long serialVersionUID = 1L;

		private Properties SEND_P, READ_P;
		public Session SEND_S, READ_S;
		private Store STORE;
		public Folder INBOX;
		
		/** Auto constructor for Email. reads the email config files in resources/email
		 * 
		 * @param type use EmailClient.ASYNC for the device-to-device communication account, and EmailClient.USER for the account that will be receiving commands from the user.
		 * @throws FileNotFoundException Catches in the event of an invalid file.
		 * @throws MessagingException Catches in the event of an issue with setting up the inbox (IMAP).
		 */
		public Email(String type, String profileDirectory) throws FileNotFoundException, MessagingException {
			if(type.equals(EmailClient.ASYNC) || type.equals(EmailClient.USER))
				makeFromEmailConfigFile(type, profileDirectory);
			makeClient();
		}

		private void makeFromEmailConfigFile(String type, String path) throws FileNotFoundException {
			type = type.toLowerCase();
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
		}
	
		public void makeClient() throws MessagingException {
			makeProperties();
			makeSessions();
			makeStore();
			makeInbox();
		}
		
		public void makeProperties() {
			SEND_P = new Properties();			
			SEND_P.put("mail.smtp.host", getProperty("SMTP_HOST"));
			SEND_P.put("mail.smtp.port", get("SMTP_PORT"));
			SEND_P.put("mail.smtp.user", getProperty("USERNAME"));
			SEND_P.put("mail.smtp.password", getProperty("PASSWORD"));
			SEND_P.put("mail.smtp.auth", "true");
			
			READ_P = System.getProperties();
			READ_P.put("mail.store.protocol", "imaps");
		}
		public void makeSessions() {
			SEND_S = Session.getDefaultInstance(SEND_P);
			READ_S = Session.getDefaultInstance(READ_P, null);
		}
		public void makeStore() throws MessagingException {
			STORE = READ_S.getStore("imaps");
			STORE.connect(
				getProperty("IMAP_HOST"),
				(Integer)get("IMAP_PORT"),
				getProperty("USERNAME"),
				getProperty("PASSWORD")
			);
		}
		public void makeInbox() throws MessagingException {
			INBOX = STORE.getFolder("INBOX");
			INBOX.open(Folder.READ_WRITE);
		}
		
		/**Updates the inbox
		 * 
		 * @throws MessagingException Catches on a failure to connect to and/or read the inbox.
		 */
//		public void checkInbox() throws MessagingException {
//			makeStore();
//			makeInbox();
//		}
		
		public void cleanup() throws MessagingException {
			INBOX.close();
		}
	}