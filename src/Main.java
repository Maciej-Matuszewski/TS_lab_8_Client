import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;
public class Main{
    
    void run()
    {

        Socket client = null;
        ObjectOutputStream output = null;
        ObjectInputStream input = null;
        
        try{
            client = new Socket(JOptionPane.showInputDialog("Podaj adres serwera"), 9666);
            output = new ObjectOutputStream(client.getOutputStream());
            output.flush();
            input = new ObjectInputStream(client.getInputStream());
            
            if(JOptionPane.showConfirmDialog(null, "Czy chcesz nawiazaæ po³¹czenie?", "", 0) == 0){
    			
            	JOptionPane.showMessageDialog(null, "DZWONIE...(INVITE)");
                sendMessage("INVITE", output);
                
                String message = "";
                Boolean bye = false;
                while(!bye){
                	
                	try {
						message = (String)input.readObject();
						System.out.println("< " + message);
						switch (message) {
						case "100 Trying":
						case "180 Ringing":
							break;
							
						case "200 OK":
							sendMessage("ACK", output);
							String sendMsg1 = JOptionPane.showInputDialog("Wiadomosc Klient 1:");
							if(sendMsg1.equals("BYE"))bye = true;
				            sendMessage(sendMsg1, output);
							break;
							
						case "486 Busy Here":
							sendMessage("BYE", output);
							bye = true;
							break;
							
						case "BYE":
							JOptionPane.showMessageDialog(null,message);
		    	 	        sendMessage("200 OK", output);
		    	 	        bye = true;
							break;

						default:
							JOptionPane.showMessageDialog(null, message);
							String sendMsg = JOptionPane.showInputDialog("Wiadomosc Klient 1:");
							if(sendMsg.equals("BYE"))bye = true;
				            sendMessage(sendMsg, output);
							
							break;
						}
						
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
                }
    		}else{
    			String message = "";
    			Boolean bye = false;
				while(!bye){
					
					try {
						message = (String)input.readObject();
						System.out.println("< " + message);
						
						switch (message) {
							
						case "INVITE":
							sendMessage("100 Trying", output);
							sendMessage("180 Ringing", output);
							if(JOptionPane.showConfirmDialog(null, "Czy chcesz odebraæ po³¹czenie od "+(String)input.readObject()+"?", "", 0) == 0) sendMessage("200 OK", output);
							else sendMessage("486 Busy Here", output);
							break;
							
						case "ACK":
							break;
							
						case "BYE":
							JOptionPane.showMessageDialog(null,message);
				 	        sendMessage("200 OK", output);
							break;

						default:
							
							JOptionPane.showMessageDialog(null, message);
							String sendMsg = JOptionPane.showInputDialog("Wiadomosc Klient 2:");
							if(sendMsg.equals("BYE"))bye = true;
				            sendMessage(sendMsg, output);
							
							break;
						}
						
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
    		}
        }
        catch(UnknownHostException unknownHost){
            unknownHost.printStackTrace();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
        	try {
				input.close();
				output.close();
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
    }
    
    void sendMessage(String message, ObjectOutputStream out)
    {
        try{
        	System.out.println("> "+message);
            out.writeObject(message);
            out.flush();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    
    public static void main(String args[])
    {
        Main client = new Main();
        client.run();
    }
    
}
