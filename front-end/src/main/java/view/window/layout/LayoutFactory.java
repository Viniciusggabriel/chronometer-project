package view.window.layout;

import javax.swing.*;

public interface LayoutFactory {
    void setLaps(int minutes1, int seconds1, int milliseconds1,
                 int minutes2, int seconds2, int milliseconds2,
                 int minutesTotal, int secondsTotal, int millisecondsTotal);
    Object[] getLaps();

    void setFrame();
}
