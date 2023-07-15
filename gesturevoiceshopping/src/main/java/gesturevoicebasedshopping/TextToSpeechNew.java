/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gesturevoicebasedshopping;
import com.sun.speech.freetts.Voice;  
import com.sun.speech.freetts.VoiceManager;


public class TextToSpeechNew {
    
    private static final String VOICENAME_kevin = "kevin16";
    
    public void textToSpeech(String message)
    {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
       
        Voice voice;
        VoiceManager voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice(VOICENAME_kevin);
        voice.allocate();

        voice.speak(message);
       
    

    }
    
}