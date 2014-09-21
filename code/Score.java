import java.io.IOException;
import java.nio.file.Paths;
import org.jsfml.graphics.*;

/** Class Score - Note: while the score class works, I ran out of time to sync it correctly */

public class Score
{
    private static Text text;
    private static Font font;
    private static int player1Score = 3, player2Score = 0,ps;
    private static String us1="", us2="";
    
    public static void start(int playerStart)// displas if "us" is player 1 or player 2
    {
        ps = playerStart;
        if(playerStart==1){ us1 = "(us-arrow & space keys) ";} else {us2 = "(us-was & d keys) ";};
        font = new Font();
        try { font.loadFromFile(Paths.get("C:/Windows/Fonts/lucon.ttf")); } catch( IOException e1 ) { } 
        text = new Text("player 1 "+ us1 + "score: 0 \nplayer 2 "+ us2 + "score: 0" ,font, 20);
        text.setColor(new Color(224,74,77));  
    }
    public static void updateScore(int player,int score)
    {
        if(ps==1){
          text.setString("(score not 100% working yet)\nplayer 1 "+ us1 + "score: " + (player1Score=player1Score+score) + "\nplayer 2 "+ us2 + "score: " + player2Score);  
        }else{
           text.setString("score not 100% working yet)\nplayer 1 "+ us1 + "score: " + player1Score + "\nplayer 2 "+ us2 + "score: " + (player2Score=player2Score+score));  
        }
    }
    public static Text getText(){
        return text;
    }
}