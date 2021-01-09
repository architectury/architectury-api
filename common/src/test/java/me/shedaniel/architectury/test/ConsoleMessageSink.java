package me.shedaniel.architectury.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsoleMessageSink implements MessageSink {
    protected final Logger logger = LogManager.getLogger("Architectury Test");
    
    @Override
    public void accept(String message) {
        logger.info(message);
    }
}
