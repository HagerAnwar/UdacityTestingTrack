
package salesinvoicegenerator.model;

public class InvoiceLine {

    private Invoice invoice;
    private String itemName;
    private double itemPrice;
    private int count;

    public InvoiceLine(Invoice invoice, String itemName, double itemPrice, int count) {
        this.invoice = invoice;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getItemTotalPrice() {
        return count * itemPrice;
    }

    @Override
    public String toString() {
        return "InvoiceLine{" + "itemName=" + itemName + ", itemPrice=" + itemPrice + ", count=" + count + '}';
    }
    
       public double getTotalOfLines() {
        return itemPrice * count;
    }
    public String getCSVFormat() {
        return invoice.getInvoiceNumber()+ "," + itemName + "," + itemPrice + "," + count;
    }
    

}
