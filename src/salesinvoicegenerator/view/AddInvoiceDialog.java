
package salesinvoicegenerator.view;


import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;



public class AddInvoiceDialog extends JDialog {
    private JTextField customerNameField;
    private JTextField invoiceDateField;
    private JLabel customerNameLabel;
    private JLabel invoiceDateLabel;
    private JButton okButton;
    private JButton cancelButton;

    public AddInvoiceDialog(SalesInvoiceGeneratorFrame salesInvoiceGeneratorFrame) {
        customerNameLabel = new JLabel("Customer Name:");
        customerNameField = new JTextField(20);
        invoiceDateLabel = new JLabel("Invoice Date:");
        invoiceDateField = new JTextField(20);
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        
        okButton.setActionCommand("addInvoiceRecord");
        cancelButton.setActionCommand("cancelAddedInvoice");
        
        okButton.addActionListener(salesInvoiceGeneratorFrame.getSalesInvoiceGeneratorController());
        cancelButton.addActionListener(salesInvoiceGeneratorFrame.getSalesInvoiceGeneratorController());
        setLayout(new GridLayout(3, 2));
        
        add(customerNameLabel);
        add(customerNameField);
        add(invoiceDateLabel);
        add(invoiceDateField);
        add(okButton);
        add(cancelButton);
        
        pack();
        
    }

    public JTextField getCustNameField() {
        return customerNameField;
    }

    public JTextField getInvDateField() {
        return invoiceDateField;
    }
    
}
    