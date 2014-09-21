import org.jsfml.graphics.*;
import java.nio.file.Paths;
import java.io.IOException;
import java.lang.Math.*;
import org.jsfml.system.Clock;

public class Rocket implements Collisionable 
{
    private static final float speed = 3;
    private Sprite RocketSprite;
    private boolean gravityEnabled = true;
    private Clock deaccelerateClock;
    private boolean firstTime = true;
    private Gun gun;
    private Map map; 
    
    public Rocket(String image, float posX,float posY,Map map) 
    {
        Texture RocketIMG = new Texture();
        try{ RocketIMG.loadFromFile(Paths.get(image)); }catch(IOException e1) {}  
        RocketSprite = new Sprite(RocketIMG);
        RocketSprite.setTexture(RocketIMG);
        RocketSprite.setPosition(posX, posY);
        RocketSprite.setScale(1.0f, 1.0f);   
        this.map = map;
        gun = new Gun(map,true,"Green",1);
        
        CollisionDetector.register(this);
    }

    public Sprite getSprite() {
        return RocketSprite;
    } 
    public void moveFoward(boolean resetClock){
        RocketSprite.move((float)Math.sin(3.14159265 * (RocketSprite.getRotation()) / 180.f) * speed,-1 * (float)Math.cos(3.14159265 * (RocketSprite.getRotation()) / 180.f) * speed);   

        if (firstTime){
           deaccelerateClock = new Clock();
           firstTime = false; 
        }else if (resetClock)
           deaccelerateClock.restart(); 
    }
    public void rotate(int rotate){
        RocketSprite.rotate(rotate);     
    } 
    public int getX(){
         return (int)RocketSprite.getPosition().x;
    }     
    public int getY(){
         return  (int)RocketSprite.getPosition().y;
    }   
    public void move(int x, int y){
         RocketSprite.setPosition(getX() + x,getY() + y);
         CollisionDetector.update(this); 
    }  
    public void gravity(float y){
         RocketSprite.setPosition(getX(),RocketSprite.getPosition().y + y);
        
         if( firstTime == false && deaccelerateClock.getElapsedTime().asMilliseconds() < 200 )
            moveFoward(false);          
    }  
    public boolean getGravity(){
         return gravityEnabled;
    }    
    public void setGravity(boolean gravityEnabled ){
         this.gravityEnabled = gravityEnabled;
    }  
    public void fireGun( ){
        gun.fireBullet((int)getSprite().getRotation(),getX(),getY());
        //testing: gun.fireBullet(0,400,280);   
    }  
    public Gun getGun( ){
        return gun;
    }  
    public boolean isEnabled() {
        return true;
    }  
    public void enabled(boolean enabled) {
        Score.updateScore(1, 1);
    }         
}