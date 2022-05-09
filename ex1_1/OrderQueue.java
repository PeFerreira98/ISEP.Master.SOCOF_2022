import java.util.*;
import java.io.*;
import java.lang.Thread;

public class OrderQueue{
    public LinkedList<Order> orders = new LinkedList<>();

    public OrderQueue(){
        orders = new LinkedList<>();
    }

    public synchronized void addOrder(Order order){
        orders.add(order);
        
        for (Order var : orders) 
        {
            System.out.print(var.number+";");
        }
        System.out.println();
    }

    public synchronized void removeOrder(){
        orders.remove();
        
        for (Order var : orders) 
        {
            System.out.print(var.number+";");
        }
        System.out.println();
    }

    public synchronized boolean isEmpty(){
        return this.orders.size() == 0;
    }
}