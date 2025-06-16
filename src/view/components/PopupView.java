package view.components;

import javax.swing.*;
import java.awt.*;

public class PopupView {
    public void showPopup(JPanel parentPanel, String mensagem, String tipo) {
        Window parentWindow = SwingUtilities.getWindowAncestor(parentPanel);

        JDialog dialog = new JDialog(parentWindow, tipo, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(370, 150);
        dialog.setLayout(new BorderLayout());
        dialog.setResizable(false); //Pra remover o icone
        dialog.setIconImage(null);

        Point location = parentPanel.getLocationOnScreen();
        Dimension size = parentPanel.getSize();

        int dialogX = location.x + (size.width - dialog.getWidth()) / 2;
        int dialogY = location.y + (size.height - dialog.getHeight()) / 2;
        dialog.setLocation(dialogX, dialogY);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); //Já que são 2 itens
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel messageLabel = new JLabel(mensagem);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton confirmButton = new JButton("OK");
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.addActionListener(e -> dialog.dispose());

        panel.add(messageLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(confirmButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}
