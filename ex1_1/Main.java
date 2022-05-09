import java.util.*;
import java.io.*;
import java.lang.Thread;

public class Main {

    public static void main(String[] args) {
        // int i=0;
        // ConcurrentLinkedQueue<Order> orders = new ConcurrentLinkedQueue<>();

        // System.out.println("Hello World!");

        // for (i=0; i<3; i++){
        //     new OrderTaker(orders).start();
        // }

        // new OrderHandler(orders).start();

        OrderQueue orderqueue = new OrderQueue();
        
        int i = 0;
        for (i=1; i < 10; i++){
            new Thread(new OrderTaker(orderqueue, 10*i)).start();
            
        }

        new Thread(new OrderHandler(orderqueue)).start();
    }
}