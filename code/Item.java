import org.jsfml.graphics.*;

public class Item implements Collisionable
{
    private boolean enabled;
    private Sprite itemSprite;

    public Item(Texture itemTex) {
        enabled = true;
        itemSprite = new Sprite(itemTex);
    }
    public boolean isEnabled() {
        return enabled;
    }    
    public Sprite getSprite() {
        return itemSprite;
    }          
    public void enabled(boolean enabled) {
        this.enabled =  enabled;
        Score.updateScore(1, +1);
    }   
  
    public int getX(){ return (int)itemSprite.getPosition().x;};//for collison dector
    public int getY(){ return (int)itemSprite.getPosition().y;}; 
}