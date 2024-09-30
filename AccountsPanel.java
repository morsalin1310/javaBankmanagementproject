package panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class AccountsPanel extends JPanel {
    private JTable accountsTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private final String txtFilePath = "resources/Customer.txt";

    public AccountsPanel() { 
        setLayout(new BorderLayout()); 

       
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        JLabel searchLabel = new JLabel("Search by Account Number:");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");  
        JButton deleteButton = new JButton("Delete Account");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);  
        searchPanel.add(deleteButton);

        
        String[] columnNames = {"Username", "Name", "Email", "Account Number", "Balance"};
        tableModel = new DefaultTableModel(columnNames, 0);
        accountsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(accountsTable);

        

        
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        

        
        loadCustomerData();

        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchCustomer();
            }
        });

        
        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);  
                loadCustomerData();  
            }
        });

        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCustomer();
            }
        });
    }

    private void loadCustomerData() {
        try (BufferedReader br = new BufferedReader(new FileReader(txtFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6) {
                    tableModel.addRow(new Object[] { data[0], data[2], data[3], data[4], data[5] });
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this,
                    "Customer accounts file not found. Please ensure 'resources/Customer.txt' is in the correct directory.",
                    "File Not Found", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void searchCustomer() {
        String searchAccountNumber = searchField.getText().trim();

        if (searchAccountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an account number to search.", "Input Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);

        boolean accountFound = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(txtFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6 && data[4].equals(searchAccountNumber)) {
                    tableModel.addRow(new Object[] { data[0], data[2], data[3], data[4], data[5] });
                    accountFound = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!accountFound) {
            JOptionPane.showMessageDialog(this, "No account found with Account Number: " + searchAccountNumber,
                    "No Match", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteCustomer() {
        int selectedRow = accountsTable.getSelectedRow();
        if (selectedRow != -1) {
            String accountNumber = (String) tableModel.getValueAt(selectedRow, 3);
            deleteCustomerFromTXT(accountNumber);
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Customer deleted successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.");
        }
    }

    private void deleteCustomerFromTXT(String accountNumber) {
        StringBuilder updatedContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(txtFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6 && !data[4].equals(accountNumber)) {
                    updatedContent.append(line).append(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFilePath))) {
            writer.write(updatedContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
