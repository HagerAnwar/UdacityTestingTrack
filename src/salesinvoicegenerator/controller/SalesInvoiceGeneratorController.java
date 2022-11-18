/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesinvoicegenerator.controller;


import salesinvoicegenerator.view.SalesInvoiceGeneratorFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.text.ParseException;
import java.io.IOException;
import javax.swing.filechooser.FileFilter;
import salesinvoicegenerator.model.Invoice;
import salesinvoicegenerator.model.InvoiceLine;
import salesinvoicegenerator.model.InvoiceDataTable;
import javax.swing.JTable;
import salesinvoicegenerator.view.InvoiceDialog;
import salesinvoicegenerator.view.LineDialog;
import salesinvoicegenerator.model.InvoiceLineDataTable;

/**
 *
 * @author Hager.Ahmed
 */
public class SalesInvoiceGeneratorController implements ActionListener, ListSelectionListener {

    private SalesInvoiceGeneratorFrame salesInvoiceGeneratorFrame;
    private InvoiceDialog invoiceDialog;
    private LineDialog invoiceLineDialog;

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
            case "createInvoiceOK":
                break;
            case "createInvoiceCancel":
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
                
            case "createLineOK":
                createLineOK();
                
            case "createLineCancel":
            createLineCancel();
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
                        JOptionPane.showMessageDialog(salesInvoiceGeneratorFrame, "Format Error, Please upload the correct format", "Error", JOptionPane.ERROR_MESSAGE);
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
                            JOptionPane.showMessageDialog(salesInvoiceGeneratorFrame, "Format Error, Please upload the correct format", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                }
                salesInvoiceGeneratorFrame.setInvoices(arrayOfInvoices);
                InvoiceDataTable invoiceDateTabel = new InvoiceDataTable(arrayOfInvoices);
                salesInvoiceGeneratorFrame.setInvoiceDataTable(invoiceDateTabel);
                salesInvoiceGeneratorFrame.getInvoicesTable().setModel(invoiceDateTabel);
                salesInvoiceGeneratorFrame.getInvoiceDataTable().fireTableDataChanged();

                //InvoicesTableModel invoicesTableModel = new InvoicesTableModel(invoicesArray);
                /*salesInvoiceGeneratorFrame.setInvoicesTableModel(invoicesTableModel);
                salesInvoiceGeneratorFrame.getInvoiceTable().setModel(invoicesTableModel);
                salesInvoiceGeneratorFrame.getInvoicesTableModel().fireTableDataChanged();*/
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            // JOptionPane.showMessageDialog(frame, "Cannot read file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void createNewInoice() {
      invoiceDialog = new InvoiceDialog(salesInvoiceGeneratorFrame);
        invoiceDialog.setVisible(true);
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
    }

    

    private void deleteInvoice() {

        int rowTobeDeleted = salesInvoiceGeneratorFrame.getInvoicesTable().getSelectedRow();
        if (rowTobeDeleted != -1) {
            salesInvoiceGeneratorFrame.getInvoices().remove(rowTobeDeleted);
            salesInvoiceGeneratorFrame.getInvoiceDataTable().fireTableDataChanged();
        }

    }

    private void save() {

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

    private void createLineCancel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void createLineOK() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}