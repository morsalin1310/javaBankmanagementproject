package panels;
import entities.Customer;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ViewBalancePanel extends JPanel {
    private Customer customer;
    JLabel balanceLabel;

    public ViewBalancePanel(Customer customer) {
        this.customer = customer;

        setLayout(null); 
        

        
        JLabel welcomeLabel = new JLabel("Welcome to Your Account, " + customer.getName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        welcomeLabel.setForeground(new Color(0, 102, 204)); 
        welcomeLabel.setBounds(250, 100, 550, 35);

        
        JLabel usernameLabel = new JLabel("Username: " + customer.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameLabel.setForeground(new Color(0, 51, 102)); 
        usernameLabel.setBounds(260, 150, 200, 30);

        
        JLabel accountNumberLabel = new JLabel("Account Number: " + customer.getAccountNumber());
        accountNumberLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        accountNumberLabel.setForeground(new Color(0, 51, 102));
        accountNumberLabel.setBounds(460, 150, 300, 30);

        
        balanceLabel = new JLabel("Current Balance: " + customer.getBalance());
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 26));
        balanceLabel.setForeground(new Color(34, 139, 34)); 
        balanceLabel.setBounds(390, 200, 350, 30);

        
        

        add(welcomeLabel);
        add(usernameLabel);
        add(accountNumberLabel);
        add(balanceLabel);
        
    
        
        refreshBalance();  
    }

    
    public void refreshBalance() {
        String txtFilePath = "resources/Customer.txt";
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(txtFilePath))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6 && data[4].equals(customer.getAccountNumber())) {  
                    double latestBalance = Double.parseDouble(data[5]);
                    balanceLabel.setText("Balance: " + latestBalance);  
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
