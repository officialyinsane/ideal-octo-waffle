package org.officialyinsane.dcsrestart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class ProcessInvoker {

    private final String pathToExecutable;
    private final List<String> arguments;

    public void invoke() throws IOException {
        List<String> launchList = Stream.concat(Stream.of(pathToExecutable), arguments.stream()).toList();

        ProcessBuilder builder = new ProcessBuilder(launchList);
        builder.redirectErrorStream(true);
        builder.redirectOutput(ProcessBuilder.Redirect.DISCARD);

        Process p = builder.start();

        log.info("Successfully started {}", pathToExecutable);

    }

}
