// Zacaria Jama
// 110037765

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Server extends JFrame {
   private JTextArea displayArea;
   private DatagramSocket socket;

   public Server() {
      super("Server");

      displayArea = new JTextArea();
      add(new JScrollPane(displayArea), BorderLayout.CENTER);
      setSize(400, 300);
      setVisible(true);

      try  {
         socket = new DatagramSocket(5000);
      } 
      catch (SocketException socketException) 
      {
         socketException.printStackTrace();
         System.exit(1);
      } 
   }

   public void waitForPackets()
   {
      while (true) 
      {
         try {
            byte[] data = new byte[100];
            DatagramPacket receivePacket = 
               new DatagramPacket(data, data.length);

            socket.receive(receivePacket);

            displayMessage("\nPacket received:" + 
               "\nFrom host: " + receivePacket.getAddress() + 
               "\nHost port: " + receivePacket.getPort() + 
               "\nLength: " + receivePacket.getLength() + 
               "\nContaining:\n\t" + new String(receivePacket.getData(), 
                  0, receivePacket.getLength()));

            if (new String(receivePacket.getData(), 0, receivePacket.getLength()).equals("120.0.0.1")) {
               sendPacketToClient(receivePacket);
            }

         } 
         catch (IOException ioException)
         {
            displayMessage(ioException + "\n");
            ioException.printStackTrace();
         } 
      } 
   }

   private void sendPacketToClient(DatagramPacket receivePacket) 
      throws IOException
   {
      displayMessage("\n\nEcho data to client...");

      DatagramPacket sendPacket = new DatagramPacket(
              receivePacket.getData(), receivePacket.getLength(),
              receivePacket.getAddress(), receivePacket.getPort());
      socket.send(sendPacket);
      displayMessage("Packet sent\n");
   }

   private void displayMessage(final String messageToDisplay)
   {
      SwingUtilities.invokeLater(
         new Runnable() 
         {
            public void run() {
               displayArea.append(messageToDisplay);
            } 
         } 
      ); 
   } 
}
/*
  public void openPage(){
     try {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Class c = Class.forName("Page");
        System.out.println("page");
        Object obj = c.newInstance();
        Component comp = (Component) obj;
        panel.add(comp);
        frame.add(panel);

        frame.setResizable(false);
        frame.setLayout(null);
     } catch (ClassNotFoundException e) {
        e.printStackTrace();
     } catch (InstantiationException e) {
        e.printStackTrace();
     } catch (IllegalAccessException e) {
        e.printStackTrace();
     }
  }
 */