package com.alcance.stdNegativeCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

public class MailReader {
	
	Folder folder;
	Store store = null;
	public List<String> readMails(String userName, String password)  throws Exception{
		
		// Get a Properties object
		Properties props = System.getProperties();
		
		// Get a Session object
	    Session session = Session.getInstance(props, null);
	    session.setDebug(false);
	    
	    // Get a Store object
	    store = session.getStore("pop3s");
		// Connect
		store.connect("pop3.live.com", -1, userName, password);
		// Open the Folder
		folder = store.getDefaultFolder();
		
		folder = folder.getFolder("INBOX");
		
		if (folder == null) {
			 System.out.println("Invalid folder");
			 return null;
		 }
		 folder.open(Folder.READ_WRITE);
		 

		 
		 Message[] messages = folder.getMessages();
		 
		 List<String> bodies = new ArrayList<String>();
		 int count =1;
         for(Message message : messages) 
         {
        	 System.out.println("["+count+"] CONTENT TYPE IS " + message.getContentType() + " SUBJECT: " + message.getSubject());
        	 if(message.isMimeType("multipart/*")) {
        		 Multipart mp = (Multipart)message.getContent();
        		 BodyPart part = mp.getBodyPart(0);
        		 //bodies.add("<b>" + message.getSubject() + "</b>"+part.getContent() + "");
        		 String contents = part.getContent() + "";
        		 String splits[] = contents.split("\n");
        		 try {
        			 String nword = splits[splits.length -2];
        			 System.out.println("FETCHED -> " +nword);
        			 bodies.add(nword);
        		 }catch (Exception e) {
				}
        		
        	 }
        	 if(message.isMimeType("text/*")) {
        		// bodies.add(message.getContent() + "");
        		 String contents = message.getContent() + "";
        		 String splits[] = contents.split("\n");
        		 try {
        			
        			 String nword = splits[splits.length -2];
        			 System.out.println("FETCHED -> " +nword);
        			 bodies.add(nword);
        		 }catch (Exception e) {
				}
        	 }
        	 count++;
         }
         folder.close(false);
		 store.close();
         return bodies;
	}
	
	public void close(){
		try {
			if(null != store) {
				store.close();
			}
			if(null != folder) {
				folder.close(true);
			}
		}catch (Exception e){
			
		}
	}
	
	private void dumpPart(Part p) throws Exception {
		if (p instanceof Message)
		    dumpMessage((Message)p);
	}
	
	private void dumpMessage(Message message) throws Exception {
		Object contents = message.getContent();
		System.out.println(" MESSAGE IS =====>\n  " + contents);
		System.out.println("-----------END-----------");
	}
	
	private  String showFlag(Message message) throws Exception{
		// FLAGS
		Flags flags = message.getFlags();
		StringBuffer sb = new StringBuffer();
		Flags.Flag[] sf = flags.getSystemFlags(); // get the system flags

		boolean first = true;
		for (int i = 0; i < sf.length; i++) {
		    String s;
		    Flags.Flag f = sf[i];
		    if (f == Flags.Flag.ANSWERED)
			s = "\\Answered";
		    else if (f == Flags.Flag.DELETED)
			s = "\\Deleted";
		    else if (f == Flags.Flag.DRAFT)
			s = "\\Draft";
		    else if (f == Flags.Flag.FLAGGED)
			s = "\\Flagged";
		    else if (f == Flags.Flag.RECENT)
			s = "\\Recent";
		    else if (f == Flags.Flag.SEEN)
			s = "\\Seen";
		    else
			continue;	// skip it
		    if (first)
			first = false;
		    else
			sb.append(' ');
		    sb.append(s);
		}

		String[] uf = flags.getUserFlags(); // get the user flag strings
		for (int i = 0; i < uf.length; i++) {
		    if (first)
			first = false;
		    else
			sb.append(' ');
		    sb.append(uf[i]);
		}
		return sb.toString();
	}
}
