package view.ui.frame;

import view.ui.service.HttpGetClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;

public class Layout extends JDialog {
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
    private final int hoursTimer = 0;
    private final int minutesTimer = 0;
    private final int secondsTimer = 0;
    private final LocalTime resultTimer = LocalTime.of(00, 00, 00);
    private final boolean activatedTimer = false;

    public Layout() {
        setContentPane(contentPanel);
        setModal(true);

        buttonSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HttpGetClient httpGetClient = new HttpGetClient();
                httpGetClient.getTime();
            }
        });
    }
}
