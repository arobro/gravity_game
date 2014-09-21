import org.jsfml.graphics.*;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.*;
import org.jsfml.system.Clock;
import org.jsfml.graphics.Image.PixelOutOfBoundsException;

public class Map
{
    private Sprite Sprite2;
    private Image Level;
    private Texture LevelTex;
    private Sprite WaterSprite;
    
    private static final int waterHeight = 500,Maxitems = 14;
    private Clock animationClock;
    private HashMap<Integer,Item > items; 
    private int weirdAnimalPos = 0;  
    private boolean weirdAnimalDirectionSwap ;
   
    private int[] animationCount = {0,0,0};
    private  ArrayList<IntRect> tex1 = new ArrayList<IntRect>();
    private  ArrayList<IntRect> tex2 = new ArrayList<IntRect>();
    private  ArrayList<IntRect> tex3 = new ArrayList<IntRect>();
    private  XmlLevelLoader xmlLevelLoader ;
    
    public Map()
    {
         animationClock = new Clock();
         xmlLevelLoader = new XmlLevelLoader();
         Level  = new Image();
         try{  Level.loadFromFile(Paths.get("files/" + xmlLevelLoader.getCustomCommand("levelBackground")));  }catch(IOException e1) {}  
         Level.createMaskFromColor(Color.BLACK);
         LevelTex = new Texture(); 
         Sprite2 = new Sprite();
         Sprite2.setPosition(10.0f, 10.0f); 
         
         try{ LevelTex.loadFromImage(Level);  }catch(TextureCreationException e1){}
         Sprite2.setTexture(LevelTex);      
         
         Texture waterTex = new Texture(); //water
         try{ waterTex.loadFromFile(Paths.get("files/water.png"));  }catch(IOException e1) {}  
         WaterSprite = new Sprite();
         WaterSprite.setTexture(waterTex);
         WaterSprite.setScale(4,4);
        
         items = new HashMap<Integer,Item>();
        
         Texture objectsTex = new Texture(); // sprite with energy crystal
         try{ objectsTex.loadFromFile(Paths.get("files/Sprites.tga"));  }catch(IOException e1) {}  
        
         for( int i = 0; i < Maxitems-1 ; i++ ){ // items
            items.put(i, new Item(objectsTex));
            CollisionDetector.register(items.get(i));
         }
         
         //shooting items
         for( int i = Maxitems -2; i < Maxitems; i++ ){
            items.put(i,new shootingItem(objectsTex,this) );    
            CollisionDetector.register(items.get(i));
         } 
         //the coodordiantes of the textures from the sprite file
         tex1.add(new IntRect(1,345,45,40));  tex1.add(new IntRect(48,345,45,40));
         tex1.add(new IntRect(96,345,45,40)); tex1.add(new IntRect(142,345,45,40));
         tex2.add(new IntRect(0,108,6,8));    tex2.add(new IntRect(6,108,6,8));
         tex2.add(new IntRect(12,108,6,8));   tex3.add(new IntRect(204,52,31,31));
         tex3.add(new IntRect(237,52,31,31)); tex3.add(new IntRect(270,52,31,31));          
    }
    
    public boolean isItemEnabled (int itemNo){
        return  items.get(itemNo).isEnabled();   
    }   
    public Sprite getItemSprite (int itemNo){
        return  items.get(itemNo).getSprite();   
    }   
    public Item getItem (int itemNo){
        return  items.get(itemNo);  
    }     
    public void move (int x, int y){
        Sprite2.move(x,y);  
    }
    public int getX (){
        return (int)Sprite2.getPosition().x;      
    }
    public int getY (){
        return (int)Sprite2.getPosition().y;      
    }
    public Color getPixel (int pixelX, int pixelY){
        try { return  Level.getPixel( pixelX  ,pixelY );    } catch ( PixelOutOfBoundsException  f) { }
        return Level.getPixel( 1  ,1 ); 
    }     
    public void setPixel (int pixelX, int pixelY,Color color){
         try{  Level.setPixel(pixelX, pixelY,color);   }catch(PixelOutOfBoundsException  f) {}   
    }     
    public void repaint (){
             try{  LevelTex.loadFromImage(Level);  }catch(TextureCreationException e1) {}        
             Sprite2.setTexture(LevelTex);    
    }    
    
    //put a bullet hole on the map
    public void remove (int centerX, int centerY, int radious){
         for (int x = -radious; x < radious ; x++){
            int height = (int)Math.sqrt(radious * radious - x * x);
            for (int y = -height; y < height; y++){
                 setPixel( centerX - (int)Sprite2.getPosition().x +x,centerY - (int)Sprite2.getPosition().y-y,new Color(255, 0, 255,0));
            }
         }
         repaint();   
    }  
    public Sprite getSprite() {
        return Sprite2;
    } 
    public Sprite getWaterSprite() {
        return WaterSprite;
    }  
    
    public void proccessCollisions(int centerX, int centerY, int radious) {
        if (getPixel(centerX-getX(), centerY-getY()).a == 255){ //rocket pixel collision check
            remove (centerX,centerY,radious);
            Score.updateScore(1, -1);
        }       
        WaterSprite.setPosition(0.0f, waterHeight + getY());  //water 
    }   
       
    public void proccessItems() {
        if( animationClock.getElapsedTime().asMilliseconds() > 100 ){
            proccessItemsAnimationClock(0,2,tex1,0);
            proccessItemsAnimationClock(2,12,tex2,1);
            proccessItemsAnimationClock(13,14,tex3,2);
            animationClock.restart();
        }    

        for( int i = 0; i < Maxitems; i++ ){   
            items.get(i).getSprite().setPosition(xmlLevelLoader.itemGetX(i) + Sprite2.getPosition().x,xmlLevelLoader.itemGetY(i) + Sprite2.getPosition().y);
        }
        items.get(12).getSprite().setTextureRect(new IntRect(105, 59, 24, 21));
        
        if (weirdAnimalPos < 10 ) weirdAnimalDirectionSwap = true; //weird animal
        if (weirdAnimalPos > 300) weirdAnimalDirectionSwap = false;
        if (weirdAnimalDirectionSwap) weirdAnimalPos++;  
        else weirdAnimalPos--;
        items.get(13).getSprite().setPosition(items.get(13).getX()+ weirdAnimalPos,items.get(13).getY()); 
    }
    
    private void proccessItemsAnimationClock(int min1,int max1,  ArrayList<IntRect> tex,int acID) {
       for( int i = min1; i < max1; i++ ){ 
           if ( items.get(i).isEnabled()) { 
               items.get(i).getSprite().setTextureRect(tex.get(animationCount[acID]));  
           }
           animationCount[acID]++;
           if( animationCount[acID] == tex.size() ) 
                animationCount[acID] = 0;   
       } 
    }
    
    public void proccessGravity(Rocket rocket)
    {
        try {    
            Color pixel2 = Level.getPixel( rocket.getX() - getX() ,rocket.getY() - getY() +21);
            if (pixel2.r == 0 && pixel2.g == 251 && pixel2.b == 250 ){ //rocket is on the base
               rocket.setGravity(false);
            }    
      
            if( rocket.getY() < waterHeight + getY() +15 && rocket.getGravity()){
                rocket.gravity(0.5f);
            }else if (rocket.getGravity()){
                rocket.gravity(-1.0f);
            }  
        } catch ( PixelOutOfBoundsException  f) { }    
    }       
}