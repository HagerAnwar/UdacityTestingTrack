package salesinvoicegenerator.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class InvoiceLineDataTable extends AbstractTableModel {

    private ArrayList<InvoiceLine> invoiceLines;
    private String[] columnsName = {"No.", "Item Name", "Item Price", "Count", "Item Total"};

    public InvoiceLineDataTable(ArrayList<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public ArrayList<InvoiceLine> getLines() {
        return invoiceLines;
    }

    @Override
    public int getRowCount() {
        return invoiceLines.size();
    }

    @Override
    public int getColumnCount() {
        return columnsName.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceLine invoiceLine = invoiceLines.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return invoiceLine.getInvoice().getInvoiceNumber();
            case 1:
                return invoiceLine.getItemName();
            case 2:
                return invoiceLine.getItemPrice();
            case 3:
                return invoiceLine.getCount();
            case 4:
                return invoiceLine.getTotalOfLines();
            default:
                return "";

        }

    }

    @Override
    public String getColumnName(int x) {
        return columnsName[x];
    }

}
