import org.jsfml.graphics.*;

public class Bullet implements Collisionable
{
    private boolean enabled, pixelDestroy;
    private int direction, distance, posX, posY, X, Y, speed;
    private Sprite bulletSprite;
   
    public Bullet(Texture bulletTex,boolean pixelDestroy,int speed)
    {
        enabled = false;
        bulletSprite = new Sprite(bulletTex);
        CollisionDetector.register(this);
        this.pixelDestroy = pixelDestroy;
        this.speed = speed;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setDirection(int direction) {
        this.direction = direction;
    }    
    public void setDistance(int distance) {
        this.distance = distance;
    }  
    public void setPosX(int posX) {
        this.posX = posX;
    }  
    public void setPosY(int posY) {
        this.posY = posY;
    }  
    public void enabled(boolean enabled) {
        this.enabled = enabled;
    }     
    public void setRotation(float direction) {
        bulletSprite.setRotation(direction);
    } 
    public void Proccess(Map map)
    {
        distance+=speed;

        //Rotation matrix maths to calcualte the bullet rotation and position
        float rad = (direction -90 ) * (float)3.142 / (float)180.0;
        float myPointx = (float)Math.cos(rad) * (float)distance;
        float myPointy = (float)Math.sin(rad) * (float)distance;
        bulletSprite.setPosition( (int)myPointx + (int)map.getX() + posX , (int)myPointy + (int)map.getY()+ posY)   ; 
              
        Color pixel = map.getPixel( (int)bulletSprite.getPosition().x - (int)map.getX() , (int)bulletSprite.getPosition().y - (int)map.getY() );
                
        if ( pixel.a == 255 && pixelDestroy){ // remove bullet if it hits the map
              map.remove((int)bulletSprite.getPosition().x , (int)bulletSprite.getPosition().y,7);
              enabled(false);
        }else if( pixel.a == 255  ){
             enabled(false);  
        }                
        CollisionDetector.update(this);       
    }      
    public Sprite getSprite() {  
        return bulletSprite;
    }     
    public int getX() {
        return (int)bulletSprite.getPosition().x; 
    }
    public int getY() {
        return (int)bulletSprite.getPosition().y;
    }   
}