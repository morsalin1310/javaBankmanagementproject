package frames;
import entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.*;



public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Login");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);  

        
        JLabel backgroundLabel = new JLabel(new ImageIcon("resources/background.jpg"));  
        backgroundLabel.setBounds(0, -10, 1000, 700);  
        add(backgroundLabel);
		
		
		
		
		

       
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);  
        loginPanel.setOpaque(false);  
        loginPanel.setBackground(new Color(255, 255, 255, 200)); 
        loginPanel.setBounds(550, 150, 370, 350);  

        
        JLabel titleLabel = new JLabel("Bank Management System");
        titleLabel.setBounds(30, 30, 350, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(60, 100, 100, 30);  
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 15));

        usernameField = new JTextField(15);
        usernameField.setBounds(140, 100, 160, 30);
        usernameField.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(60, 150, 100, 30);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 15));

        passwordField = new JPasswordField(15);
        passwordField.setBounds(140, 150, 160, 30);
        passwordField.setFont(new Font("Arial", Font.BOLD, 13));

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(140, 200, 100, 30);
        loginButton.setFont(new Font("Arial", Font.BOLD, 15));
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        
        JLabel extraLabel = new JLabel("Are you new here? Then click");
        extraLabel.setBounds(90, 260, 350, 30);
        extraLabel.setFont(new Font("Arial", Font.BOLD, 15));

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(140, 290, 100, 30);
        registerButton.setFont(new Font("Arial", Font.BOLD, 15));
        registerButton.setOpaque(true);
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);


        
        loginPanel.add(titleLabel);
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(extraLabel);
        loginPanel.add(registerButton);

        backgroundLabel.add(loginPanel);  

       
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                loginButton.setBackground(new Color(158, 144, 130)); 
            }
        
            @Override
            public void mouseExited(MouseEvent evt) {
                loginButton.setBackground(null); 
            }
        });

        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                registerButton.setBackground(new Color(158, 144, 130)); 
            }
        
            @Override
            public void mouseExited(MouseEvent evt) {
                registerButton.setBackground(null); 
            }
        });
        

       
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

              
                User loggedInUser = authenticateUser(username, password);

                if (loggedInUser != null) {
                  
                    if (loggedInUser instanceof Customer) {
                        new CustomerDashboardFrame((Customer) loggedInUser);
                    } else if (loggedInUser instanceof Banker) {
                        new BankerDashboardFrame();
                    }
                    dispose();  
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                }
            }
        });

       
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame();  
                dispose();  
            }
        });

        setVisible(true);
    }

   
    private User authenticateUser(String username, String password) {
        
        User customer = authenticateFromTXT("resources/Customer.txt", username, password, "Customer");
        if (customer != null) return customer;

        
        User banker = authenticateFromTXT("resources/Banker.txt", username, password, "Banker");
        if (banker != null) return banker;

        return null;  
    }

 
    private User authenticateFromTXT(String fileName, String username, String password, String userType) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length > 3 && fields[0].equals(username) && fields[1].equals(password)) {
                    
                    if (userType.equals("Customer")) {
                        return new Customer(fields[0], fields[1], fields[2], fields[3], fields[4], Double.parseDouble(fields[5]));
                    } else if (userType.equals("Banker")) {
                        return new Banker(fields[0], fields[1], fields[2], fields[3]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
