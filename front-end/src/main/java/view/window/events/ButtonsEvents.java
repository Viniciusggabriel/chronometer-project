package view.window.events;

import com.google.gson.Gson;
import view.client.connection.ClientConnectionManage;
import view.client.routes.PostClient;
import view.dto.OperationPostClientResult;
import view.dto.OperationResult;
import view.window.layout.Layout;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ButtonsEvents implements ButtonsEventsFactory {
    Gson gson = new Gson();
    Timer timer;
    private final Layout layout;
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
                if (minutesTimer >= 60) {
                    minutesTimer = 0;
                    secondsTimer = 0;
                    millisecondsTimer = 0;
                }
                OperationPostClientResult operationPostClientResult = new OperationPostClientResult(millisecondsTimer, secondsTimer, minutesTimer, 0);

                resultTimer = gson.toJson(operationPostClientResult);
            }
        });

        timer.start();
        layout.setTrueReset(true);
    }

    @Override
    public void buttonReset(JButton bReset) {
        bReset.setBackground(Color.GREEN);

        ClientConnectionManage clientConnectionManage = new ClientConnectionManage();
        PostClient postClient = new PostClient(clientConnectionManage);

        OperationResult operationResult; // Todo: resulTimer is null
        try {
            operationResult = postClient.clientPostHandler(resultTimer);

            if (operationResult.operationCode() != 201) {
                JOptionPane.showMessageDialog(null, "Erro: " + operationResult.operationMessage() + " Código: " + operationResult.operationCode());
            } else {
                JOptionPane.showMessageDialog(null, operationResult.operationMessage());
            }
        } catch (IOException error) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir valores para requisição: " + error);
        } finally {

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
