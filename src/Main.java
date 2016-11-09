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

        	String fromAddress = JOptionPane.showInputDialog("Client: Podaj adres email nadawcy");
        	
        	String[] fromAddressSplited = fromAddress.split("@");
        	
        	if (fromAddressSplited.length != 2 ){
        		JOptionPane.showMessageDialog(null, "Client: Nieprawidłowy adres email");
        		this.run();
        		return;
        	}
        	
            client = new Socket(fromAddressSplited[1], 9666);
            output = new ObjectOutputStream(client.getOutputStream());
            output.flush();
            input = new ObjectInputStream(client.getInputStream());
            String recivedMessage;
            
            recivedMessage = (String)input.readObject();
            if (!recivedMessage.substring(0, 3).equals("220")){
        		JOptionPane.showMessageDialog(null, "Client: Coś poszło nie tak :(");
        		this.run();
        		return;
            }
            
            this.sendMessage("helo serwer.email.com", output);
            
            ////////////////////////////////////////////////////////////
            recivedMessage = (String)input.readObject();
            if (!recivedMessage.substring(0, 3).equals("250")){
        		JOptionPane.showMessageDialog(null, "Client: Coś poszło nie tak :(");
        		this.run();
        		return;
            }
            
            this.sendMessage("mail from: <" + fromAddress + ">", output);
            
            ////////////////////////////////////////////////////////////
            recivedMessage = (String)input.readObject();
            if (!recivedMessage.substring(0, 3).equals("250")){
        		JOptionPane.showMessageDialog(null, "Client: Coś poszło nie tak :(");
        		this.run();
        		return;
            }
        	String toAddress = JOptionPane.showInputDialog("Client: Podaj adres email odbiorcy");
            
            this.sendMessage("rcpt to: <" + toAddress + ">", output);
            
            ////////////////////////////////////////////////////////////
            recivedMessage = (String)input.readObject();
            if (!recivedMessage.substring(0, 3).equals("250")){
        		JOptionPane.showMessageDialog(null, "Client: Coś poszło nie tak :(");
        		this.run();
        		return;
            }
            
            this.sendMessage("data", output);
            
            ////////////////////////////////////////////////////////////
            recivedMessage = (String)input.readObject();
            if (!recivedMessage.substring(0, 3).equals("354")){
        		JOptionPane.showMessageDialog(null, "Client: Coś poszło nie tak :(");
        		this.run();
        		return;
            }

        	String data = JOptionPane.showInputDialog("Client: Podaj treść wiadomości");
            this.sendMessage(data, output);
            
            ////////////////////////////////////////////////////////////
            recivedMessage = (String)input.readObject();
            if (!recivedMessage.substring(0, 3).equals("250")){
        		JOptionPane.showMessageDialog(null, "Client: Coś poszło nie tak :(");
        		this.run();
        		return;
            }
            
            this.sendMessage("quit", output);
            
            
        }
        catch(UnknownHostException unknownHost){
            unknownHost.printStackTrace();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        finally{
        	try {
        		if (input != null) input.close();
        		if (output != null) output.close();
        		if (client != null) client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        }
    }
    
    void sendMessage(String message, ObjectOutputStream out){
        try{
        	System.out.println("> "+message);
            out.writeObject(message);
            out.flush();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    
    public static void main(String args[]){
        Main client = new Main();
        client.run();
    }
    
}
