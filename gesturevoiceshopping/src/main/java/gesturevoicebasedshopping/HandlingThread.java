/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gesturevoicebasedshopping;

import java.awt.Color;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;


public class HandlingThread extends Thread
{
    
    ProductFrame pp;
    
    public static String reply="";
    
    public static int statusint=0;
    
    public void setObject(ProductFrame p)
    {
        pp=p;
    }
    public void run()
    {
        
        try
        {
            
            System.out.println("Thread Started ================");
            
               Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gesturedb", "root", "root");
           Statement st = conn.createStatement();
        while(true)
        {
            
           
            
             String query="Select *from gestureinfo where gid=1";
           //  System.out.println("query "+query);
              ResultSet rs1=st.executeQuery(query);
             String gesturename="";
             if(rs1.next())
            {

              gesturename=rs1.getString(2);
              
              String ges="BLANK",gid1="1";
                String upquery="Update gestureinfo set  gesturename='"+ges+"'where gid='"+gid1+"'";
                st.executeUpdate(upquery);
          
              //break;
            }
            if(gesturename.length()>0)
            {
                  System.out.println("gesturename "+gesturename);
                  
                  if(gesturename.equals("BACK"))
                  {
                       ObjectKeeper.obpd.funBack();
                  }
                  else if(gesturename.equals("BUY"))
                  {
                      reply="";
                      ObjectKeeper.obpd.tokenGeneration();
                      statusint=1;
                      
                  }
                  else if(gesturename.equals("MORE INFO"))
                  {
                      pp.productDetails(); 
                  }
                  else if(gesturename.equals("NEXT"))
                  {
                       pp.funNext();
                  }
                  else if(gesturename.equals("PREVIOUS"))
                  {
                      ObjectKeeper.obpd.funBack();
                  }
                
                
            }
            else
            {
                System.out.println("No Gestures");
            }
            
            
              if(reply.equals("yes") && statusint==1)
            {
                ProductFrame.count=1;
                reply="";
                 statusint=0;
                    ObjectKeeper.pf.dispose();
                 ProductFrame mf = new ProductFrame();
                 mf.setVisible(true); // Show the second frame
                 mf.setExtendedState(MAXIMIZED_BOTH); // Hide the first frame
                 mf.getContentPane().setBackground(Color.WHITE);
                 
                   
               //  file1.delete();
            }
              
               if(reply.equals("no") && statusint==1)
            {
                ProductFrame.count=1;
                reply="";
                statusint=0;
                String itemslist="Purchased Product List :";
                double totalprice=0.0;
        
                for (int i = 0; i <DataKeeper.itemlist.size(); i++) 
                {
                    ArrayList row=(ArrayList) DataKeeper.itemlist.get(i);
                    itemslist=itemslist+"\n"+row.get(0);
                    String rate=(String) row.get(1);
                    rate=rate.replace(",", "");
                    System.out.println("Rate: "+rate);
                    double price =Double.parseDouble(rate);
                    totalprice=totalprice+price;
                }
                
                String msg="Total Price of the Products: "+"Rs."+Double.toString(totalprice);
                String token= new DecimalFormat("0000").format(new Random().nextInt(9999));
                itemslist=itemslist+"\n"+msg+"\n"+"Generated Token for the Payment: "+token+"\n\n"+
                        "Thanks for the Shopping at GRAB Electronics";
                
               
               //  JOptionPane.showMessageDialog(null, itemslist);
                  ObjectKeeper.pf.dispose();
                 ExitFrame lf = new ExitFrame();
                 lf.setVisible(true);
                 lf.setExtendedState(MAXIMIZED_BOTH);
                 lf.jTextArea1.setText(itemslist);
              //   new PlayAudio().playAudio("D://botvoice//welcome.mp3");
                // file1.delete();
                 
            }
            Thread.sleep(2000);
          
//                pp.funNext();
//                
//                Thread.sleep(5000);
//                pp.productDetails();
//                              
//                Thread.sleep(5000);
//                ObjectKeeper.obpd.funBack();
//                Thread.sleep(1000);
//                
//                 pp.funNext();
//                
//                Thread.sleep(5000);
//                pp.productDetails();
//                              
//                Thread.sleep(5000);
//                ObjectKeeper.obpd.funBack();
//                
//                 pp.funNext();
//                
//                Thread.sleep(5000);
//                pp.productDetails();
//                              
//                Thread.sleep(5000);
//                ObjectKeeper.obpd.funBack();
//                
//                
//                
//                 pp.funNext();
//                
//                Thread.sleep(5000);
//                pp.productDetails();
//                              
//                Thread.sleep(5000);
//                ObjectKeeper.obpd.funBack();
               
//                break;
                
                
                
                
                
                
                
                
            }
        }
         catch(Exception ex)
            {
                System.out.println("Exception ex "+ex);
            }
           
            
            
            
            
        }
    }

