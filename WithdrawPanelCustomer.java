package panels;

import entities.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WithdrawPanelCustomer extends JPanel {
    private Customer customer;
    private JTextField amountField;

    public WithdrawPanelCustomer(Customer customer) {
        this.customer = customer;

        setLayout(null);

        JLabel amountLabel = new JLabel("Enter Amount to Withdraw: ");
        amountLabel.setBounds(290, 170, 250, 30);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 18));

        amountField = new JTextField(15);
        amountField.setBounds(525, 170, 200, 30);
        amountField.setFont(new Font("Arial", Font.BOLD, 18));

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(470, 250, 110, 40);
        withdrawButton.setFont(new Font("Arial", Font.BOLD, 18));

        add(amountLabel);
        add(amountField);
        add(withdrawButton);

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String amountStr = amountField.getText().trim();
                if (!amountStr.isEmpty()) {
                    try {
                        double amount = Double.parseDouble(amountStr);
                        if (amount > 0) {
                            updateBalance(customer.getAccountNumber(), amount);
                        } else {
                            JOptionPane.showMessageDialog(WithdrawPanelCustomer.this,
                                    "Amount must be greater than zero.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(WithdrawPanelCustomer.this,
                                "Invalid amount. Please enter a valid number.");
                    }
                } else {
                    JOptionPane.showMessageDialog(WithdrawPanelCustomer.this,
                            "Please enter both account number and amount.");
                }

                
                amountField.setText("");
            }
        });

        setVisible(true);
    }

    private void updateBalance(String accountNumber, double amount) {
        String txtFilePath = "resources/Customer.txt";
        StringBuilder updatedContent = new StringBuilder();
        boolean accountFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(txtFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6 && data[4].equals(accountNumber)) {
                    accountFound = true;
                    double currentBalance = Double.parseDouble(data[5]);
                    currentBalance = currentBalance - amount;
                    if (currentBalance < 0) {
                        JOptionPane.showMessageDialog(this, "Insufficient balance for withdrawal.");
                        return;
                    }
                    data[5] = String.valueOf(currentBalance);
                    if (accountFound) {
                        customer.setBalance(currentBalance);
                    }
                    JOptionPane.showMessageDialog(this, "Transaction successful. Updated balance: " + currentBalance);

                    recordTransaction(accountNumber, "Withdraw", amount, currentBalance);
                }
                updatedContent.append(String.join(",", data)).append("\n");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!accountFound) {
            JOptionPane.showMessageDialog(this, "Account number not found.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFilePath))) {
            writer.write(updatedContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recordTransaction(String accountNumber, String transactionType, double amount, double updatedBalance) {
        String transactionFile = "resources/Transactions.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(transactionFile, true))) {
            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            String record = accountNumber + "," + transactionType + "," + amount + "," + timestamp + ","
                    + updatedBalance + "\n";
            writer.write(record);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
