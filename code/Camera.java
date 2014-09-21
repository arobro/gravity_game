import org.jsfml.graphics.*;
import org.jsfml.window.*;
import java.util.*;

/** Class Camera - the camera follows the rocket on a map.
 *
 * when the rocket moves to the edge of the screen, move the camera to follow the rocket
 * The camera does not move when the rocket is in the middle of the screen
 * 
 * @author  Aidan Brown
 * @version 1 */

public class Camera
{
    HashMap<Integer,Rocket> rockets;
    Map map;
    VideoMode VMode;
    boolean ShipEdgeX,ShipEdgeY,ShipEdgeBackwardsX,ShipEdgeBackwardsY;
 	
    public Camera(HashMap<Integer,Rocket> rockets,Map map,VideoMode VMode) 
    {
        this.rockets = rockets;;
        this.map = map;  
        this.VMode = VMode;  
    }
    
    public void follow()  //camera follow
    {      
        //fowards     
        if (rockets.get(1).getX() > VMode.width * .6 && ShipEdgeX == false){
                map.move(-1,0);
                rockets.get(1).move(-1,0);
        }
        if (rockets.get(1).getX()  > VMode.width * .9 ||  ShipEdgeX == true ){
                map.move(-3,0);
                rockets.get(1).move(-4,0);
                ShipEdgeX = true;     
        }
        if (rockets.get(1).getX()  < VMode.width * .6 ){
                ShipEdgeX = false;
        }
        //backwards
        if (rockets.get(1).getX() < VMode.width * .4 && ShipEdgeBackwardsX == false){
               map.move(1,0);
               rockets.get(1).move(1,0);
        }
        if (rockets.get(1).getX()  < VMode.width * .1 || ShipEdgeBackwardsX == true){
                map.move(3,0);
                rockets.get(1).move(4,0);
                ShipEdgeBackwardsX = true;      
        }
        if (rockets.get(1).getX() > VMode.width * .4 ){ 
                ShipEdgeBackwardsX = false;
        }
        //down
        if (rockets.get(1).getY() > VMode.height * .6 && ShipEdgeY == false){
                map.move(0,-1);
                rockets.get(1).move(0,-1);
        }

        if (rockets.get(1).getY() > VMode.height * .9 || ShipEdgeY == true){
                map.move(0,-3);
                rockets.get(1).move(0,-4);
                ShipEdgeY = true;
        }
        if (rockets.get(1).getY() < VMode.height * .6){
                ShipEdgeY = false;
        }
       //up
        if (rockets.get(1).getY() < VMode.height * .4 && ShipEdgeBackwardsY == false ){
                map.move(0,1);
                rockets.get(1).move(0,1);
        }
        if (rockets.get(1).getY() < VMode.height  * .1 || ShipEdgeBackwardsY == true){
                map.move(0,3);
                rockets.get(1).move(0,4);
                ShipEdgeBackwardsY = true;
        }
        if (rockets.get(1).getY() > VMode.height * .4 ){
                ShipEdgeBackwardsY = false;
        }
    }
}