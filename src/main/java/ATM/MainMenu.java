package ATM;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class MainMenu {
    private JButton depositButton;
    private JButton balanceInquiryButton;
    private JButton withdrawalButton;
    private JButton logoutButton;
    private JTextArea transactionArea; // <-- Eklendi
    public JPanel panelMain;

    private ArrayList<String> transactions = new ArrayList<>();

    public MainMenu(JFrame frame) {
        panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout(10, 10)); // <-- GridLayout yerine BorderLayout

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10)); // Butonları düzenlemek için
        balanceInquiryButton = new JButton("Balance Inquiry");
        depositButton = new JButton("Deposit");
        withdrawalButton = new JButton("Withdrawal");
        logoutButton = new JButton("Logout");

        buttonPanel.add(balanceInquiryButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawalButton);
        buttonPanel.add(logoutButton);

        panelMain.add(buttonPanel, BorderLayout.NORTH);

        transactionArea = new JTextArea();
        transactionArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(transactionArea);
        panelMain.add(scrollPane, BorderLayout.CENTER);

        balanceInquiryButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Your balance: $" + ATMApp.currentUser.balance);
        });

        depositButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter amount to deposit:");
            try {
                double amount = Double.parseDouble(input);
                if (amount <= 0) throw new Exception();

                ATMApp.currentUser.balance += amount;

                try (Connection conn = DBHelper.connect()) {
                    String sql = "UPDATE Users SET Balance = ? WHERE PIN = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setDouble(1, ATMApp.currentUser.balance);
                    ps.setInt(2, ATMApp.currentUser.pin);
                    ps.executeUpdate();
                }

                String record = "Deposited: $" + amount;
                transactions.add(record);
                transactionArea.append(record + "\n");

                JOptionPane.showMessageDialog(frame, "Deposit successful. New balance: $" + ATMApp.currentUser.balance);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid deposit amount.");
            }
        });

        withdrawalButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter amount to withdraw:");
            try {
                double amount = Double.parseDouble(input);

                if (amount > ATMApp.currentUser.balance || amount <= 0) {
                    JOptionPane.showMessageDialog(frame, "Insufficient or invalid amount.");
                } else {
                    ATMApp.currentUser.balance -= amount;

                    try (Connection conn = DBHelper.connect()) {
                        String sql = "UPDATE Users SET Balance = ? WHERE PIN = ?";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setDouble(1, ATMApp.currentUser.balance);
                        ps.setInt(2, ATMApp.currentUser.pin);
                        ps.executeUpdate();
                    }

                    String record = "Withdrew: $" + amount;
                    transactions.add(record);
                    transactionArea.append(record + "\n");

                    JOptionPane.showMessageDialog(frame, "Withdrawal successful. New balance: $" + ATMApp.currentUser.balance);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input.");
            }
        });

        logoutButton.addActionListener(e -> {
            if (!transactions.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Transaction summary:\n" + String.join("\n", transactions));
            }
            transactions.clear();
            transactionArea.setText(""); // TextArea temizle
            ATMApp.currentUser = null;
            frame.setContentPane(new Login(frame).panelMain);
            frame.revalidate();
            frame.repaint();
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);
        });
    }
}
