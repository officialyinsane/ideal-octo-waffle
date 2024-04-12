package org.officialyinsane.dcsrestart;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.fail;

public class ProcessInvokerTest {

    private static final String NOTEPAD_PP_EXE = "C:\\Program Files\\Notepad++\\notepad++.exe";
    private static final String ADMIN_USER = "redte";
    private static final String ADMIN_PASSWORD = "not-putting-this-in-git-rofl";

    private ProcessInvoker testSubject;

    @Test
    public void testStartupWithoutAdmin() {
        testSubject = new ProcessInvoker(NOTEPAD_PP_EXE, List.of("--server"));

        try {
            testSubject.invoke(false, null, null);
        } catch (IOException e) {
            fail("Exceptions are bad!", e);
        }
    }

    @Test
    public void testStartupWithAdmin() {
        testSubject = new ProcessInvoker(NOTEPAD_PP_EXE, List.of("--server"));

        try {
            testSubject.invoke(true, ADMIN_USER, ADMIN_PASSWORD);
        } catch (IOException e) {
            fail("Exceptions are bad!", e);
        }
    }
}
