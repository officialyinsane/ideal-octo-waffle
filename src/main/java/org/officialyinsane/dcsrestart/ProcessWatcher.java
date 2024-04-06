package org.officialyinsane.dcsrestart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RequiredArgsConstructor
@Slf4j
public class ProcessWatcher {

    private final String processName;

    private final Runnable restartHook;

    private boolean suppressDuringRestart = false;

    public synchronized void tick() {
        if (suppressDuringRestart)
            return;

        try {
            Process process = Runtime.getRuntime().exec("tasklist");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                boolean isRunning = reader.lines()
                        .anyMatch(line -> line.contains(processName));
                if (!isRunning) {
                    suppressDuringRestart = true;
                    log.warn("Detected the process isn't running, starting it now!");
                    restartHook.run();
                    suppressDuringRestart = false;
                }
            }
        } catch (Exception e) {
            log.error("An exception happened!", e);
        }
    }

}
