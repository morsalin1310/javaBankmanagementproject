package frames;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class RegisterFrame extends JFrame {
    private JTextField usernameField, nameField, emailField;
    private JPasswordField passwordField;

    public RegisterFrame() {
        setTitle("Register");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel setup
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(230, 230, 250));
        mainPanel.setLayout(null);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBounds(900, 10, 90, 30);

        mainPanel.add(backButton);

        JLabel titleLabel = new JLabel("Create Your Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBounds(320, 50, 400, 50);
        titleLabel.setForeground(new Color(72, 61, 139));
        mainPanel.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 22));
        usernameLabel.setBounds(250, 150, 150, 30);
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.BOLD, 20));
        usernameField.setBounds(450, 150, 300, 35);
        mainPanel.add(usernameLabel);
        mainPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 22));
        passwordLabel.setBounds(250, 210, 150, 30);
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.BOLD, 20));
        passwordField.setBounds(450, 210, 300, 35);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 22));
        nameLabel.setBounds(250, 270, 150, 30);
        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.BOLD, 20));
        nameField.setBounds(450, 270, 300, 35);
        mainPanel.add(nameLabel);
        mainPanel.add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 22));
        emailLabel.setBounds(250, 330, 150, 30);
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.BOLD, 20));
        emailField.setBounds(450, 330, 300, 35);
        mainPanel.add(emailLabel);
        mainPanel.add(emailField);

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 24));
        registerButton.setBounds(450, 400, 200, 50);

        mainPanel.add(registerButton);

        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame();
            }
        });

        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String name = nameField.getText();
                String email = emailField.getText();

               
                String accountNumber = generateAccountNumber();

                try (FileWriter writer = new FileWriter("resources/Customer.txt", true)) {
                    writer.append(
                            username + "," + password + "," + name + "," + email + "," + accountNumber + ",0.0\n");
                    JOptionPane.showMessageDialog(null,
                            "Registration successful. Your account number is: " + accountNumber);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                
                //to reset testfield
                usernameField.setText("");
                passwordField.setText("");
                nameField.setText("");
                emailField.setText("");
            }
        });

        add(mainPanel);
        setVisible(true);
    }

    // Method to generate a random 10-digit account number
    private String generateAccountNumber() {
        String accountNumber = String.valueOf(System.currentTimeMillis());
        return accountNumber.substring(accountNumber.length() - 10);
    }

}
