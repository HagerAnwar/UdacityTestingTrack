
package salesinvoicegenerator.model;


import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class InvoiceDataTable extends AbstractTableModel {
    private ArrayList<Invoice> invoices;
    private String[] columnsName = {"No.", "Date", "Customer", "Total"};
    
    public InvoiceDataTable(ArrayList<Invoice> invoices) {
        this.invoices = invoices;
    }

   
    
    @Override
    public int getRowCount() {
        return invoices.size();
    }

    @Override
    public int getColumnCount() {
        return columnsName.length;
    }

    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Invoice invoice = invoices.get(rowIndex);
        
        switch (columnIndex) {
            case 0: return invoice.getInvoiceNumber();
            case 1: return invoice.getInvoiceDate();
            case 2: return invoice.getCustomerName();
            case 3: return invoice.getTotalOfInvoice();
            default : return "";
        }
    }
    
    
    @Override
    public String getColumnName(int columnName) {
        return columnsName[columnName];
    }
}