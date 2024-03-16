package view;

import controller.DataBaseConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View {
    // Atributos
    private JButton buttonSelect;
    private JPanel panelMain;
    private JButton buttonInsert;

    // Método main
    public static void main(String[] args) {
        setView();
    }

    // Método que setá os atributos do frame
    public static void setView() {
        JFrame frame = new JFrame("View"); // Titulo do frame
        frame.setContentPane(new View().panelMain); // Coloca tudo que está dentro do panelMain para dentro do frame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(600, 400);
    }

    // Função view, java swing insere para adicionar os eventos do projeto
    private View() {
        buttonSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataBaseConnection dataBase = new DataBaseConnection();
                Object[] resultSelectPass = dataBase.querySelectDataBase("SELECT * FROM PANEL_PASS", "PASS");
                Object[] resultSelectPassType = dataBase.querySelectDataBase("SELECT * FROM PANEL_PASS", "PASS_TYPE");

                if (resultSelectPass != null && resultSelectPassType != null) {

                    // Loop que define os valores dentro do array como object
                    for (Object resultPass : resultSelectPass) {
                        for (Object resultPassType : resultSelectPassType) {
                            System.out.println(String.valueOf(resultPassType + " " + resultPass));
                        }
                    }

                } else {
                    System.out.println("Nenhum resultado encontrado.");
                }
            }
        });
    }
}
