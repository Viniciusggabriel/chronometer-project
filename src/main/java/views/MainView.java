package views;

import controller.DatabaseController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Time;
import java.time.LocalTime;

public class MainView extends JDialog {
    private JPanel contentPanel;
    private JButton buttonSelect;
    private JButton buttonInsertReset;
    private JTable tableTime;
    private JPanel buttonsPanel;
    private JPanel buttons;
    private JPanel tablePanel;
    private JLabel labelHours;
    private JLabel labelMinutes;
    private JLabel labelSeconds;
    private JButton buttonRun;
    private DefaultTableModel tableModel;
    private Timer timer;
    private int hoursTimer = 0;
    private int minutesTimer = 0;
    private int secondsTimer = 0;
    private  LocalTime resultTimer = LocalTime.of(00,00,00);

    private boolean activatedTimer = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView dialog = new MainView();
            dialog.pack(); // Faz um tamanho antes dos components serem carregados
            dialog.setVisible(true);
        });
    }

    private void onSelectDb() {
        // Lógica para um evento
        DatabaseController dataBase = new DatabaseController();

        Object[] resultSelectClockLastValue = dataBase.querySelectDataBase("SELECT * FROM TIME_CLOCK ORDER BY ID_TIME DESC LIMIT 1;", "TIME_LAP");
        Object[] resultSelectClockFullValues = dataBase.querySelectDataBase("SELECT * FROM TIME_CLOCK ORDER BY ID_TIME DESC LIMIT 2", "TIME_LAP");

        if (resultSelectClockLastValue.length == 0 && resultSelectClockFullValues.length == 0) {
            JOptionPane.showMessageDialog(null, "Não existe valores dentro do banco de dados", "Erro", JOptionPane.WARNING_MESSAGE);
        }

        // Passa os valores obtidos para a tipagem de tempo
        Time firstValue = (Time) resultSelectClockFullValues[0];
        Time penultimateValue = (Time) resultSelectClockFullValues[1];

        int hoursSelect = firstValue.getHours() + penultimateValue.getHours();
        int minutesSelect = firstValue.getMinutes() + penultimateValue.getMinutes();
        int secondsSelect = firstValue.getSeconds() + penultimateValue.getSeconds();

        if (secondsSelect >= 60) {
            minutesSelect += secondsSelect / 60;
            secondsSelect %= 60;
        }

        if (minutesSelect > 59) {
            hoursSelect += minutesSelect / 60;
            minutesSelect %= 60;
        }

        LocalTime totalTime = LocalTime.of(hoursSelect, minutesSelect, secondsSelect);

        Object[] rowData = {firstValue, penultimateValue, totalTime};
        tableModel.addRow(rowData);
    }

    // Além de enviar tem de parar o cronometro
    private void onInsertDb(Time data) {
        activatedTimer = false;
        hoursTimer = 0;
        labelHours.setText("Horas: " + hoursTimer);
        minutesTimer = 0;
        labelMinutes.setText("Minutos: " + minutesTimer);
        secondsTimer = 0;
        labelSeconds.setText("Segundos: " + secondsTimer);
        if (timer != null) {
            timer.stop();
        }

        DatabaseController dataBase = new DatabaseController();
        dataBase.queryInsertDataBase(data);
    }

    private void onInitialize() {
        // Muda o estado do botão de iniciar
        activatedTimer = true;
        buttonRun.setBackground(Color.GREEN);

        // Evento para mudar o cronometro
        timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsTimer++;
                labelSeconds.setText("Segundos: " + secondsTimer);
                if (secondsTimer >= 60) {
                    minutesTimer++;
                    secondsTimer = 0;
                    labelMinutes.setText("Minutos: " + minutesTimer);
                    if (minutesTimer >= 60) {
                        hoursTimer++;
                        minutesTimer = 0;
                        labelHours.setText("Horas: " + hoursTimer);
                    }
                }
               resultTimer = LocalTime.of(hoursTimer, minutesTimer, secondsTimer);
            }
        });
        timer.start();
    }

    private void onExit() {
        // Evento de saida
        dispose();
    }

    // Define tudo que tera na minha tela
    public MainView() {
        /*
        * Definições
        */
        setContentPane(contentPanel);
        setModal(true);
        getRootPane().setDefaultButton(buttonSelect);

        /*
        * Components
        */
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Primeira volta");
        tableModel.addColumn("Segunda volta");
        tableModel.addColumn("Soma das voltas");

        tableTime.setModel(tableModel);


        /*
        * Eventos
        */
        // Eventos botõe
        buttonSelect.addActionListener(e -> onSelectDb());
        buttonInsertReset.addActionListener(e -> onInsertDb(Time.valueOf(resultTimer)));
        buttonRun.addActionListener((e) -> {
            if (!activatedTimer) onInitialize();
        });

        // Evento do botão de X da página
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Evento no esc para fechar a pagina
        contentPanel.registerKeyboardAction(e -> onExit(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Evento para fixar a página em 80% da view user
        contentPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                // Pega o tamanho da tela do usuário usando o toolkit do awt(Abstract Window Toolkit)
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Dimension screenSize = toolkit.getScreenSize();

                // (int) transforma o valor double ou float para int
                int dialogMaxWidth = (int) (screenSize.getWidth() * 0.8);
                int dialogMaxHeight = (int) (screenSize.getHeight() * 0.8);

                if (screenSize.width > dialogMaxWidth || screenSize.height > dialogMaxHeight) {
                    // Redefine o tamanho da janela para o tamanho máximo permitido
                    setSize(dialogMaxWidth, dialogMaxHeight);
                }
            }
        });
    }
}
