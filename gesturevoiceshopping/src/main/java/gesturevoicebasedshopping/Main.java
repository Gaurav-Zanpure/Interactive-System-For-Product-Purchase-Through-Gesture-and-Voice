
package gesturevoicebasedshopping;


import java.awt.Color;
import java.awt.Dimension;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {

   
    public static void main(String[] args) {
        // TODO code application logic here
        
        
//        new TextToSpeechNew().textToSpeech("hello how are you");
//
        try
        {
             Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gesturedb", "root", "root");
           Statement st = conn.createStatement();
                String ges="BLANK",gid1="1";
                String upquery="Update gestureinfo set  gesturename='"+ges+"'where gid='"+gid1+"'";
                st.executeUpdate(upquery);
        }
        catch(Exception ex)
        {
            
        }
        
         
           
          try { 
              
                  new VoiceTextInit().initVT();
              
              
             UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); 
            } catch(ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ignored){}

                
           
            MainFrame lf = new MainFrame();
            lf.setVisible(true);
            lf.setExtendedState(MAXIMIZED_BOTH);
            new PlayAudio().playAudio("C://botvoice//welcome.mp3");

            lf.dispose();
            ProductFrame mf = new ProductFrame();
            mf.setVisible(true); // Show the second frame
            mf.setExtendedState(MAXIMIZED_BOTH); // Hide the first frame
            mf.getContentPane().setBackground(Color.WHITE);
             HandlingThread  ht=new HandlingThread();
            ht.setObject(mf);
            ht.start();
             new PlayAudio().playAudio("C://botvoice//phone.mp3");
            
           
             

        
    }
    
}
