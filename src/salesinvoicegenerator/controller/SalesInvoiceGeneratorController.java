package salesinvoicegenerator.controller;

import salesinvoicegenerator.view.SalesInvoiceGeneratorFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.IOException;
import javax.swing.filechooser.FileFilter;
import salesinvoicegenerator.model.Invoice;
import salesinvoicegenerator.model.InvoiceLine;
import salesinvoicegenerator.model.InvoiceDataTable;
import salesinvoicegenerator.view.AddInvoiceDialog;
import salesinvoicegenerator.view.AddInvoiceLineDialog;
import salesinvoicegenerator.model.InvoiceLineDataTable;

/**
 *
 * @author Hager.Ahmed
 */
public class SalesInvoiceGeneratorController implements ActionListener, ListSelectionListener {

    private SalesInvoiceGeneratorFrame salesInvoiceGeneratorFrame;
    private AddInvoiceDialog invoiceDialog;
    private AddInvoiceLineDialog invoiceLineDialog;

    public SalesInvoiceGeneratorController(SalesInvoiceGeneratorFrame salesInvoiceGeneratorFrame) {
        this.salesInvoiceGeneratorFrame = salesInvoiceGeneratorFrame;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        //System.out.println("Welcome");
        switch (ae.getActionCommand()) {
            case "Load File":
                loadFile();

                break;

            case "Save File":
                saveFile();
                break;

            case "Create New Inoice":
                createNewInoice();
                break;

            case "addInvoiceRecord":
                addInvoiceRecord();
                break;
            case "cancelAddedInvoice":
                cancelAddedInvoice();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;

            case "Save":
                save();
                break;

            case "Cancel":
                cancel();
                break;

            case "addNewLineRecord":
                addNewLineRecord();

            case "cancelAddedLine":
                cancelAddedLine();
                break;

        }

    }

    private void loadFile() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileFilter() {
            public String getDescription() {
                return "CSV Documents (*.csv)";
            }

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    return f.getName().toLowerCase().endsWith(".csv");
                }
            }
        });
        try {
            int result = fileChooser.showOpenDialog(salesInvoiceGeneratorFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File invoiceHeadereaderFile = fileChooser.getSelectedFile();
                Path invoiceHeaderPath = Paths.get(invoiceHeadereaderFile.getAbsolutePath());
                List<String> invoiceHeaderLines = Files.readAllLines(invoiceHeaderPath);
                ArrayList<Invoice> arrayOfInvoices = new ArrayList<>();
                for (String invoiceHeaderLine : invoiceHeaderLines) {
                    try {
                        String[] header = invoiceHeaderLine.split(",");
                        int invoiceNumber = Integer.parseInt(header[0]);
                        String invoiceDate = header[1];
                        String customerName = header[2];

                        Invoice invoice = new Invoice(invoiceNumber, invoiceDate, customerName);
                        arrayOfInvoices.add(invoice);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(salesInvoiceGeneratorFrame, "Please upload the correct format", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                result = fileChooser.showOpenDialog(salesInvoiceGeneratorFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File invoiceLineFile = fileChooser.getSelectedFile();
                    Path invoiceLinePath = Paths.get(invoiceLineFile.getAbsolutePath());
                    List<String> itemLines = Files.readAllLines(invoiceLinePath);

                    for (String itemLine : itemLines) {
                        try {
                            String headerLine[] = itemLine.split(",");
                            int invoiceNumber = Integer.parseInt(headerLine[0]);
                            String itemName = headerLine[1];
                            double itemPrice = Double.parseDouble(headerLine[2]);
                            int count = Integer.parseInt(headerLine[3]);
                            Invoice invoiceInLine = null;
                            for (Invoice invoice : arrayOfInvoices) {
                                if (invoice.getInvoiceNumber() == invoiceNumber) {
                                    invoiceInLine = invoice;
                                    break;
                                }
                            }

                            InvoiceLine invoiceLine = new InvoiceLine(invoiceInLine, itemName, itemPrice, count);
                            invoiceInLine.getLines().add(invoiceLine);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                           JOptionPane.showMessageDialog(salesInvoiceGeneratorFrame, "Please upload the correct format", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                }
                salesInvoiceGeneratorFrame.setInvoices(arrayOfInvoices);
                InvoiceDataTable invoiceDateTabel = new InvoiceDataTable(arrayOfInvoices);
                salesInvoiceGeneratorFrame.setInvoiceDataTable(invoiceDateTabel);
                salesInvoiceGeneratorFrame.getInvoicesTable().setModel(invoiceDateTabel);
                salesInvoiceGeneratorFrame.getInvoiceDataTable().fireTableDataChanged();

            }
        } catch (IOException ex) {
            ex.printStackTrace();
             JOptionPane.showMessageDialog(salesInvoiceGeneratorFrame, "Please upload the correct format", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createNewInoice() {
        invoiceDialog = new AddInvoiceDialog(salesInvoiceGeneratorFrame);
        invoiceDialog.setVisible(true);
    }

    private void addInvoiceRecord() {
        String invoiceDate = invoiceDialog.getInvDateField().getText();
        String customerName = invoiceDialog.getCustNameField().getText();
        int invoiceNumber = salesInvoiceGeneratorFrame.nextInvoiceIndex();
        try {
            String[] partsOfDate = invoiceDate.split("-");
            if (partsOfDate.length < 3) {
                JOptionPane.showMessageDialog(salesInvoiceGeneratorFrame, "Date is in wrong format , please enter the date as the following: DD-MM-YY", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int day = Integer.parseInt(partsOfDate[0]);
                int month = Integer.parseInt(partsOfDate[1]);
                int year = Integer.parseInt(partsOfDate[2]);
                if (day > 31 || month > 12) {
                    JOptionPane.showMessageDialog(salesInvoiceGeneratorFrame, "Date is in wrong format , please enter the date as the following: DD-MM-YY", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    Invoice invoice = new Invoice(invoiceNumber, invoiceDate, customerName);
                    salesInvoiceGeneratorFrame.getInvoices().add(invoice);
                    salesInvoiceGeneratorFrame.getInvoiceDataTable().fireTableDataChanged();
                    invoiceDialog.setVisible(false);
                    invoiceDialog.dispose();
                    invoiceDialog = null;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(salesInvoiceGeneratorFrame, "Date is in wrong format , please enter the date as the following: DD-MM-YY", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelAddedInvoice() {
        invoiceDialog.setVisible(false);
        invoiceDialog.dispose();
        invoiceDialog = null;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = salesInvoiceGeneratorFrame.getInvoicesTable().getSelectedRow();
        if (selectedIndex != -1) {
            Invoice currentInvoice = salesInvoiceGeneratorFrame.getInvoices().get(selectedIndex);
            salesInvoiceGeneratorFrame.getInvoiceNumberLabel().setText("" + currentInvoice.getInvoiceNumber());
            salesInvoiceGeneratorFrame.getInvoiceDateLabel().setText(currentInvoice.getInvoiceDate());
            salesInvoiceGeneratorFrame.getCustomerNameLabel().setText(currentInvoice.getCustomerName());
            salesInvoiceGeneratorFrame.getInvoiceTotalLabel().setText("" + currentInvoice.getTotalOfInvoice());
            InvoiceLineDataTable invoiceLineDataTable = new InvoiceLineDataTable(currentInvoice.getLines());
            salesInvoiceGeneratorFrame.getIvoiceLinesTable().setModel(invoiceLineDataTable);
            invoiceLineDataTable.fireTableDataChanged();

        }
    }

    private void saveFile() {
        ArrayList<Invoice> invoices = salesInvoiceGeneratorFrame.getInvoices();
        String invoiceHeaders = "";
        String lines = "";
        for (Invoice invoice : invoices) {
            String invCSV = invoice.getCSVFormat();
            invoiceHeaders += invCSV;
            invoiceHeaders += "\n";

            for (InvoiceLine line : invoice.getLines()) {
                String lineCSV = line.getCSVFormat();
                lines += lineCSV;
                lines += "\n";
            }
        }
        System.out.println("Check point");
        try {
            JFileChooser fc = new JFileChooser();
            int result = fc.showSaveDialog(salesInvoiceGeneratorFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                FileWriter hfw = new FileWriter(headerFile);
                hfw.write(invoiceHeaders);
                hfw.flush();
                hfw.close();
                result = fc.showSaveDialog(salesInvoiceGeneratorFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fc.getSelectedFile();
                    FileWriter lfw = new FileWriter(lineFile);
                    lfw.write(lines);
                    lfw.flush();
                    lfw.close();
                }
            }
        } catch (Exception ex) {

        }
    }

    private void deleteInvoice() {

        int rowTobeDeleted = salesInvoiceGeneratorFrame.getInvoicesTable().getSelectedRow();
        if (rowTobeDeleted != -1) {
            salesInvoiceGeneratorFrame.getInvoices().remove(rowTobeDeleted);
            salesInvoiceGeneratorFrame.getInvoiceDataTable().fireTableDataChanged();
        }

    }

    //create new item
    private void save() {
        invoiceLineDialog = new AddInvoiceLineDialog(salesInvoiceGeneratorFrame);
        invoiceLineDialog.setVisible(true);
    }

    private void cancel() {
        int itemToBeDelaeted = salesInvoiceGeneratorFrame.getIvoiceLinesTable().getSelectedRow();

        if (itemToBeDelaeted != -1) {
            InvoiceLineDataTable linesTableModel = (InvoiceLineDataTable) salesInvoiceGeneratorFrame.getIvoiceLinesTable().getModel();
            linesTableModel.getLines().remove(itemToBeDelaeted);
            linesTableModel.fireTableDataChanged();
            salesInvoiceGeneratorFrame.getInvoiceDataTable().fireTableDataChanged();
        }
    }

   

    private void addNewLineRecord() {
        String item = invoiceLineDialog.getItemNameField().getText();
        String countString = invoiceLineDialog.getItemCountField().getText();
        String priceString = invoiceLineDialog.getItemPriceField().getText();
        int count = Integer.parseInt(countString);
        double price = Double.parseDouble(priceString);
        int selectedInvoice = salesInvoiceGeneratorFrame.getInvoicesTable().getSelectedRow();
        if (selectedInvoice != -1) {
            Invoice invoice = salesInvoiceGeneratorFrame.getInvoices().get(selectedInvoice);
            InvoiceLine line = new InvoiceLine(invoice, item, price, count);
            invoice.getLines().add(line);
            InvoiceLineDataTable invoiceLineDataTable = (InvoiceLineDataTable) salesInvoiceGeneratorFrame.getIvoiceLinesTable().getModel();

            invoiceLineDataTable.fireTableDataChanged();
            salesInvoiceGeneratorFrame.getInvoiceDataTable().fireTableDataChanged();
        }
        invoiceLineDialog.setVisible(false);
        invoiceLineDialog.dispose();
        invoiceLineDialog = null;
    }
     private void cancelAddedLine() {
        invoiceLineDialog.setVisible(false);
        invoiceLineDialog.dispose();
        invoiceLineDialog = null;

    }

}
