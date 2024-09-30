package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class WithdrawPanel extends JPanel {
    private JTextField accountNumberField;
    private JTextField amountField;
    private JButton withdrawButton;

    public WithdrawPanel() {

        setLayout(null);

        JLabel accountNumberLabel = new JLabel("Account Number:");
        accountNumberLabel.setBounds(300, 130, 200, 30);
        accountNumberLabel.setFont(new Font("Arial", Font.BOLD, 18));

        accountNumberField = new JTextField();
        accountNumberField.setBounds(480, 130, 200, 30);
        accountNumberField.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel amountLabel = new JLabel("Amount to Withdraw:");
        amountLabel.setBounds(300, 170, 200, 30);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 18));

        amountField = new JTextField();
        amountField.setBounds(480, 170, 200, 30);
        amountField.setFont(new Font("Arial", Font.BOLD, 18));

        withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(420, 250, 110, 40);
        withdrawButton.setFont(new Font("Arial", Font.BOLD, 18));

        add(accountNumberLabel);
        add(accountNumberField);
        add(amountLabel);
        add(amountField);
        add(withdrawButton);

        
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String accountNumber = accountNumberField.getText().trim();
                String amountStr = amountField.getText().trim();
                if (!accountNumber.isEmpty() && !amountStr.isEmpty()) {
                    try {
                        double amount = Double.parseDouble(amountStr);
                        if (amount > 0) {
                            updateBalance(accountNumber, amount);
                        } else {
                            JOptionPane.showMessageDialog(WithdrawPanel.this,
                                    "Amount must be greater than zero.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(WithdrawPanel.this,
                                "Invalid amount. Please enter a valid number.");
                    }
                } else {
                    JOptionPane.showMessageDialog(WithdrawPanel.this,
                            "Please enter both account number and amount.");
                }

                

                accountNumberField.setText("");
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
