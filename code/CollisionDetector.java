import java.util.*;

/** Class CollisionDetector - keeps track of all objects and when there is a
 * collision call them in their own time to destroy them selfs.
 *
 * @author  Aidan Brown
 * @version 1 */

public class CollisionDetector 
{
   private static final int radious = 17;
   static ArrayList<Collisionable> collisionableItems = new ArrayList<Collisionable>();

   public static void register(Collisionable c ){
           collisionableItems.add(c);
   };

   public static void update(Collisionable c){     
       for (Collisionable cItem : collisionableItems){
          if ( cItem.isEnabled() ){
              double x2 = c.getX() - cItem.getX();
              double y2 = c.getY() - cItem.getY();   
                         
              if(x2 > 0 && y2 > 0){ // circular math colision        
                  if( (x2*x2) + (y2*y2) < (radious*radious) ) {
                      c.enabled(false); // destroy item
                      cItem.enabled(false);
                  }
              }       
          }
       }
   };       
}