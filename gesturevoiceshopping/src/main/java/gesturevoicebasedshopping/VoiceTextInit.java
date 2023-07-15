/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gesturevoicebasedshopping;

import javax.speech.*;
import javax.speech.recognition.*;
import java.io.FileReader;
import java.util.Locale;
public class VoiceTextInit extends ResultAdapter
{
    
    static Recognizer rec;
   public void initVT()
   {
       try {
			// Create a recognizer that supports English.
			rec = Central.createRecognizer(
							new EngineModeDesc(Locale.ENGLISH));
                    System.out.println(rec);
			// Start up the recognizer
			rec.allocate();

			// Load the grammar from a file, and enable it
			FileReader reader = new FileReader("C:\\gram.txt");
                        System.out.println("Read file1");
			RuleGrammar gram = rec.loadJSGF(reader);
			//  System.out.println("Read file2");

			// Add the listener to get results
			rec.addResultListener(new VoiceTextInit());

			// Commit the grammar
			rec.commitChanges();

			// Request focus and start listening
			rec.requestFocus();
			rec.resume();
		} catch (Exception e) {
			e.printStackTrace();
		}
   }
   
   	public void resultAccepted(ResultEvent e)
        {
            try{
		Result r = (Result)(e.getSource());
		ResultToken tokens[] = r.getBestTokens();
              
                String spokenword="";
		//for (int i = 0; i < tokens.length; i++)
                {
                    spokenword=tokens[0].getSpokenText();
                }
			
                spokenword=spokenword.toLowerCase();
                DataKeeper.voice=spokenword;
		  System.out.println("THE WORD YOU SPOKE IS "+spokenword);
                  
                HandlingThread.reply=spokenword;

                 // ProductDetailsFrame.voicestatus=spokenword;
                  

		// Deallocate the recognizer and exit
		//rec.deallocate();
		//System.exit(0);anydesk
            }
            catch(Exception e1){
                System.out.println(e1);
            }
	}
}
