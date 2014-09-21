import java.rmi.*;
import java.util.*;
 
public interface RmiServerIntf extends Remote {
    public HashMap<String,Integer> rocketRmiSync(int x, int y, int direction,boolean bulletPress, boolean  gravity ) throws RemoteException ;
}