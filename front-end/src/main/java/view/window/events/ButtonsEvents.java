package view.window.events;

import com.google.gson.Gson;
import view.client.connection.ClientConnectionManage;
import view.client.routes.GetClient;
import view.client.routes.PostClient;
import view.dto.OperationGetClientResult;
import view.dto.OperationPostClientResult;
import view.dto.OperationResult;
import view.window.layout.Layout;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

public class ButtonsEvents implements ButtonsEventsFactory {
    Layout layout;
    Thread stopWatch;
    private static volatile boolean timerState = true;
    private long millisecondsTimer, secondsTimer, minutesTimer, hoursTimer;

    public ButtonsEvents(Layout layout) {
        this.layout = layout;
    }

    @Override
    public void buttonRun(JButton bRun) {
        bRun.setBackground(Color.green);

        stopWatch = new Thread(() -> {
            long startTime = System.currentTimeMillis(); // Tempo de inicio do sistema

            while (timerState) {
                long elapsedTime = System.currentTimeMillis() - startTime; // Tempo atual sendo comparado com o de inicio

                minutesTimer = elapsedTime / (1000 * 60);
                secondsTimer = (elapsedTime / 1000) % 60;
                millisecondsTimer = elapsedTime % 1000;

                layout.setMilliseconds(String.valueOf(millisecondsTimer));
                layout.setSeconds(String.valueOf(secondsTimer));
                layout.setMinutes(String.valueOf(minutesTimer));

                try {
                    Thread.sleep(1);
                } catch (InterruptedException error) {
                    JOptionPane.showMessageDialog(null, "Erro ao iniciar a cronometro: " + error);
                }
            }
        });

        stopWatch.start();
    }

    @Override
    public void buttonInsert(JButton bInsert) throws IOException {
        timerState = false;

        if (stopWatch != null) {
            try {
                stopWatch.join();
            } catch (InterruptedException error) {
                JOptionPane.showMessageDialog(null, "Erro ao encerrar cronometro: " + error);
            }
        }

        ClientConnectionManage clientConnectionManage = new ClientConnectionManage();
        PostClient postClient = new PostClient(clientConnectionManage);

        int milliseconds = Integer.valueOf(layout.getMilliseconds().split(": ")[1]);
        int seconds = Integer.parseInt(layout.getSeconds().split(": ")[1]);
        int minutes = Integer.parseInt(layout.getMinutes().split(": ")[1]);

        OperationPostClientResult operationPostClientResult = new OperationPostClientResult(milliseconds, seconds, minutes, 0);

        Gson jsonObject = new Gson();
        String data = jsonObject.toJson(operationPostClientResult);
        System.out.println(data);

        OperationResult result = postClient.clientPostHandler(data);

        if (result.operationCode() != 201) {
            JOptionPane.showMessageDialog(null, "Código: " + result.operationCode() + " Mensagem: " + result.operationMessage());
        } else {
            JOptionPane.showMessageDialog(null, result.operationMessage());
        }
    }

    @Override
    public void buttonGetLaps(JButton bRequest) throws IOException {
        ClientConnectionManage clientConnectionManage = new ClientConnectionManage();
        GetClient getClient = new GetClient(clientConnectionManage);

        OperationGetClientResult operationGetClientResult = getClient.clientGetHandler();

        if (operationGetClientResult.operationResult().operationCode() != 200) {
            JOptionPane.showMessageDialog(null, "Código: " + operationGetClientResult.operationResult().operationCode() + " Mensagem: " + operationGetClientResult.operationResult().operationMessage());
        }

        Map<String, Object> response = operationGetClientResult.response();
        double milliseconds1 = 0.0, seconds1 = 0.0, minutes1 = 0.0, hours1 = 0.0;
        double milliseconds2 = 0.0, seconds2 = 0.0, minutes2 = 0.0, hours2 = 0.0;


        for (String key : response.keySet()) {
            Map<String, Integer> laps = (Map<String, Integer>) response.get(key);

            for (String times : laps.keySet()) {
                Object valor = laps.get(times);

                switch (times) {
                    case "milliseconds":
                        if (key.equals("Primeira volta")) {
                            milliseconds1 = (double) valor;
                        } else if (key.equals("Segunda volta")) {
                            milliseconds2 = (double) valor;
                        }
                        break;
                    case "seconds":
                        if (key.equals("Primeira volta")) {
                            seconds1 = (double) valor;
                        } else if (key.equals("Segunda volta")) {
                            seconds2 = (double) valor;
                        }
                        break;
                    case "minutes":
                        if (key.equals("Primeira volta")) {
                            minutes1 = (double) valor;
                        } else if (key.equals("Segunda volta")) {
                            minutes2 = (double) valor;
                        }
                        break;
                    case "hours":
                        if (key.equals("Primeira volta")) {
                            hours1 = (double) valor;
                        } else if (key.equals("Segunda volta")) {
                            hours2 = (double) valor;
                        }
                        break;
                }
            }
        }

        // Soma de valores
        int minutesTotal = (int) minutes1 + (int) minutes2;
        int secondsTotal = (int) seconds1 + (int) seconds2;
        int millisecondsTotal = (int) milliseconds1 + (int) milliseconds2;
        int hoursTotal = (int) hours1 + (int) hours2;


        // Validação
        if (millisecondsTotal >= 1000) {
            secondsTotal += millisecondsTotal / 1000;
            millisecondsTotal %= 1000;
        }

        if (secondsTotal >= 60) {
            minutesTotal += secondsTotal / 60;
            secondsTotal %= 60;
        }

        if (minutesTotal >= 60) {
            hoursTotal += minutesTotal / 60;
            minutesTotal %= 60;
        }

        // Insere os valores no layout
        layout.setLaps((int) minutes1, (int) seconds1, (int) milliseconds1,
                (int) minutes2, (int) seconds2, (int) milliseconds2,
                minutesTotal, secondsTotal, millisecondsTotal);
    }
}