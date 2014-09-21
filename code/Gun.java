import org.jsfml.graphics.*;
import java.nio.file.Paths;
import java.io.IOException;
import org.jsfml.system.*;
import java.util.*;

public class Gun
{
    private Map map;
    private Clock clockBullet;
    private HashMap<Integer,Bullet> bulletSpace; //magazine
    private static final int Maxitems = 12;
       
    public Gun(Map map,boolean pixelDestroy,String bulletTexID,int speed)
    {
        this.map = map;
        clockBullet = new Clock();
        Texture bulletTex = new Texture(); // bullet
        bulletSpace =  new HashMap<Integer,Bullet>();

        try{ bulletTex.loadFromFile(Paths.get("files/bullet" + bulletTexID + ".png")); }catch(IOException e1) {}      
             
        for( int i = 0; i < 10; i++ ){
           bulletSpace.put(i, new Bullet(bulletTex,pixelDestroy,speed));
        }      
    }
    
    public void fireBullet(int direction , int posX, int posY ){
        //fire if last bullet was fired over 100ms ago
        if ( clockBullet.getElapsedTime().asMilliseconds() > 100 ){
            int lowestbulletNo = 10;
            for( int i = 9; i > 0; i-- ){
                if ( bulletSpace.get(i).isEnabled() == false){
                    lowestbulletNo = i;//find the first bullet in the gun barrol
                }
            }

            if ( lowestbulletNo < 10 ){
               bulletSpace.get(lowestbulletNo).setDirection(direction);
               bulletSpace.get(lowestbulletNo).setDistance(0);
               bulletSpace.get(lowestbulletNo).setPosX(posX - map.getX()) ;
               bulletSpace.get(lowestbulletNo).setPosY(posY - map.getY()) ;
               bulletSpace.get(lowestbulletNo).enabled(true);
               bulletSpace.get(lowestbulletNo).setRotation(direction+1);
           }
        }
        clockBullet.restart();
    }     
    public void Proccess() {
        for( int i = 0; i < 9; i++ ){
            if (bulletSpace.get(i).isEnabled())              
                bulletSpace.get(i).Proccess(map);                            
        }        
    }
    public boolean getBulletSpaceEnabled(int bulletNo) { 
        return bulletSpace.get(bulletNo).isEnabled();
    }    
    public Sprite getSprite(int bulletNo) {  
        return bulletSpace.get(bulletNo).getSprite();
    }       
}