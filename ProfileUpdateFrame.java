package frames;
import entities.Customer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ProfileUpdateFrame extends JFrame {
    private Customer customer;
    private JTextField nameField, emailField, usernameField;
    private JPasswordField passwordField;

    public ProfileUpdateFrame(Customer customer) {
        this.customer = customer;

        setTitle("Update Profile");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        
        JLabel usernameLabel = new JLabel("Username: ");
        usernameLabel.setBounds(50, 80, 120, 25);  
        usernameField = new JTextField(customer.getUsername(), 15);
        usernameField.setBounds(180, 80, 200, 25);  
        
        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setBounds(50, 120, 120, 25);  
        nameField = new JTextField(customer.getName(), 15);
        nameField.setBounds(180, 120, 200, 25);  
        
        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setBounds(50, 160, 120, 25); 
        emailField = new JTextField(customer.getEmail(), 15);
        emailField.setBounds(180, 160, 200, 25);  
        
        JLabel passwordLabel = new JLabel("New Password: ");
        passwordLabel.setBounds(50, 200, 120, 25);  
        passwordField = new JPasswordField(15);
        passwordField.setBounds(180, 200, 200, 25);  
        
        JButton updateButton = new JButton("Update Profile");
        updateButton.setBounds(180, 260, 200, 35);  
        
        
        add(usernameLabel);
        add(usernameField);
        add(nameLabel);
        add(nameField);
        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(updateButton);
        
        

        // Action listener for update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUsername = usernameField.getText();
                String newName = nameField.getText();
                String newEmail = emailField.getText();
                String newPassword = new String(passwordField.getPassword());  

                
                customer.setUsername(newUsername);
                customer.setName(newName);
                customer.setEmail(newEmail);

                
                if (!newPassword.isEmpty()) {
                    customer.setPassword(newPassword);
                }

                
                updateCustomerInTXT();

                JOptionPane.showMessageDialog(null, "Profile updated successfully!");
                dispose();  
            }
        });

        setVisible(true);
    }

   
    private void updateCustomerInTXT() {
        StringBuilder updatedContent = new StringBuilder();
        String accountNumber = customer.getAccountNumber();
        
        //to read
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/Customer.txt"))) {
            String line;

            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                
                if (data.length >= 6 && data[4].equals(accountNumber)) {
                    String updatedLine = customer.getUsername() + "," +
                                         customer.getPassword() + "," +
                                         customer.getName() + "," +
                                         customer.getEmail() + "," +
                                         customer.getAccountNumber() + "," +
                                         customer.getBalance();
                    updatedContent.append(updatedLine).append("\n");
                } else {
                    
                    updatedContent.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/Customer.txt"))) {
            writer.write(updatedContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
