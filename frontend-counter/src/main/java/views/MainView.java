package views;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Map;

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
            frame.pack(); // Faz um tamanho de tela antes dos components serem carregados
            frame.setVisible(true);
        });
    }


    private void onSelectDb() throws IOException {
        // Request API
        URL url = new URL("http://localhost:8080/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(3000);

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Cria uma string para os dados recebidos
            StringBuilder response = new StringBuilder();
            String line;
            // Loop que pega os valores de dentro do buffer e passa para o line e depois para a string
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Encerra a conexão
            reader.close();
            connection.disconnect();

            String jsonResponse = response.toString();

            Gson gson = new Gson();

            Map<String, Object> responseData = gson.fromJson(jsonResponse, new TypeToken<Map<String, Object>>() {
            }.getType());
            String primeiraVolta = (String) responseData.get("primeira_volta");
            String segundaVolta = (String) responseData.get("segunda_volta");
            String totalVoltas = (String) responseData.get("total_voltas");

            firstLap.setText("Primeira volta: " + primeiraVolta);
            secondLap.setText("Segunda volta: " + segundaVolta);
            totalLap.setText("Total de voltas: " + totalVoltas);

        } else {
            JOptionPane.showMessageDialog(null, "Erro ao realizar consulta com o servidor: " + responseCode, "Erro GET request", JOptionPane.ERROR_MESSAGE);
        }

        connection.disconnect();
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
            String postData = String.valueOf(data);

            // Submit API
            String apiUrl = "http://localhost:8080/submit";

            // Criação da conexão HTTP
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true); // Habilita a saída para enviar dados no corpo da solicitação

            // Escreve os dados no corpo da solicitação
            try (OutputStream os = connection.getOutputStream()) {
                byte[] postDataBytes = postData.getBytes("UTF-8");
                os.write(postDataBytes);
            }

            // Verifica o código de resposta
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                // Mensagem de sucesso caso item seja inserido
                ImageIcon icon = new ImageIcon("src/img/check-check.png");
                JOptionPane.showMessageDialog(null, "Tempo inserido com sucesso", "Sucesso", JOptionPane.PLAIN_MESSAGE, icon);
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao inserir tempo: ", "Error", JOptionPane.WARNING_MESSAGE);
            }

            connection.disconnect();
        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir valores dentro do servidor: " + error, "Erro POST request", JOptionPane.ERROR_MESSAGE);
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
            } catch (IOException error) {
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
