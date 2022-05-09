public class Order {
	private static int id = 0;
	public int number;
	public String Consumer = "";
	public String Product = "";
	public String PaymentInfo = ""; 
	private Object blockear;

	public Order() {
		this.number = id;
		this.Consumer = "" + id;
		this.Product = "" + id;
		this.PaymentInfo = "" + id;
		id ++;
	}	
	public Order(int id) {
		this.number = id;
		this.Consumer = "" + id;
		this.Product = "" + id;
		this.PaymentInfo = "" + id;
		id ++;
	}	
}