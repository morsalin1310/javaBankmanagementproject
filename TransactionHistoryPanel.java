package panels;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionHistoryPanel extends JPanel {
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JTextField startDateField;
    private JTextField endDateField;

    public TransactionHistoryPanel(String accountNumber) {
        setLayout(new BorderLayout());
        
       
        JPanel filterPanel = new JPanel(new FlowLayout());
        JLabel startLabel = new JLabel("Start Date (YYYY-MM-DD): ");
        startDateField = new JTextField(10);
        JLabel endLabel = new JLabel("End Date (YYYY-MM-DD): ");
        endDateField = new JTextField(10);
        JButton filterButton = new JButton("Filter by Date Range");
        JButton showAllButton = new JButton("Show All");  
        JButton deleteButton = new JButton("Delete");
        
        filterPanel.add(startLabel);
        filterPanel.add(startDateField);
        filterPanel.add(endLabel);
        filterPanel.add(endDateField);
        filterPanel.add(filterButton);
        filterPanel.add(showAllButton);  
        filterPanel.add(deleteButton);

        
        String[] columnNames = {"Account Number", "Transaction Type", "Amount", "Date", "Balance"};
        tableModel = new DefaultTableModel(columnNames, 0);
        transactionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(transactionTable);

        

        
        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        

        
        loadTransactionData(accountNumber);

       
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startDate = startDateField.getText().trim();
                String endDate = endDateField.getText().trim();
                if (isValidDateFormat(startDate) && isValidDateFormat(endDate)) {
                    filterByDateRange(accountNumber, startDate, endDate);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter valid dates in YYYY-MM-DD format.");
                }
            }
        });

       
        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                loadTransactionData(accountNumber);
            }
        });

       
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = transactionTable.getSelectedRow();
                if (selectedRow != -1) {
                    String accountNum = (String) transactionTable.getValueAt(selectedRow, 0);
                    String transactionType = (String) transactionTable.getValueAt(selectedRow, 1);
                    String transactionAmount = String.valueOf(transactionTable.getValueAt(selectedRow, 2));
                    String transactionDate = (String) transactionTable.getValueAt(selectedRow, 3);
                    String transactionBalance = String.valueOf(transactionTable.getValueAt(selectedRow, 4));

                    
                    deleteTransactionFromtxt(accountNum, transactionType, transactionAmount, transactionDate, transactionBalance);

                    
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(null, "Transaction deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a transaction to delete.");
                }
            }
        });
    }

    private void loadTransactionData(String accountNumber) {
        tableModel.setRowCount(0);  
        try (BufferedReader br = new BufferedReader(new FileReader("resources/Transactions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5 && data[0].equals(accountNumber)) {
                    tableModel.addRow(new Object[]{data[0], data[1], data[2], data[3], data[4]});
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Transactions file not found.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    private void filterByDateRange(String accountNumber, String startDate, String endDate) {
        tableModel.setRowCount(0);  

        try (BufferedReader br = new BufferedReader(new FileReader("resources/Transactions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5 && data[0].equals(accountNumber)) {
                    String transactionDate = data[3];
                    if (isDateInRange(transactionDate, startDate, endDate)) {
                        tableModel.addRow(new Object[]{data[0], data[1], data[2], data[3], data[4]});
                    }
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Transactions file not found.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isDateInRange(String transactionDate, String startDate, String endDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date transaction = dateFormat.parse(transactionDate.split(" ")[0]); // Splited to get date part only
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            return !transaction.before(start) && !transaction.after(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isValidDateFormat(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void deleteTransactionFromtxt(String accountNumber, String type, String amount, String date, String balance) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/Transactions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (!(data.length == 5 && data[0].equals(accountNumber) && data[1].equals(type) && data[2].equals(amount) && data[3].equals(date) && data[4].equals(balance))) {
                    sb.append(line).append("\n");  
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("resources/Transactions.txt"))) {
            bw.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
