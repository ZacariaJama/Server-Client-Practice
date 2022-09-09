// Zacaria Jama
// 110037765
// 03-27-2022

import javax.swing.JFrame;

public class ServerTest
{
   public static void main(String[] args)
   {
      Server application = new Server();
      application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      application.waitForPackets();
   } 
}

// read a text in C
// run a java file through C
// parent and child? what's a fork?

