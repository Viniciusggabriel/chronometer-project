package views;

import controller.*;
import controller.QueryExecutorSele;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.security.Key;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;

public class MainView extends JFrame {
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
    private JPanel timerPanel;
    private JLabel firstLap;
    private JLabel secondLap;
    private JLabel totalLap;
    private JPanel lapPanel;
    private DefaultTableModel tableModel;
    private Timer timer;
    private int hoursTimer = 0;
    private int minutesTimer = 0;
    private int secondsTimer = 0;
    private LocalTime resultTimer = LocalTime.of(00, 00, 00);
    private boolean activatedTimer = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView frame = new MainView();
            frame.pack(); // Faz um tamanho antes dos components serem carregados
            frame.setVisible(true);
        });
    }

    private void onSelectDb() throws SQLException {

        DatabaseConnectionManage connectionManager = new DatabaseConnectionManage();
        QueryExecutorSele dataBase = new QueryExecutorSele(connectionManager);

        Object[] resultSelectClockLastValue = dataBase.executeQuery("SELECT * FROM TIME_CLOCK ORDER BY ID_TIME DESC LIMIT 1;", "TIME_LAP");
        Object[] resultSelectClockFullValues = dataBase.executeQuery("SELECT * FROM TIME_CLOCK ORDER BY ID_TIME DESC LIMIT 2", "TIME_LAP");

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

        try {
            firstLap.setText("Primeira Volta: " + firstValue);
            secondLap.setText("Segunda Volta: " + penultimateValue);
            totalLap.setText("Total das Voltas: " + String.valueOf(totalTime));
        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir dados na tela: " + error, "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onInsertDb(Time data) throws SQLException {
        activatedTimer = false;
        hoursTimer = 0;
        labelHours.setText("Horas: " + hoursTimer);
        minutesTimer = 0;
        labelMinutes.setText("Minutos: " + minutesTimer);
        secondsTimer = 0;
        labelSeconds.setText("firstLap: " + secondsTimer);
        if (timer != null) {
            timer.stop();
            buttonRun.setBackground(Color.WHITE);
        }
        try {
            DatabaseConnectionManage connectionManager = new DatabaseConnectionManage();
            QueryExecutorImpl dataBase = new QueryExecutorImpl(connectionManager);
            dataBase.executeInsert("CALL INSERT_DATA_TIME('" + data + "');");

            // Mensagem de sucesso caso item seja inserido
            ImageIcon icon = new ImageIcon("src/img/check-check.png");
            JOptionPane.showMessageDialog(null, "Tempo inserido com sucesso", "Sucesso", JOptionPane.PLAIN_MESSAGE, icon);
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir tempo dentro do banco de dados: " + error, "Error", JOptionPane.WARNING_MESSAGE);
        }

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
                labelSeconds.setText("firstLap: " + secondsTimer);
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
        getRootPane().setDefaultButton(buttonSelect);

        /*
         * Eventos
         */
        // Eventos botõe
        buttonSelect.addActionListener(e -> {
            try {
                onSelectDb();
            } catch (SQLException error) {
                throw new RuntimeException("Erro ao requisitar valores: " + error); // Exeção de tempo de execução em java
            }
        });
        buttonInsertReset.addActionListener(e -> {
            try {
                onInsertDb(Time.valueOf(resultTimer));
            } catch (SQLException error) {
                throw new RuntimeException("Erro ao inserir valor: " + error);
            }
        });
        buttonRun.addActionListener((e) -> {
            if (!activatedTimer) onInitialize();
        });

        // Evento do botão de X da página
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Evento no esc para fechar a pagina
        contentPanel.registerKeyboardAction(e -> onExit(), KeyStroke.getKeyStroke(KeyEvent.VK_PRINTSCREEN, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Evento para fixar a página em 80% da view user
        contentPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                // Pega o tamanho da tela do usuário usando o toolkit do awt(Abstract Window Toolkit)
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Dimension screenSize = toolkit.getScreenSize();

                // (int) transforma o valor double ou float para int
                int frameMaxWidth = (int) (screenSize.getWidth() * 0.4);
                int frameMaxHeight = (int) (screenSize.getHeight() * 0.4);

                if (screenSize.width > frameMaxWidth || screenSize.height > frameMaxHeight) {
                    // Redefine o tamanho da janela para o tamanho máximo permitido
                    setSize(frameMaxWidth, frameMaxHeight);
                }
            }
        });
    }
}
