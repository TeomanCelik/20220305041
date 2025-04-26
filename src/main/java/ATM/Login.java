package ATM;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login {
    private JPasswordField passwordField1;
    private JButton donTUHaveButton;
    private JButton clearButton;
    private JButton loginButton;
    public JPanel panelMain;

    public Login(JFrame frame) {
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayout(5, 1, 10, 10));

        passwordField1 = new JPasswordField();
        loginButton = new JButton("Login");
        clearButton = new JButton("Clear");
        donTUHaveButton = new JButton("Don't Have an Account?");

        panelMain.add(new JLabel("Enter your PIN:"));
        panelMain.add(passwordField1);
        panelMain.add(loginButton);
        panelMain.add(clearButton);
        panelMain.add(donTUHaveButton);

        loginButton.addActionListener(e -> {
            String pinInput = new String(passwordField1.getPassword()).trim();
            if (pinInput.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter your PIN.");
                return;
            }
            try {
                int enteredPin = Integer.parseInt(pinInput);
                try (Connection conn = DBHelper.connect()) {
                    if (conn == null) {
                        JOptionPane.showMessageDialog(frame, "Database connection failed.");
                        return;
                    }

                    String sql = "SELECT * FROM Users WHERE PIN = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, enteredPin);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        ATMApp.currentUser = new User(
                                rs.getString("Name"),
                                rs.getString("Surname"),
                                rs.getInt("PIN"),
                                rs.getDouble("Balance")
                        );
                        JOptionPane.showMessageDialog(frame, "Welcome, " + ATMApp.currentUser.name + " " + ATMApp.currentUser.surname);
                        frame.setContentPane(new MainMenu(frame).panelMain);
                        frame.revalidate();
                        frame.repaint();
                        frame.setSize(400, 400);
                        frame.setLocationRelativeTo(null);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Incorrect PIN.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Database error.");
                    ex.printStackTrace();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "PIN must be numeric.");
            }
        });

        clearButton.addActionListener(e -> passwordField1.setText(""));

        donTUHaveButton.addActionListener(e -> {
            frame.setContentPane(new Register(frame).panelMain);
            frame.setSize(400, 400);
            frame.setLocationRelativeTo(null);
        });
    }
}
