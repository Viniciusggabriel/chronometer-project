package org.telemetry.entity;

public class TimeEntity {
    private int milliseconds, seconds, minutes, hours;

    public TimeEntity(int milliseconds, int seconds, int minutes, int hours) {
        this.milliseconds = milliseconds % 1000; // Modulo para limitar milissegundos entre 0 e 999
        this.seconds = (seconds + milliseconds / 1000) % 60; // Soma segundos e converte milissegundos excedentes, mod para limitar entre 0 e 59
        this.minutes = (minutes + seconds / 60) % 60; // Soma minutos e converte segundos excedentes, mod para limitar entre 0 e 59
        this.hours = (hours + minutes / 60) % 24; // Soma horas e converte minutos excedentes, modulo para limitar entre 0 e 23
        validateTime();
    }

    private void validateTime() {
        if (milliseconds < 0 || seconds < 0 || minutes < 0 || hours < 0) {
            throw new IllegalArgumentException("Os componentes não podem ser negativos");
        }
    }

    // Métodos gets
    public int getMilliseconds() {
        return milliseconds;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getHours() {
        return hours;
    }

    // Métodos sets
    public void setMilliseconds(int milliseconds) {
        this.milliseconds = milliseconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
}
