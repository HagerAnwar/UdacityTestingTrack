
package salesinvoicegenerator.model;

import java.util.ArrayList;


public class Invoice {

    private int invoiceNumber;
    private String invoiceDate;
    private String customerName;
    private ArrayList<InvoiceLine> lines;
   

    public Invoice(int invoiceNumber, String invoiceDate, String customerName) {
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.customerName = customerName;
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public ArrayList<InvoiceLine> getLines() {
        
        if(lines==null)
        {
        lines=new ArrayList();
        }
        return lines;
    }

    public void setInvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getTotalPriceOfInvoice() {
        double totalOfInvoice = 0.0;
        for (InvoiceLine lines : getLines()) {
            totalOfInvoice += lines.getItemTotalPrice();
        }
        return totalOfInvoice;
    }

    @Override
    public String toString() {
        return "Invoice{" + "invoiceNumber=" + invoiceNumber + ", invoiceDate=" + invoiceDate + ", customerName=" + customerName + ", lines=" + lines + '}';
    }
    
   public double getTotalOfInvoice() {
        double totalInvoice = 0.0;
        for (InvoiceLine line : getLines()) {
            totalInvoice += line.getTotalOfLines();
        }
        return totalInvoice;
    }
      public String getCSVFormat() {
        return invoiceNumber + "," + invoiceDate + "," + customerName;
    }

}
