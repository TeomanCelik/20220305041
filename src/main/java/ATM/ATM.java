package ATM;

import javax.swing.*;

public class ATM {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("ATM System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ATMApp.loginScreen = new Login(frame); // Burada bir kez yaratıyoruz
            if (ATMApp.loginScreen.panelMain == null) {
                JOptionPane.showMessageDialog(null, "ATM.Login.panelMain is null! Check your .form setup.");
                return;
            }

            frame.setContentPane(ATMApp.loginScreen.panelMain);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
