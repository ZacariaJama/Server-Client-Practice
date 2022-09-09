// Zacaria Jama
// 110037765
// 03-27-2022

import javax.swing.JFrame;

public class ClientTest
{
   public static void main(String[] args)
   {
      Client application = new Client();
      application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      application.waitForPackets();
   } 
}