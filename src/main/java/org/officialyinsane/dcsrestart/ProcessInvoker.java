package org.officialyinsane.dcsrestart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

@RequiredArgsConstructor
@Slf4j
public class ProcessInvoker {

    private static final List<String> adminArguments = List.of("runas.exe", "/savecred", "/user:");

    private final String pathToExecutable;
    private final List<String> arguments;

    public Process invoke(boolean elevatePermissions, String username, String password) throws IOException {
        int lastIndex = adminArguments.size() -1;
        String credential = adminArguments.get(lastIndex) + username;

        Stream<String> minimumPermissionsExecution = Stream.concat(Stream.of(pathToExecutable), arguments.stream());
        Stream<String> argStream;
        if (elevatePermissions) {
            String originalArguments = minimumPermissionsExecution.collect(joining(" "));
            argStream = Stream.concat(
                            adminArguments.subList(0, lastIndex).stream(),
                            Stream.of(credential, "\"" + originalArguments + "\""));
        } else argStream = minimumPermissionsExecution;

        List<String> argList = argStream.toList();
        log.info("About to execute: " + argList);

        ProcessBuilder builder = new ProcessBuilder(argList);
        builder.redirectErrorStream(true);
        builder.redirectOutput(ProcessBuilder.Redirect.DISCARD);

        Process p = builder.start();
        if (elevatePermissions) try (OutputStream os = p.getOutputStream()) {
            String typedPassword = password + "\n";
            os.write(typedPassword.getBytes(UTF_8));
            os.flush();
        }

        log.info("Started {}", pathToExecutable);
        return p;
    }

}
