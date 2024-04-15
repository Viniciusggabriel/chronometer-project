package view.window.layout;

import view.window.events.ButtonsEvents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.IOException;

public class Layout extends WindowAdapter implements ActionListener, LayoutFactory {
    private final Frame frame;
    private final JLabel lMilliseconds, lSeconds, lMinutes;
    JLabel lFirstLap, lSecondLap, lTotalLap;
    private final JButton bRun, bReset, bRequest;

    public Layout() {
        // Pega tamanho da tela
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        int frameMaxWidth = (int) (dimension.getWidth() * 0.4);
        int frameMaxHeight = (int) (dimension.getHeight() * 0.4);

        // Grid do layout
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        // Coloca os painéis esticados
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;

        // Frame que vai criar a janela
        frame = new Frame("APS");
        frame.pack();
        frame.setSize(frameMaxWidth, frameMaxHeight);
        frame.addWindowListener(this);
        frame.setLayout(new GridBagLayout());

        // Painel para o timer
        JPanel panelTimer = new JPanel(new GridLayout(1, 3));

        // Painel para as voltas
        JPanel panelLaps = new JPanel(new GridLayout(3, 1));

        // Painel para os botões
        JPanel panelButtons = new JPanel(new GridLayout(3, 1));

        /*Cria as labels para o timer e insere eles no painel*/
        // Label de milissegundos
        lMilliseconds = new JLabel("milliseconds: 000");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panelTimer.add(lMilliseconds, gridBagConstraints);
        // Label de segundos
        lSeconds = new JLabel("seconds: 00");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panelTimer.add(lSeconds, gridBagConstraints);
        // Label de minutos
        lMinutes = new JLabel("minutes: 00");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panelTimer.add(lMinutes, gridBagConstraints);

        /*Cria os labels para as voltas*/
        // Label para a primeira volta
        lFirstLap = new JLabel("Primeira volta: 00:00.000");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panelLaps.add(lFirstLap, gridBagConstraints);
        // Label para a segunda volta
        lSecondLap = new JLabel("Segunda volta: 00:00.000");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panelLaps.add(lSecondLap, gridBagConstraints);
        // Label para o total da duas voltas
        lTotalLap = new JLabel("Total voltas: 00:00.000");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panelLaps.add(lTotalLap, gridBagConstraints);

        /*Cria os botões e adiciona eventos*/
        // Botão de iniciar o timer
        bRun = new JButton("Iniciar");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        bRun.addActionListener(this);
        panelButtons.add(bRun, gridBagConstraints);
        // Botão que reset o timer e insere o valor atual
        bReset = new JButton("Inserir");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        bReset.addActionListener(this);
        panelButtons.add(bReset, gridBagConstraints);
        // Botão que pega valores da api
        bRequest = new JButton("Ultimas voltas");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        bRequest.addActionListener(this);
        panelButtons.add(bRequest, gridBagConstraints);

        /*Adiciona os painéis aos frames*/
        // Painel do timer
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        frame.add(panelTimer, gridBagConstraints);

        // Painel das voltas
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        frame.add(panelLaps, gridBagConstraints);

        // Painel dos botões
        gridBagConstraints.gridx = 1;
        frame.add(panelButtons, gridBagConstraints);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jButton = (JButton) e.getSource();

        ButtonsEvents buttonsEvents = new ButtonsEvents(this);

        if (jButton.equals(bRun)) {
            buttonsEvents.buttonRun(bRun);
        } else if (jButton.equals(bReset)) {
            try {
                buttonsEvents.buttonInsert(bReset);
            } catch (IOException error) {
                throw new RuntimeException(error);
            }
        } else {
            try {
                buttonsEvents.buttonGetLaps(bRequest);
            } catch (IOException error) {
                throw new RuntimeException(error);
            }
        }
    }

    @Override
    public void setFrame() {
        frame.setVisible(true);
    }

    // Métodos sets e gets
    @Override
    public void setLaps(int minutes1, int seconds1, int milliseconds1,
                        int minutes2, int seconds2, int milliseconds2,
                        int minutesTotal, int secondsTotal, int millisecondsTotal) {

        lFirstLap.setText(String.format("Primeira volta: %02d:%02d.%03d", minutes1, seconds1, milliseconds1));
        lSecondLap.setText(String.format("Segunda volta: %02d:%02d.%03d", minutes2, seconds2, milliseconds2));
        lTotalLap.setText(String.format("Total voltas: %02d:%02d.%03d", minutesTotal, secondsTotal, millisecondsTotal));

    }

    @Override
    public Object[] getLaps() {
        Object[] getLapsObject = new Object[3];

        getLapsObject[0] = lFirstLap.getText();
        getLapsObject[1] = lSecondLap.getText();
        getLapsObject[2] = lTotalLap.getText();

        return getLapsObject;
    }

    public void setMilliseconds(String milliseconds) {
        lMilliseconds.setText("milliseconds: " + milliseconds);
    }

    public void setSeconds(String seconds) {
        lSeconds.setText("seconds: " + seconds);
    }

    public void setMinutes(String minutes) {
        lMinutes.setText("minutes: " + minutes);
    }

    public String getMilliseconds() {
        return lMilliseconds.getText();
    }

    public String getSeconds() {
        return lSeconds.getText();
    }

    public String getMinutes() {
        return lMinutes.getText();
    }
}