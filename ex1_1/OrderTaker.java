import java.util.*;
import java.io.*;
import java.lang.Thread;

public class OrderTaker implements Runnable{
	private OrderQueue orderqueue;
    private int threadId;
	
	public OrderTaker(OrderQueue orderqueue, int threadId) {
		this.orderqueue = orderqueue;
        this.threadId = threadId;
	}
	
	@Override
	public void run(){
        int i = 0;
        for (i=1; i<4; i++){
		    this.orderqueue.addOrder(new Order(threadId+i));

            try {
                Thread.sleep(10);
            }
            catch(Exception ex){
                System.out.println("d");
            }

        }
	}
}