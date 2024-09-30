package panels;

import entities.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class TransferPanel extends JPanel {
    private Customer customer;
    private JTextField amountField;
    private JTextField recipientAccountField;

    public TransferPanel(Customer customer) {
        this.customer = customer;

        setLayout(null);

        JLabel recipientLabel = new JLabel("Recipient Account Number: ");
        recipientLabel.setBounds(290, 120, 250, 30);
        recipientLabel.setFont(new Font("Arial", Font.BOLD, 18));

        recipientAccountField = new JTextField(15);
        recipientAccountField.setBounds(530, 120, 200, 30);
        recipientAccountField.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel amountLabel = new JLabel("Enter Amount to Transfer: ");
        amountLabel.setBounds(290, 170, 250, 30);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 18));

        amountField = new JTextField(15);
        amountField.setBounds(530, 170, 200, 30);
        amountField.setFont(new Font("Arial", Font.BOLD, 18));

        JButton transferButton = new JButton("Transfer");
        transferButton.setBounds(470, 250, 110, 40);
        transferButton.setFont(new Font("Arial", Font.BOLD, 18));

        add(amountLabel);
        add(amountField);
        add(recipientLabel);
        add(recipientAccountField);
        add(transferButton);

       
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double amount;
                String recipientAccount = recipientAccountField.getText().trim();

                try {
                    amount = Double.parseDouble(amountField.getText());
                    if (amount <= 0 || recipientAccount.isEmpty()) {
                        JOptionPane.showMessageDialog(null,
                                "Please enter a positive amount and a valid recipient account.");
                    } else {
                        if (updateBalances(customer.getAccountNumber(), recipientAccount, amount)) {
                            JOptionPane.showMessageDialog(null, "Successfully transferred $" + amount
                                    + " to recipient account: " + recipientAccount);

                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Transfer failed. Please check your balance or recipient account.");
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid amount entered. Please try again.");
                }

           

                recipientAccountField.setText("");
                amountField.setText("");
            }
        });

        setVisible(true);
    }

    private boolean updateBalances(String senderAccount, String recipientAccount, double amount) {
        File inputFile = new File("resources/Customer.txt");
        StringBuilder updatedData = new StringBuilder();
        boolean senderFound = false, recipientFound = false;
        double senderNewBalance = 0.0, recipientNewBalance = 0.0;

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length >= 6) {
                  
                    if (data[4].equals(senderAccount)) {
                        double currentBalance = Double.parseDouble(data[5]);
                        if (currentBalance >= amount) {
                            senderNewBalance = currentBalance - amount;
                            data[5] = String.valueOf(senderNewBalance);
                            customer.setBalance(senderNewBalance);
                            senderFound = true;
                        } else {
                            return false;
                        }
                    }

                   
                    if (data[4].equals(recipientAccount)) {
                        double currentBalance = Double.parseDouble(data[5]);
                        recipientNewBalance = currentBalance + amount;
                        data[5] = String.valueOf(recipientNewBalance);
                        recipientFound = true;
                    }

                    updatedData.append(String.join(",", data)).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (senderFound) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(inputFile, false))) {
                bw.write(updatedData.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            recordTransaction(senderAccount, recipientAccount, amount, recipientFound, senderNewBalance,
                    recipientNewBalance);

            return true;
        }
        return false;
    }

    private void recordTransaction(String senderAccount, String recipientAccount, double amount, boolean recipientFound,
            double senderNewBalance, double recipientNewBalance) {

        String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        String record;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("resources/Transactions.txt", true))) {
            if (recipientFound) {
                record = senderAccount + "," + "Transfer" + "," + amount + "," + timestamp + "," + senderNewBalance
                        + "\n";
                String recordr = recipientAccount + "," + "Received" + "," + amount + "," + timestamp + ","
                        + recipientNewBalance + "\n";
                bw.write(recordr);

            } else {
                record = senderAccount + "," + "Transfer" + "," + amount + "," + timestamp + "," + senderNewBalance
                        + "\n";
            }

            bw.write(record);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
