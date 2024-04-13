package view.window.events;

import view.client.connection.ClientConnectionFactoryManage;
import view.client.routes.PostClient;
import view.dto.OperationPostClientResult;
import view.window.layout.Layout;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.Duration;
import java.util.Locale;

public class ButtonsEvents implements ButtonsEventsFactory {
    private final Layout layout;
    Timer timer;
    private int minutesTimer, secondsTimer, millisecondsTimer = 0;
    private String resultTimer;

    public ButtonsEvents(Layout layout) {
        this.layout = layout;
    }

    @Override
    public void buttonRun(JButton bRun) {
        bRun.setBackground(Color.GREEN);
        // Evento para mudar o cronometro
        timer = new Timer(1, event -> {
            millisecondsTimer++;
            layout.setMilliseconds(String.valueOf(millisecondsTimer));
            if (millisecondsTimer >= 1000) {
                secondsTimer++;
                millisecondsTimer = 0;
                layout.setSeconds(String.valueOf(secondsTimer));
                if (secondsTimer >= 60) {
                    minutesTimer++;
                    secondsTimer = 0;
                    layout.setMinutes(String.valueOf(minutesTimer));
                }
            }

            Duration duration = Duration.ofMinutes(minutesTimer).plusSeconds(secondsTimer).plusMillis(millisecondsTimer); // Pega todos os tempos e soma
            long durationToMilli = duration.toMillis();
            resultTimer = String.format(Locale.ENGLISH, "%02d:%02d:%03d",
                    durationToMilli / 60000,
                    (durationToMilli % 60000) / 1000,
                    (durationToMilli % 1000));
        });
        timer.start();
        layout.setTrueReset(true);
    }

    @Override
    public void buttonReset(JButton bReset) {
        bReset.setBackground(Color.GREEN);

        ClientConnectionFactoryManage clientConnectionFactoryManage = new ClientConnectionFactoryManage();
        PostClient postClient = new PostClient(clientConnectionFactoryManage);

        OperationPostClientResult operationPostClientResult;
        try {
            operationPostClientResult = postClient.clientPostHandler(String.valueOf(resultTimer));

            if (operationPostClientResult.operationResult().operationCode() != 201) {
                JOptionPane.showMessageDialog(null, operationPostClientResult.operationResult());
            } else {
                JOptionPane.showMessageDialog(null, operationPostClientResult.operationResult() + " " + operationPostClientResult.data());
            }
        } catch (IOException error) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir valores para requisição: " + error);
        } finally {
            timer.stop();

            // Reinicie as variáveis do cronômetro
            millisecondsTimer = 0;
            secondsTimer = 0;
            minutesTimer = 0;

            // Atualize a exibição do cronômetro para mostrar 00:00:000
            layout.setMilliseconds("000");
            layout.setSeconds("00");
            layout.setMinutes("00");

            // Reinicie o cronômetro
            buttonRun(bReset);
        }
    }

    @Override
    public void buttonRequest(JButton bRequest) {
        bRequest.setBackground(Color.GREEN);
    }
}
