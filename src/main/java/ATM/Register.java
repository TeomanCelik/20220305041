package ATM;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Register {
    private JTextField textField1;
    private JTextField textField2;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JButton alreadyHaveAnAccountButton;
    private JButton registerButton;
    private JButton clearButton;
    public JPanel panelMain;

    public Register(JFrame frame) {
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayout(8, 1, 10, 10));

        textField1 = new JTextField();
        textField2 = new JTextField();
        passwordField1 = new JPasswordField();
        passwordField2 = new JPasswordField();
        registerButton = new JButton("Register");
        clearButton = new JButton("Clear");
        alreadyHaveAnAccountButton = new JButton("Already Have an Account?");

        panelMain.add(new JLabel("Name:"));
        panelMain.add(textField1);
        panelMain.add(new JLabel("Surname:"));
        panelMain.add(textField2);
        panelMain.add(new JLabel("PIN:"));
        panelMain.add(passwordField1);
        panelMain.add(new JLabel("Confirm PIN:"));
        panelMain.add(passwordField2);
        panelMain.add(registerButton);
        panelMain.add(clearButton);
        panelMain.add(alreadyHaveAnAccountButton);

        registerButton.addActionListener(e -> {
            String name = textField1.getText().trim();
            String surname = textField2.getText().trim();
            String pin1 = new String(passwordField1.getPassword()).trim();
            String pin2 = new String(passwordField2.getPassword()).trim();

            if (name.isEmpty() || surname.isEmpty() || pin1.isEmpty() || pin2.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                return;
            }

            if (!pin1.equals(pin2)) {
                JOptionPane.showMessageDialog(frame, "PINs do not match.");
                return;
            }

            try {
                int pin = Integer.parseInt(pin1);

                try (Connection conn = DBHelper.connect()) {
                    if (conn == null) {
                        JOptionPane.showMessageDialog(frame, "Database connection failed.");
                        return;
                    }

                    String sql = "INSERT INTO Users (Name, Surname, PIN, Balance) VALUES (?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, name);
                    ps.setString(2, surname);
                    ps.setInt(3, pin);
                    ps.setDouble(4, 0.0);

                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "Account created successfully!");

                    frame.setContentPane(new Login(frame).panelMain);
                    frame.revalidate();
                    frame.repaint();
                    frame.setSize(400, 400);
                    frame.setLocationRelativeTo(null);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "PIN already exists or database error.");
                    ex.printStackTrace();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "PIN must be numeric.");
            }
        });

        clearButton.addActionListener(e -> {
            textField1.setText("");
            textField2.setText("");
            passwordField1.setText("");
            passwordField2.setText("");
        });

        alreadyHaveAnAccountButton.addActionListener(e -> {
            frame.setContentPane(new Login(frame).panelMain);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);
        });
    }
}
