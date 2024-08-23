package com.alcanl.app.process.child.main;


import lombok.Getter;
import lombok.experimental.Accessors;
import java.io.IOException;

import static com.alcanl.app.process.child.Common.*;

@Getter
@Accessors(prefix = "m_")
public class MainAppInitializerProcess {
    private Process m_mainProcess;
    private final String m_username;
    private final String m_password;

    public MainAppInitializerProcess(String username, String password)
    {
        m_username = username;
        m_password = password;
    }
    public void startProcess() throws IOException {
        m_mainProcess = new ProcessBuilder(ms_starterCommandJava, ms_starterCommandJar, ms_mainAppPath,
                ms_starterDatabaseUsernamePropArgument.formatted(m_username),
                ms_starterDatabasePasswordPropArgument.formatted(m_password))
                .redirectErrorStream(true).start();
    }

    public boolean isProcessAlive()
    {
        return m_mainProcess.isAlive();
    }

    public void destroyProcess()
    {
        m_mainProcess.destroy();
    }

}
