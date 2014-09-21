import org.jsfml.graphics.*;
import org.jsfml.system.Time;
import org.jsfml.system.Clock;
import java.util.*;

public class shootingItem extends Item
{
    Clock clockBullet;
    HashMap<Integer,Gun > guns; 

    public shootingItem(Texture itemTex, Map map)
    {
        super(itemTex);
        guns = new HashMap<Integer,Gun>();  
        guns.put(0, new Gun(map,false,"Yellow",3));
        guns.put(1, new Gun(map,false,"Yellow",3));
         
        clockBullet = new Clock();
    }
    public void Process()
    {
        if ( clockBullet.getElapsedTime().asMilliseconds() > 500 && isEnabled() ) {
            guns.get(0).fireBullet(200,getX(),getY()+20); 
            guns.get(1).fireBullet(150,getX()+20,getY()+20); 
            clockBullet.restart();  
        }
    }
    public Gun getGun( int gunNo) {
         return guns.get(gunNo);
    }          
}