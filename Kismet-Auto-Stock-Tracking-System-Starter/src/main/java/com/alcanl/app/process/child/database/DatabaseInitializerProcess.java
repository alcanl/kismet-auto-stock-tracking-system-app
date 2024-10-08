package com.alcanl.app.process.child.database;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.IOException;

import static com.alcanl.app.process.child.Common.*;

@Accessors(prefix = "m_")
@Getter
@Setter
public class DatabaseInitializerProcess {
    private Process m_databaseIntializerProcess;
    private String m_username;
    private String m_password;

    public DatabaseInitializerProcess()
    {
    }
    public void startProcess() throws IOException
    {
        m_databaseIntializerProcess = new ProcessBuilder(ms_starterCommandJava, ms_starterCommandJar, ms_dbStarterAppPath,
                ms_starterDatabaseUsernamePropArgument.formatted(m_username),
                ms_starterDatabasePasswordPropArgument.formatted(m_password))
                .redirectErrorStream(true).start();
    }

    public boolean isProcessAlive()
    {
        return m_databaseIntializerProcess.isAlive();
    }

    public void destroyProcess()
    {
        m_databaseIntializerProcess.destroy();
    }
}
