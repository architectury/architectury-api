package me.shedaniel.architectury.test;

public interface MessageSink {
    void accept(String message);
    
    default void accept(String message, Object... args) {
        accept(String.format(message, args));
    }
}
