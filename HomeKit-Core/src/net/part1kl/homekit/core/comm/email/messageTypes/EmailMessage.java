/**
 * See README for licensing information
*/
package net.part1kl.homekit.core.comm.email.messageTypes;

import java.io.File;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import net.part1kl.homekit.core.comm.email.Email;



/** Basic email type. Instances of EmailMessage only need to make custom contructors and call super() with preset variables to be able to send messages.
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
 * 			<td> EmailMessage </td>
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
public abstract class EmailMessage extends MimeMessage{
	
//PUBLIC
	public final Email FROM;
	public final String[] TO;
	public final String SUBJECT, MESSAGE;
	public final File[] ATTACHMENTS;
	
//PRIVATE
	private final String PLAIN_TEXT = "text/plain", HTML_TEXT = "text/html; charset=utf-8";
	private boolean HTML_MESSAGE;
	private InternetAddress FROM_ADDRESS;
	private InternetAddress TO_ADDRESSES[];
	
	/**
	 * 
	 * @param from Email object used to send the email.
	 * @param attachments Attachments to be sent with the email, if any.
	 * @param subject Subject of the message.
	 * @param message Actual text content of the message. Can be plain text or HTML.
	 * @param isHTML Set true if message is HTML, false if message is plain text.
	 * @param toCcBcc Use one of the static RecipientType variables to specify how the email is sent.
	 * @param to Enter as many email addresses as you need here.
	 * @throws IOException Catches during setup if one of the attachment files can't be read.
	 * @throws MessagingException Catches in the event of a failure to set up the email addresses or the message content.
	 */
	public EmailMessage(Email from, File[] attachments, String subject, String message, boolean isHTML, javax.mail.Message.RecipientType toCcBcc, String... to) throws MessagingException, IOException {
		super(from.SEND_S);
		FROM = from;
		ATTACHMENTS = attachments;
		SUBJECT = subject;
		MESSAGE = message;
		HTML_MESSAGE = isHTML;
		TO = to;
		makeMessage(toCcBcc);
	}
	
	public void makeMessage(javax.mail.Message.RecipientType toCcBcc) throws IOException, MessagingException {
		FROM_ADDRESS = new InternetAddress(FROM.getProperty("FROM"));
		
		TO_ADDRESSES = new InternetAddress[TO.length];
		for(int i=0; i<TO.length; i++) {
			TO_ADDRESSES[i] = new InternetAddress(TO[i]);
		}
		
		setFrom(FROM_ADDRESS);
		setRecipients(toCcBcc, TO_ADDRESSES);
		setSubject(SUBJECT);
		
		if(ATTACHMENTS == null) {
			setContent(MESSAGE, (HTML_MESSAGE ? HTML_TEXT:PLAIN_TEXT));
		}
		else {
			BodyPart bodyPart = new MimeBodyPart();
			setContent(MESSAGE, (HTML_MESSAGE ? HTML_TEXT:PLAIN_TEXT));
			
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(bodyPart);
			
			for(File f : ATTACHMENTS) {
				bodyPart = new MimeBodyPart();
				bodyPart.setDataHandler(new DataHandler(new FileDataSource(f)));
				bodyPart.setFileName(f.getCanonicalPath());
				multipart.addBodyPart(bodyPart);
			}
			
			setContent(multipart);
		}
	}
	
	/**Sends the email.
	 * 
	 * @throws MessagingException Catches if the email failed to send.
	 */
	public void sendMessage() throws MessagingException {
		Transport sender = FROM.SEND_S.getTransport("smtps");
		sender.connect(
			FROM.getProperty("SMTP_HOST"),
			(Integer)FROM.get("SMTP_PORT"),
			FROM.getProperty("USERNAME"),
			FROM.getProperty("PASSWORD")
		);
		sender.sendMessage(this, getAllRecipients());
		sender.close();
	}

	
	
}
