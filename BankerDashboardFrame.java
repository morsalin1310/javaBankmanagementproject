package frames;
import panels.AccountsPanel;
import panels.DepositPanel;
import panels.WithdrawPanel;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BankerDashboardFrame extends JFrame {
    
    private JPanel contentPanel;
    private JPanel logoPanel, navigationPanel;
    private JButton logoutButton;
    private JButton manageAccountsButton, depositButton, withdrawButton;

    public BankerDashboardFrame() {
        
        setTitle("Banker Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null); 

       
        logoPanel = new JPanel();
        logoPanel.setBounds(0, 0, 1000, 60); 
        logoPanel.setLayout(null);
        logoPanel.setBackground(new Color(0, 51, 102)); 

        
        JLabel bankName = new JLabel("Students Bank");
        bankName.setFont(new Font("Arial", Font.BOLD, 24));
        bankName.setForeground(Color.WHITE);
        bankName.setBounds(20, 10, 300, 40); 
        logoPanel.add(bankName);

        
        logoutButton = new JButton("Logout");
        logoutButton.setBounds(860, 10, 100, 40); 
        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        logoutButton.setBackground(new Color(30, 144, 255)); 
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setOpaque(true);
        logoPanel.add(logoutButton);

        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  
                new LoginFrame();  
            }
        });


        navigationPanel = new JPanel();
        navigationPanel.setBounds(0, 60, 1000, 50); 
        navigationPanel.setLayout(new GridLayout(1, 3, 10, 0)); 
        navigationPanel.setBackground(new Color(100, 149, 237)); 

        
        manageAccountsButton = createNavigationButton("Manage Accounts");
        depositButton = createNavigationButton("Deposit");
        withdrawButton = createNavigationButton("Withdraw");

        
        navigationPanel.add(manageAccountsButton);
        navigationPanel.add(depositButton);
        navigationPanel.add(withdrawButton);

      
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBounds(0, 110, 1000, 590); 
        contentPanel.setBackground(new Color(255, 255, 255)); 

    
        contentPanel.add(new AccountsPanel(), "Manage Accounts"); 
        contentPanel.add(new DepositPanel(),"Deposit");
        contentPanel.add(new WithdrawPanel(),"Withdraw");
        
       

        
        manageAccountsButton.addActionListener(e -> switchPanel("Manage Accounts"));
        depositButton.addActionListener(e -> switchPanel("Deposit"));
        withdrawButton.addActionListener(e -> switchPanel("Withdraw"));

        
        add(logoPanel);
        add(navigationPanel);
        add(contentPanel);

      
        switchPanel("Manage Accounts");
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
            case "Manage Accounts":
                manageAccountsButton.setBackground(new Color(30, 144, 255)); 
                break;
            case "Deposit":
                depositButton.setBackground(new Color(30, 144, 255)); 
                break;
            case "Withdraw":
                withdrawButton.setBackground(new Color(30, 144, 255)); 
                break;
        }
    }

    
    private void resetButtonColors() {
        manageAccountsButton.setBackground(new Color(65, 105, 225));
        depositButton.setBackground(new Color(65, 105, 225));
        withdrawButton.setBackground(new Color(65, 105, 225));
    }

}
