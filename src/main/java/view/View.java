package view;

import controller.DataBaseConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View {
    // Atributo
    private JButton buttonMsg;
    private JPanel panelMain;

    // Método main
    public static void main(String[] args) {
        JFrame frame = new JFrame("View"); // Titulo do frame
        frame.setContentPane(new View().panelMain); // Coloca tudo que está dentro do panelMain para dentro do frame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(400, 400);
    }

    // Método View
    public View() {
        buttonMsg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataBaseConnection dataBase = new DataBaseConnection();
                String result = String.valueOf(dataBase.dataBaseConnection());
                JOptionPane.showMessageDialog(null, result);
            }
        });
    }
}
