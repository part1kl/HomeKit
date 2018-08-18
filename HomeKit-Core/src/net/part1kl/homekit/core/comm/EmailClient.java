/**
 * See README for licensing information
*/
package net.part1kl.homekit.core.comm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;




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
	
	
	
	private static final String ASYNC = "ASYNC", USER = "USER", SEND = "SEND", READ = "READ";
	
	private HashMap<String, Email> EMAILS = new HashMap<String, Email>();
	private String getFromProperty(String type) { return EMAILS.get(type).getProperty("FROM"); }
	
	private HashMap<String, Properties> PROPS = new HashMap<String, Properties>(); // ASYNC_SEND, ASYNC_READ, USER_SEND, USER_READ
	private HashMap<String, Session> SESSIONS = new HashMap<String, Session>(); // ASYNC_SEND, ASYNC_READ, USER_SEND, USER_READ
	private HashMap<String, Store> STORES = new HashMap<String, Store>(); // ASYNC, USER TODO: create make() method
	private HashMap<String, Folder> INBOXES = new HashMap<String, Folder>(); // ASYNC, USER TODO: create make() method
	
	public EmailClient(String deviceID, String emailProfilePath) {
		DEVICE_ID = deviceID;
		
		EMAILS.put(ASYNC, new Email(ASYNC, emailProfilePath));
		makeClient(ASYNC);
		
		EMAILS.put(USER, new Email(USER, emailProfilePath));
		makeClient(USER);
		
	}
	public void makeClient(String type) {
		makeProperties(type, true);
		makeProperties(type, false);
		makeSession(type, true);
		makeSession(type, false);
		makeStore(type);
		makeInbox(type);
	}
	public void makeProperties(String type, boolean sending) {
		Properties props = new Properties();
		if(sending) {
			props.put("mail.smtp.host", EMAILS.get(type).getProperty("SMTP_HOST"));
			props.put("mail.smtp.port", EMAILS.get(type).get("SMTP_PORT"));
			props.put("mail.smtp.user", EMAILS.get(type).getProperty("USERNAME"));
			props.put("mail.smtp.password", EMAILS.get(type).getProperty("PASSWORD"));
			props.put("mail.smtp.auth", "true");
		}
		else {
			props = System.getProperties();
			props.put("mail.store.protocol", "imaps");
		}
		PROPS.put(type+"_"+(sending ? SEND:READ), props);
	}
	public void makeSession(String type, boolean sending) {
		Session session;
		if(sending) {
			session = Session.getDefaultInstance(PROPS.get(type+"_"+SEND));
		}
		else {
			session = Session.getDefaultInstance(PROPS.get(type+"_"+READ), null);
		}
		SESSIONS.put(type+"_"+(sending ? SEND:READ), session);
	}
	public void makeStore(String type) {
		try {
			Store store = SESSIONS.get(type+"_"+READ).getStore("imaps");
			store.connect(
				EMAILS.get(type).getProperty("IMAP_HOST"),
				(Integer)EMAILS.get(type).get("IMAP_PORT"),
				EMAILS.get(type).getProperty("USERNAME"),
				EMAILS.get(type).getProperty("PASSWORD")
			);
			STORES.put(type, store);
		} catch (Exception e) { e.printStackTrace(); }
	}
	public void makeInbox(String type) {
		try {
			Folder inbox = STORES.get(type).getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);
			INBOXES.put(type, inbox);
		} catch (MessagingException e) { e.printStackTrace(); }
	}
	
//GENERIC EMAIL SENDERS
	
	public void sendPlainTextMessage(String type, String from, String to, String subject, String message) {
		Message mimeMessage = new MimeMessage(SESSIONS.get(type+"_"+SEND));
		
		InternetAddress fromAd = null;
		InternetAddress toAd = null;
		try {
			fromAd = new InternetAddress(from);
			toAd = new InternetAddress(to);
		} catch (Exception e) { e.printStackTrace(); }
		
		try {
			mimeMessage.setFrom(fromAd);
			mimeMessage.setRecipient(RecipientType.TO, toAd);
			mimeMessage.setSubject(subject);
			mimeMessage.setText(message);
			
			Transport sender = SESSIONS.get(type+"_"+SEND).getTransport("smtps");
			sender.connect(
				EMAILS.get(type).getProperty("SMTP_HOST"),
				(Integer)EMAILS.get(type).get("SMTP_PORT"),
				EMAILS.get(type).getProperty("USERNAME"),
				EMAILS.get(type).getProperty("PASSWORD")
			);
			sender.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			sender.close();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
//SPECIFIC EMAIL SENDERS
	
	public void sendAsyncMessage(String recipientID, String message) {
		sendPlainTextMessage(ASYNC, getFromProperty(ASYNC), getFromProperty(ASYNC), DEVICE_ID+" to "+recipientID, message);
	}
	
	
	public void cleanup() {
		try {
			INBOXES.get("ASYNC").close();
			INBOXES.get("USER").close();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
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
		 * @param type use EmailClient.ASYNC for the device-to-device communication account, and EmailClient.USER for the account that will be receiving commands from the user.
		 */
		public Email(String type, String profileDirectory) {
			if(type.equals(ASYNC) || type.equals(USER))
				makeFromEmailConfigFile(type, profileDirectory);
		}

		private void makeFromEmailConfigFile(String type, String path) {
			try {
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
			} catch (FileNotFoundException e) { e.printStackTrace(); }
		}
	}
}
