import org.jsfml.graphics.*;
import java.nio.file.Paths;
import java.io.IOException;
import org.jsfml.system.Time;
import org.jsfml.system.Clock;
import java.util.*;

public class Gun
{
    Map map;
    Clock clock3;
    HashMap<Integer,Sprite> objectsSprite;
    HashMap<Integer,Bullet> bulletSpace;//magazine
    static int Maxitems = 12;
    static int crystalRadious = 17; 
       
    public Gun(Map map)
    {
        this.map = map;
        clock3 = new Clock();
        
        // bullet
        Texture bulletTex = new Texture();
        try{ bulletTex.loadFromFile(Paths.get("files/bullet.png"));  }catch(IOException e1) {}  
         
         
        objectsSprite = new HashMap<Integer,Sprite>();
        bulletSpace =  new HashMap<Integer,Bullet>();
        
        for( int i = 0; i < 10; i++ ){
            objectsSprite.put(i,new Sprite(bulletTex));
            bulletSpace.put(i, new Bullet());
            CollisionDetector.register(bulletSpace.get(i));
        }      
    }
    
    public void fireBullet(int direction , int posX, int posY ){
        Time elapsed3 = clock3.getElapsedTime();
            if (  elapsed3.asMilliseconds() > 100){
                int lowestbulletNo = 10;
                for( int i = 9; i > 0; i-- ){
                    if ( bulletSpace.get(i).isEnabled() == false){
                        lowestbulletNo = i;
                    }
                }

                if ( lowestbulletNo < 10 ){
                    bulletSpace.get(lowestbulletNo).setDirection(direction);
                    bulletSpace.get(lowestbulletNo).setDistance(0);
                    bulletSpace.get(lowestbulletNo).setPosX(posX - map.getX()) ;
                    bulletSpace.get(lowestbulletNo).setPosY(posY - map.getY()) ;
                    bulletSpace.get(lowestbulletNo).enabled(true);
                    objectsSprite.get(lowestbulletNo).setRotation(direction+1);
                }
            }
            clock3.restart();
        }   
        
public void Proccess(  )
{
    for( int i = 0; i < 9; i++ ){
        if (bulletSpace.get(i).isEnabled() ){

              bulletSpace.get(i).distance++;

              //Rotation_matrix maths
              float rad = (bulletSpace.get(i).getDirection() -90 ) * (float)3.142 / (float)180.0;
              float myPointx = (float)Math.cos(rad) * (float)bulletSpace.get(i).getDistance();
              float myPointy = (float)Math.sin(rad) * (float)bulletSpace.get(i).getDistance();
              
                 
              objectsSprite.get(i).setPosition( (int)myPointx + (int)map.getX() + (int)bulletSpace.get(i).getPosX() , (int)myPointy + (int)map.getY()+ (int)bulletSpace.get(i).getPosY( ))   ;
            
              Color pixel = map.getPixel( (int)objectsSprite.get(i).getPosition().x - (int)map.getX() , (int)objectsSprite.get(i).getPosition().y - (int)map.getY() );

              if (pixel.a == 255){
                  map.remove(  (int)objectsSprite.get(i).getPosition().x , (int)objectsSprite.get(i).getPosition().y,7);
                  bulletSpace.get(i).enabled(false);
             }
             
                            
             bulletSpace.get(i).setX((int)objectsSprite.get(i).getPosition().x- map.getItem(0).getXOffset());
             bulletSpace.get(i).setY((int)objectsSprite.get(i).getPosition().y);
             CollisionDetector.update(bulletSpace.get(i));
                                       
         }
        
    }        
}

   public boolean getBulletSpaceEnabled(int bulletNo)
    { 
        return bulletSpace.get(bulletNo).isEnabled();
    }   
        
    public Sprite getSprite(int bulletNo)
    {  
        return objectsSprite.get(bulletNo);
    }       
}