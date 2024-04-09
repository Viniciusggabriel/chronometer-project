package view.ui;

import view.ui.frame.Layout;

public class MainFrame {
    public static void main(String[] args) {
        Layout dialog = new Layout();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
