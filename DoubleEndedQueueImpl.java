import java.util.ArrayList;
import java.util.List;
 
public class DoubleEndedQueueImpl {
 
    private List<Integer> deque = new ArrayList<Integer>();
     
    public void insertFront(int item){
       
        System.out.println("adding at front: "+item);
        deque.add(0,item);
        System.out.println(deque);
    }
     
    public void insertRear(int item){
        
        System.out.println("adding at rear: "+item);
        deque.add(item);
        System.out.println(deque);
    }
     
    public void removeFront(){
        if(deque.isEmpty()){
            System.out.println("Deque underflow!! unable to remove.");
            return;
        }
       
        int rem = deque.remove(0);
        System.out.println("removed from front: "+rem);
        System.out.println(deque);
    }
     
    public void removeRear(){
        if(deque.isEmpty()){
            System.out.println("Deque underflow!! unable to remove.");
            return;
        }
        
        int rem = deque.remove(deque.size()-1);
        System.out.println("removed from front: "+rem);
        System.out.println(deque);
    }
     
    public int peakFront(){
        
        int item = deque.get(0);
        System.out.println("Element at first: "+item);
        return item;
    }
     
    public int peakRear(){
        
        int item = deque.get(deque.size()-1);
        System.out.println("Element at rear: "+item);
        return item;
    }
     
    public static void main(String a[]){
         
        DoubleEndedQueueImpl deq = new DoubleEndedQueueImpl();
        deq.insertFront(34);
        deq.insertRear(45);
        deq.removeFront();
        deq.removeFront();
        deq.removeFront();
        deq.insertFront(21);
        deq.insertFront(98);
        deq.insertRear(5);
        deq.insertFront(43);
        deq.removeRear();
    }
}