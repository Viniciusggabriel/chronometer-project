package view.window.events;

import javax.swing.*;
import java.io.IOException;

public interface ButtonsEventsFactory {
    void buttonRun(JButton bRun);

    void buttonReset(JButton bReset) throws IOException;

    void buttonGetLaps(JButton bRequest) throws IOException;

}
