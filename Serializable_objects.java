import java.io.Serializable;

public class Serializable_objects implements Serializable {
    public Object num;
    public Object amounts;
    
    public Serializable_objects(){}
    public Serializable_objects(int num){
     System.out.println("Serializable object class" + num);
    }
}