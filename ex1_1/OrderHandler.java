import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OrderHandler implements Runnable{
	private OrderQueue orderqueue;
	
	public OrderHandler(OrderQueue orderqueue) {
		this.orderqueue = orderqueue;
	}
	
	@Override
	public void run() {
		int stop =0;
		while(true) {
			if (!this.orderqueue.isEmpty()) {
				this.orderqueue.removeOrder();
			}
			else{
				try {
					stop++;
					if(stop > 5){
						break;
					}
					Thread.sleep(1000);
				}
				catch(Exception ex){
					System.out.println("d");
				}
			}
		}
	}
}