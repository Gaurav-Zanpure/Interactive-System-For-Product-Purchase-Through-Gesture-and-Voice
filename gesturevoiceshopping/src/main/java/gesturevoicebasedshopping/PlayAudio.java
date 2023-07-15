/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gesturevoicebasedshopping;
import javazoom.jl.player.Player;
import java.io.FileInputStream;



public class PlayAudio {
    
    public void playAudio(String audiopath)
    {
        try {
        FileInputStream fileInputStream = new FileInputStream(audiopath);
        Player player = new Player((fileInputStream));
        player.play();
         DataKeeper.player=player;
    }catch (Exception e){
        System.out.println(e);
    }
    }
    
     
}
