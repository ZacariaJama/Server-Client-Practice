// Zacaria Jama
// 110037765

import java.awt.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Client extends JFrame
{
   private JTextField enterField;
   private JTextArea displayArea;
   private DatagramSocket socket;


   public Client()
   {
      super("Main.Client");

      enterField = new JTextField("120.0.0.1");
      enterField.addActionListener(
              new ActionListener()
              {
                 public void actionPerformed(ActionEvent event)
                 {
                    try {
                       String[] files = event.getActionCommand().split(" ");
                       for (String message: files) {
                          displayArea.append("\nSending packet containing: " +
                                  message + "\n");

                          byte[] data = message.getBytes();

                          DatagramPacket sendPacket = new DatagramPacket(data,
                                  data.length, InetAddress.getLocalHost(), 5000);

                          socket.send(sendPacket);
                       }
                       displayArea.append("Packet sent\n");
                       displayArea.setCaretPosition(
                               displayArea.getText().length());
                    }
                    catch (IOException ioException)
                    {
                       displayMessage(ioException + "\n");
                       ioException.printStackTrace();
                    }
                 }
              }
      );

      add(enterField, BorderLayout.NORTH);

      displayArea = new JTextArea();
      add(new JScrollPane(displayArea), BorderLayout.CENTER);

      setSize(400, 300);
      setVisible(true);

      try {
         socket = new DatagramSocket();
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
            DatagramPacket receivePacket = new DatagramPacket(
                    data, data.length);

            socket.receive(receivePacket);


            //----------------------------------------------------------------------------------------------------------
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            //String pack = new String(receivePacket.getData(), 0, receivePacket.getLength());
            Class<?> cls = Class.forName("Page");
            Constructor<?> constructor = cls.getConstructor();

            FileWriter fw = new FileWriter("C:\\Users\\zjama\\JAVA PROGRAMS\\src\\report.txt");
            BufferedWriter bw = new BufferedWriter(fw);

            JPanel panel = new JPanel();
            String rpt;
            panel.setBounds(30,30,100,100);
            if (cls.getField("compileError").equals(true)){
               rpt = "Failed to compile.";
               JLabel label = new JLabel(rpt);
               bw.write(rpt+"\n");
               label.setBounds(30,30,100,50);
               panel.add(label);
            }
            if (cls.getField("test1").equals(1)) {
               rpt = "Test 1: Pass";
               bw.write(rpt+"\n");
               JLabel label = new JLabel(rpt);
               label.setBounds(30,30,100,50);
               panel.add(label);
            } else if (cls.getField("test1").equals(2)){
               rpt = "Test 1: Fail";
               bw.write(rpt+"\n");
               JLabel label = new JLabel(rpt);
               label.setBounds(30,30,100,50);
               panel.add(label);
            }
            if (cls.getField("test2").equals(1)) {
               rpt = "Test 2: Pass";
               bw.write(rpt+"\n");
               JLabel label = new JLabel(rpt);
               label.setBounds(30,80,100,50);
               panel.add(label);
            } else if (cls.getField("test2").equals(2)){
               rpt = "Test 2: Fail";
               bw.write(rpt+"\n");
               JLabel label = new JLabel(rpt);
               label.setBounds(30,80,100,50);
               panel.add(label);
            }
            displayArea.add(panel);

            frame.add((Component) constructor.newInstance(), BorderLayout.CENTER);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(cls);
            oos.flush();
            byte [] bytes = bos.toByteArray();

            displayMessage("\nPacket received:" +
                    "\nFrom host: " + receivePacket.getAddress() +
                    "\nHost port: " + receivePacket.getPort() +
                    "\nLength: " + bytes.length +
                    "\nContaining:\n\t" + new String(receivePacket.getData(),
                    0, receivePacket.getLength()));

            frame.setSize(300, 150);
            //frame.setLayout(null);
            frame.setResizable(false);
            frame.setVisible(true);
         }
         catch (IOException | NoSuchMethodException exception)
         {
            displayMessage(exception + "\n");
            exception.printStackTrace();
         } catch (InvocationTargetException e) {
            e.printStackTrace();
         } catch (InstantiationException e) {
            e.printStackTrace();
         } catch (IllegalAccessException e) {
            e.printStackTrace();
         } catch (ClassNotFoundException e) {
            e.printStackTrace();
         } catch (NoSuchFieldException e) {
            e.printStackTrace();
         }
      }
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