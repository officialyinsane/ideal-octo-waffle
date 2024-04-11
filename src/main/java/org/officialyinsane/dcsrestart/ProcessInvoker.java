package org.officialyinsane.dcsrestart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class ProcessInvoker {

    private static final List<String> adminArguments = List.of("runas.exe", "/savecred", "/user:");

    private final String pathToExecutable;
    private final List<String> arguments;

    public void invoke(boolean elevatePermissions, String username) throws IOException {
        int lastIndex = adminArguments.size() -1;
        String credential = adminArguments.get(lastIndex) + username;

        Stream<String> argStream;
        if (elevatePermissions) {
            argStream = Stream.concat(
                    Stream.concat(
                            adminArguments.subList(0, lastIndex).stream(),
                            Stream.of(credential)), arguments.stream());
        } else argStream = Stream.concat(Stream.of(pathToExecutable), arguments.stream());

        ProcessBuilder builder = new ProcessBuilder(argStream.toList());
        builder.redirectErrorStream(true);
        builder.redirectOutput(ProcessBuilder.Redirect.DISCARD);

        Process p = builder.start();

        log.info("Successfully started {}", pathToExecutable);

    }

}
