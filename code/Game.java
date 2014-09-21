import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.*;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import org.jsfml.window.*;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Vector2i;
import java.nio.file.Paths;
import java.io.IOException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.net.MalformedURLException;
import java.util.*;

/** Class Game - starts the game and draws all object on the window
 *
 * The game is broken up into Model,View, Controller with a load class, to load the level.
 * 
 * @author  Aidan Brown
 * @version 1 */

public class Game extends UnicastRemoteObject implements RmiServerIntf {      
  private static final int Maxitems = 14;
  private RmiServerIntf rmiServerObj = null; 
  private HashMap<Integer,Rocket> rockets;
  private Controller controller1;
  private Map map1;
  private RenderWindow window;
    
  public static void main(String[] args) {
      try { 
           Game g = new Game(); 
           if(args[0]=="1") 
               g.start(1); //single player start
           else
               g.rmiMultiplayerStart();   
      } catch (RemoteException e) {}
  }   
  public Game() throws RemoteException {
      super(0);
  }       
  private void rmiMultiplayerStart(){
        try { //server
            LocateRegistry.createRegistry(1099); //special exception handler for registry creation
            Game Obj = new Game();
     
            try {   // Bind this object instance to the name "RmiServer"
                 Naming.rebind("//localhost/RmiServer", Obj ); // PeerServer bound in registry
                 Obj.start(1);
            } catch ( MalformedURLException f) { }
            
        } catch (RemoteException e) { //client
            
            try {  
                rmiServerObj = (RmiServerIntf)Naming.lookup("//localhost/RmiServer");            
                start(2);
            } catch ( NotBoundException f) {
            } catch ( MalformedURLException h) {
            } catch ( RemoteException i) { }
        }   
  }
  private void start(int rocketID) {
        window = new RenderWindow();
        VideoMode VMode = new VideoMode(1024, 600, 32);
        window.create( VMode, "Space Battle - By Aidan");
        window.setFramerateLimit(70);//Limit the framerat
        Score.start(rocketID); 
   
        Image img  = new Image();//background
        try { img.loadFromFile(Paths.get("files/wikipediaStars.jpg")); } catch( IOException z1 ) {}  //    files\\space
        Texture BackgroundSpaceIMG = new Texture();
        
        try { BackgroundSpaceIMG.loadFromImage(img); } catch( TextureCreationException e1 ) { }        
        Sprite BackgroundSpaceSprite = new Sprite(BackgroundSpaceIMG);
        BackgroundSpaceSprite.setPosition(0.0f, 0.0f); 
                   
        map1 = new Map();
        rockets =  new HashMap<Integer,Rocket>();
        
        if(rocketID == 1){   
             rockets.put(1,new Rocket("files/rocket1.png",12.0f, 230.0f,map1));
             rockets.put(2,new Rocket("files/rocket2.png",12.0f, 230.0f,map1));
        }else{
             rockets.put(2,new Rocket("files/rocket1.png",12.0f, 230.0f,map1));
             rockets.put(1,new Rocket("files/rocket2.png",12.0f, 230.0f,map1));  
        }
        rockets.get(2).getSprite().setPosition(500,500);
        
        Camera camera1 = new Camera( rockets,map1,VMode ); 
        controller1 = new Controller( rockets,rmiServerObj,rocketID,map1 ); 
        
        Thread gameloop2Thread = new Thread(){ 
            public void run(){
                while(true){ //Second game loop to multithread
                    controller1.proccessKeys();
                    map1.proccessCollisions(rockets.get(1).getX(),rockets.get(1).getY(), 7);
                    map1.proccessCollisions(rockets.get(2).getX(),rockets.get(2).getY(), 7); 
                    try { Thread.sleep(14); } catch (InterruptedException e) {}
                }
            }
        };
        gameloop2Thread.start();   
        
        if(rocketID==1) window.setPosition(new Vector2i (1,1)) ;
        
        while(window.isOpen()) { //Main game loop 1
           map1.proccessItems();        
           window.clear(Color.RED);
           window.draw(BackgroundSpaceSprite);
           window.draw(map1.getWaterSprite());          
           window.draw(map1.getSprite());//level 
           renderRockets(); 
           renderItems();
           window.draw(Score.getText());
           window.display();

           for(Event event : window.pollEvents()) { //Handle quit game
               if(event.type == Event.Type.CLOSED)
                   System.exit(1);
           } 
           camera1.follow();   
        }         
  }
  
  private void renderRockets(){
      for( int ii = 1; ii < 3; ii++ ){ //rocket1 and 2
          map1.proccessGravity(rockets.get(ii));
          window.draw(rockets.get(ii).getSprite());
          rockets.get(ii).getGun().Proccess();
          for( int j = 0; j < 9; j++ ) {
              if ( rockets.get(ii).getGun().getBulletSpaceEnabled(j) )
                  window.draw(rockets.get(ii).getGun().getSprite(j));
          } 
      }       
  }
  
  private void renderItems(){
      for( int i = 0; i < Maxitems; i++ ){
          if ( map1.isItemEnabled(i))
               window.draw(map1.getItemSprite(i));
      }
          
      for( int h = 12; h < 14; h++ ){ //shooting items
          for( int i = 0; i < 2; i++ ){
              ((shootingItem)map1.getItem(h)).getGun(i).Proccess();
              ((shootingItem)map1.getItem(h)).Process();
                
               for( int j = 0; j < 9; j++ ){
                   if ( ((shootingItem)map1.getItem(h)).getGun(i).getBulletSpaceEnabled(j) )
                       window.draw(((shootingItem)map1.getItem(h)).getGun(i).getSprite(j));
               } 
          }  
      }  
  }
  
  public HashMap<String,Integer> rocketRmiSync(int x, int y, int direction,boolean bulletPress,boolean gravity ){
       rockets.get(2).getSprite().setPosition(x + map1.getSprite().getPosition().x,y + map1.getSprite().getPosition().y);
       rockets.get(2).getSprite().setRotation(direction);
       rockets.get(2).setGravity(gravity);
         
       if (bulletPress)
           rockets.get(2).fireGun();       
       
       HashMap<String,Integer> networkQue =  new HashMap<String,Integer>();
       networkQue.put("rotation", (int)rockets.get(1).getSprite().getRotation());
       networkQue.put("x", rockets.get(1).getX()  - map1.getX() );
       networkQue.put("y", rockets.get(1).getY()  - map1.getY() );  
       networkQue.put("bulletPress", (controller1.getBulletPress()) ? 1 : 0  ); //cast boolean to int 
       networkQue.put("gravity", rockets.get(1).getGravity()  ? 1 : 0  ); //cast boolean to int 
       
       return  networkQue;
  }
}