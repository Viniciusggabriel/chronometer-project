package view.window.layout;

import view.window.events.ButtonsEvents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

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
        frame = new Frame();
        frame.setTitle("APS");
        frame.setSize(frameMaxWidth, frameMaxHeight);
        frame.addWindowListener(this);
        frame.setLayout(new GridBagLayout());

        // Painel para o timer
        JPanel panelTimer = new JPanel();
        panelTimer.setBackground(new Color(255, 0, 0));

        // Painel para as voltas
        JPanel panelLaps = new JPanel();
        panelLaps.setBackground(new Color(0, 30, 255));

        // Painel para os botões
        JPanel panelButtons = new JPanel();
        panelButtons.setBackground(new Color(255, 255, 0));

        /*Cria as labels para o timer e insere eles no painel*/
        // Label de milissegundos
        lMilliseconds = new JLabel("Milissegundos: 000");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panelTimer.add(lMilliseconds, gridBagConstraints);
        // Label de segundos
        lSeconds = new JLabel("Segundos: 00");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panelTimer.add(lSeconds, gridBagConstraints);
        // Label de minutos
        lMinutes = new JLabel("Minutos: 00");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panelTimer.add(lMinutes, gridBagConstraints);

        /*Cria os labels para as voltas*/
        // Label para a primeira volta
        lFirstLap = new JLabel("Primeira volta: 00:00:00");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panelLaps.add(lFirstLap, gridBagConstraints);
        // Label para a segunda volta
        lSecondLap = new JLabel("Segunda volta: 00:00:00");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panelLaps.add(lSecondLap, gridBagConstraints);
        // Label para o total da duas voltas
        lTotalLap = new JLabel("Total das voltas: 00:00:00");
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
        bReset = new JButton("Reset");
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

        // Botão de reset desabilitado ao iniciar
        bReset.setEnabled(false);
    }

    /* Métodos para inserir valores nos labels */
    // Método para inserir no timer
    @Override
    public void setMilliseconds(String milliseconds) {
        lMilliseconds.setText("Milliseconds: " + milliseconds);
    }

    @Override
    public void setSeconds(String seconds) {
        lSeconds.setText("Segundos: " + seconds);
    }

    @Override
    public void setMinutes(String minutes) {
        lMinutes.setText("Minutos: " + minutes);
    }

    public void setTrueReset(boolean state) {
        bReset.setEnabled(state);
    }

    // Método para inserir as voltas
    @Override
    public void setLaps(Timer firstLap, Timer secondLap, Timer totalLaps) {
        lMilliseconds.setText(String.valueOf(firstLap));
        lSeconds.setText(String.valueOf(secondLap));
        lMinutes.setText(String.valueOf(totalLaps));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jButton = (JButton) e.getSource();

        ButtonsEvents buttonsEvents = new ButtonsEvents(this);

        if (jButton.equals(bRun)) {
            buttonsEvents.buttonRun(bRun);
        } else if (jButton.equals(bReset)) {
            buttonsEvents.buttonReset(bReset);
        } else {
            buttonsEvents.buttonRequest(bRequest);
        }
    }

    public void setFrame() {
        frame.setVisible(true);
    }
}