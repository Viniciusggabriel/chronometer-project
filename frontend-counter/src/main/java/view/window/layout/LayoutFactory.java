package view.window.layout;

import javax.swing.*;

public interface LayoutFactory {
    void setMilliseconds(String seconds);

    void setSeconds(String minutes);

    void setMinutes(String hours);

    void setTrueReset(boolean state);

    void setLaps(Timer firstLap, Timer secondLap, Timer totalLaps);
}
