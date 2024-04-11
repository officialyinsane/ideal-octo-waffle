package org.officialyinsane.dcsrestart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableScheduling
@Slf4j
public class WatcherConfiguration {

    @Value("#{'${app-arguments}'.split(',')}")
    private List<String> arguments;

    @Value("${path-to-exe}")
    private String pathToExecutable;

    @Value("${process-name}")
    private String processName;

    @Value("${priviledged-username}")
    private String priviledgedUsername;

    @Scheduled(cron = "0 * * * * ?")
    public void tick() {
        log.debug("Ticking...");
        processWatcher().tick();
    }

    public ProcessWatcher processWatcher() {
        return new ProcessWatcher(processName, failureHandler());
    }

    private Runnable failureHandler() {
        return () -> {
            try {
                processInvoker().invoke(
                        shouldRaisePriviledges(),
                        priviledgedUsername);
            } catch (IOException e) {
                log.error("An exception happened launching the application!", e);
            }
        };
    }

    private ProcessInvoker processInvoker() {
        return new ProcessInvoker(pathToExecutable, arguments);
    }

    private boolean shouldRaisePriviledges() {
        return priviledgedUsername != null && !priviledgedUsername.isEmpty();
    }
}
