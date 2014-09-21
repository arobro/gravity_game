import org.jsfml.window.event.Event;
import org.jsfml.window.*;
import org.jsfml.graphics.*;
import java.rmi.RemoteException;
import java.util.*;

public class Controller
{
    private HashMap<Integer,Rocket> rockets;
    private RmiServerIntf rmiServerObj;
    private int rocketID;
    private Camera camera;
    private Map map;
    private boolean bulletPress = false;
    
    public Controller(HashMap<Integer,Rocket> rockets,RmiServerIntf rmiServerObj,int rocketID,Map map)
    {
        this.rockets = rockets;
        this.rmiServerObj = rmiServerObj;
        this.rocketID = rocketID;
        this.map = map;
    }
    
    public boolean getBulletPress(){
        boolean retunBulletPress = bulletPress;
        bulletPress = false; // reset bulet press
        return retunBulletPress;
    }

    public void proccessKeys()
    {
        if(rocketID==1){ // player 1
            if (Keyboard.isKeyPressed(Keyboard.Key.UP)) {
                rockets.get(1).moveFoward(true); 
                rockets.get(1).setGravity(true);             
            }          
            if (Keyboard.isKeyPressed(Keyboard.Key.LEFT)) // Rotate the sprite
               rockets.get(1).rotate(-1);
            if (Keyboard.isKeyPressed(Keyboard.Key.RIGHT))
                rockets.get(1).rotate(1);
            if (Keyboard.isKeyPressed(Keyboard.Key.SPACE)){
                  rockets.get(1).fireGun();
                  bulletPress = true;
            }    
            
        }else{ // player 2
            
            if (Keyboard.isKeyPressed(Keyboard.Key.W)){
               rockets.get(1).moveFoward(true);  
               rockets.get(1).setGravity(true);
            }           
            if (Keyboard.isKeyPressed(Keyboard.Key.A)) // Rotate the sprite
                rockets.get(1).rotate(-1);    
            if (Keyboard.isKeyPressed(Keyboard.Key.S))
                rockets.get(1).rotate(1); 
            if (Keyboard.isKeyPressed(Keyboard.Key.D)){
                 rockets.get(1).fireGun();
                 bulletPress = true;       
            }         

            synckRocketRMI();
        }  
    } 
    
    private void synckRocketRMI()
    {
        try { // sync rockets using rmi  
            HashMap<String,Integer> networkQue =  rmiServerObj.rocketRmiSync((int)rockets.get(1).getX()  - map.getX(),
            (int)rockets.get(1).getY()  - map.getY() , (int)rockets.get(1).getSprite().getRotation(),getBulletPress(),rockets.get(1).getGravity()); 
                
            rockets.get(2).getSprite().setPosition( networkQue.get("x")  + (int)map.getSprite().getPosition().x,networkQue.get("y")  + (int)map.getSprite().getPosition().y);
            rockets.get(2).getSprite().setRotation( networkQue.get("rotation"));     
            rockets.get(2).getSprite().setRotation( networkQue.get("rotation"));     
            rockets.get(2).setGravity((boolean)(networkQue.get("gravity") != 0));//cast int to boolean
                       
            if (networkQue.get("bulletPress")==1)
                rockets.get(2).fireGun();     

       } catch ( RemoteException i) {}        
    }     
}