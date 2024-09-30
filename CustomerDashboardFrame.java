package frames;
import entities.Customer;
import panels.DepositPanelCustomer;
import panels.TransactionHistoryPanel;
import panels.TransferPanel;
import panels.ViewBalancePanel;
import panels.WithdrawPanelCustomer;

import javax.swing.*;
import java.awt.*;

public class CustomerDashboardFrame extends JFrame {
    
    private Customer customer;
    private JPanel contentPanel;
    private JButton viewBalanceButton, depositButton, withdrawButton, transferButton, transactionHistoryButton, profileButton;

    public CustomerDashboardFrame(Customer customer) {
        this.customer = customer;

        
        setTitle("Customer Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null); 

        // Logo Panel 
        JPanel logoPanel = new JPanel();
        logoPanel.setBounds(0, 0, 1000, 60); 
        logoPanel.setLayout(null);
        logoPanel.setBackground(new Color(0, 51, 102)); 

        
        JLabel bankName = new JLabel("Students Bank");
        bankName.setFont(new Font("Arial", Font.BOLD, 24));
        bankName.setForeground(Color.WHITE);
        bankName.setBounds(20, 10, 300, 40); 
        logoPanel.add(bankName);

        
        profileButton = new JButton("Profile");
        profileButton.setBounds(720, 10, 130, 40);
        profileButton.setFont(new Font("Arial", Font.BOLD, 16));
        profileButton.setBackground(new Color(30, 144, 255)); 
        profileButton.setForeground(Color.WHITE);
        profileButton.setFocusPainted(false);
        profileButton.setBorderPainted(false);
        profileButton.setOpaque(true);
        logoPanel.add(profileButton);

        profileButton.addActionListener(e -> new ProfileUpdateFrame(customer)); 

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(860, 10, 100, 40); 
        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        logoutButton.setBackground(new Color(30, 144, 255)); 
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setOpaque(true);
        logoPanel.add(logoutButton);

        logoutButton.addActionListener(e -> {
            dispose(); 
            new LoginFrame(); 
        });

        // Navigation panel
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBounds(0, 60, 1000, 50); 
        navigationPanel.setLayout(new GridLayout(1, 5, 5, 0)); 
        navigationPanel.setBackground(new Color(100, 149, 237)); 

        
        viewBalanceButton = createNavigationButton("View Balance");
        depositButton = createNavigationButton("Deposit");
        withdrawButton = createNavigationButton("Withdraw");
        transferButton = createNavigationButton("Transfer");
        transactionHistoryButton = createNavigationButton("Transaction History");
        

        
        navigationPanel.add(viewBalanceButton);
        navigationPanel.add(depositButton);
        navigationPanel.add(withdrawButton);
        navigationPanel.add(transferButton);
        navigationPanel.add(transactionHistoryButton);
        

        
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBounds(0, 110, 1000, 590); 
        contentPanel.setBackground(new Color(255, 255, 255)); 

         
        ViewBalancePanel viewBalancePanel = new ViewBalancePanel(customer);

       
        contentPanel.add( viewBalancePanel, "View Balance");
        contentPanel.add(new DepositPanelCustomer(customer), "Deposit");
        contentPanel.add(new WithdrawPanelCustomer(customer), "Withdraw");
        contentPanel.add(new TransferPanel(customer), "Transfer");
        contentPanel.add(new TransactionHistoryPanel(customer.getAccountNumber()), "Transaction History");

      
        viewBalanceButton.addActionListener(e -> {
            viewBalancePanel.refreshBalance();
            switchPanel("View Balance");
        });
        depositButton.addActionListener(e -> switchPanel("Deposit"));
        withdrawButton.addActionListener(e -> switchPanel("Withdraw"));
        transferButton.addActionListener(e -> switchPanel("Transfer"));
        transactionHistoryButton.addActionListener(e -> switchPanel("Transaction History"));

        
        add(logoPanel);
        add(navigationPanel);
        add(contentPanel);

       
        switchPanel("View Balance");
        setVisible(true);
    }

    
    private JButton createNavigationButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(65, 105, 225)); 
        button.setForeground(Color.WHITE); 
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }

    
    private void switchPanel(String section) {
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        cardLayout.show(contentPanel, section);

        
        resetButtonColors();

        
        switch (section) {
            case "View Balance":
                viewBalanceButton.setBackground(new Color(30, 144, 255)); 
                break;
            case "Deposit":
                depositButton.setBackground(new Color(30, 144, 255)); 
                break;
            case "Withdraw":
                withdrawButton.setBackground(new Color(30, 144, 255)); 
                break;
            case "Transfer":
                transferButton.setBackground(new Color(30, 144, 255)); 
                break;
            case "Transaction History":
                transactionHistoryButton.setBackground(new Color(30, 144, 255)); 
                break;
        }
    }

    
    private void resetButtonColors() {
        viewBalanceButton.setBackground(new Color(65, 105, 225));
        depositButton.setBackground(new Color(65, 105, 225));
        withdrawButton.setBackground(new Color(65, 105, 225));
        transferButton.setBackground(new Color(65, 105, 225));
        transactionHistoryButton.setBackground(new Color(65, 105, 225));
    }
}
