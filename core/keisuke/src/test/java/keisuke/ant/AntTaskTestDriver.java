package keisuke.ant;

import static keisuke.util.StringUtil.LINE_SEP;

import java.io.File;
import java.io.PrintStream;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.util.ProcessUtil;

/**
 * Antのタスククラスをテストするためのドライバー
 */
public class AntTaskTestDriver {

	private Project project;
    private StringBuffer logBuffer;
    private StringBuffer fullLogBuffer;
    private StringBuffer outBuffer;
    private StringBuffer errBuffer;
    //private BuildException buildException;

	/**
     * Sets up to run the named project
     *
     * @param  filename name of project file to run
     * @throws BuildException fired when ant has an error
     */
    public void configureProject(final String filename) throws BuildException {
        configureProject(filename, Project.MSG_DEBUG);
        //configureProject(filename, Project.MSG_INFO);
    }

    /**
     * Sets up to run the named project
     *
     * @param  filename name of project file to run
     * @param logLevel level of logging
     * @throws BuildException fired when ant has an error
     */
    public void configureProject(final String filename, final int logLevel)
        throws BuildException {
        logBuffer = new StringBuffer();
        fullLogBuffer = new StringBuffer();
        project = new Project();
        project.init();
        File antFile = new File(System.getProperty("root"), filename);
        project.setUserProperty("ant.file", antFile.getAbsolutePath());
        // set two new properties to allow to build unique names when running multithreaded tests
        project.setProperty("ant.processid", ProcessUtil.getProcessId("<Process>"));
        project.setProperty("ant.threadname", Thread.currentThread().getName());
        project.addBuildListener(new AntTestListener(logLevel));
        ProjectHelper.configureProject(project, antFile);
    }

    /**
     * Executes a target we have set up
     *
     * @pre configureProject has been called
     * @param  targetName  target to run
     */
    public void executeTarget(final String targetName) {
        PrintStream sysOut = System.out;
        PrintStream sysErr = System.err;
        try {
            sysOut.flush();
            sysErr.flush();
            outBuffer = new StringBuffer();
            PrintStream out = new PrintStream(new AntOutputStream(outBuffer));
            System.setOut(out);
            errBuffer = new StringBuffer();
            PrintStream err = new PrintStream(new AntOutputStream(errBuffer));
            System.setErr(err);
            logBuffer = new StringBuffer();
            fullLogBuffer = new StringBuffer();
            //buildException = null;
            project.executeTarget(targetName);
        } finally {
            System.setOut(sysOut);
            System.setErr(sysErr);
        }

    }

    /**
     * Gets the INFO, WARNING and ERROR message from the current execution,
     * unless the logging level is set above any of these level in which case
     * the message is excluded.
     * This is only valid if configureProject() has been called.
     *
     * @return The INFO, WARN and ERROR messages in the log.
     */
    public String getLog() {
        return logBuffer.toString();
    }

    /**
     * Gets any messages that have been logged during the current execution, unless
     * the logging level has been set above the log level defined in the message.
     *
     * Only valid if configureProject() has been called.
     * @return the content of the log.
     */
    public String getFullLog() {
        return fullLogBuffer.toString();
    }

    /**
     * Provides all output sent to the System.out stream during the current execution.
     * @return all output messages in a single string, normalised to have platform independent line breaks.
     */
    public String getStdout() {
        //return cleanBuffer(outBuffer);
        return outBuffer.toString();
    }

    /**
     * Provides all output sent to the System.err stream during the current execution.
     * @return all error messages in a single string, normalised to have platform independent line breaks.
     */
    public String getStderr() {
        //return cleanBuffer(errBuffer);
    	return errBuffer.toString();
    }

    /*
    private String cleanBuffer(final StringBuffer buffer) {
        StringBuilder cleanedBuffer = new StringBuilder();
        for (int i = 0; i < buffer.length(); i++) {
            char ch = buffer.charAt(i);
            if (ch != '\r') {
                cleanedBuffer.append(ch);
            }
        }
        return cleanedBuffer.toString();
    }
    */

    /*/**
     * 発行されたBuildExceptionインスタンスを返す
     * @return 発行されたBuildExceptionインスタンス
     */
    //public BuildException getBuildException() {
    //    return buildException;
    //}

    /**
     * an output stream which saves stuff to our buffer.
     */
    protected static class AntOutputStream extends java.io.OutputStream {
        private StringBuffer strbuffer;

        /**
         * Constructs a output stream for saving what will be written by ant task
         * @param buffer StingBuffrt instance
         */
        public AntOutputStream(final StringBuffer buffer) {
            this.strbuffer = buffer;
        }

        /**
         * Writes a character into the buffer
         * @param b integer which is casted a character
         */
        public void write(final int b) {
            strbuffer.append((char) b);
        }
    }

    /**
     * Our own personal build listener.
     */
    private class AntTestListener implements BuildListener {
        private int logLevel;

        /**
         * Constructs a test listener which will ignore log events
         * above the given level.
         *
         * @param level log level
         */
        AntTestListener(final int level) {
            this.logLevel = level;
        }

        /**
         * Fired before any targets are started.
         *
         * @param event BuildEvent instance
         */
        public void buildStarted(final BuildEvent event) {
        }

        /**
         * Fired after the last target has finished. This event
         * will still be thrown if an error occurred during the build.
         *
         * @param event BuildEvent instance
         * @see BuildEvent#getException()
         */
        public void buildFinished(final BuildEvent event) {
        }

        /**
         * Fired when a target is started.
         *
         * @param event BuildEvent instance
         * @see BuildEvent#getTarget()
         */
        public void targetStarted(final BuildEvent event) {
            //System.out.println("targetStarted " + event.getTarget().getName());
        }

        /**
         * Fired when a target has finished. This event will
         * still be thrown if an error occurred during the build.
         *
         * @param event BuildEvent instance
         * @see BuildEvent#getException()
         */
        public void targetFinished(final BuildEvent event) {
            //System.out.println("targetFinished " + event.getTarget().getName());
        }

        /**
         * Fired when a task is started.
         *
         * @param event BuildEvent instance
         * @see BuildEvent#getTask()
         */
        public void taskStarted(final BuildEvent event) {
            //System.out.println("taskStarted " + event.getTask().getTaskName());
        }

        /**
         * Fired when a task has finished. This event will still
         * be throw if an error occurred during the build.
         *
         * @param event BuildEvent instance
         * @see BuildEvent#getException()
         */
        public void taskFinished(final BuildEvent event) {
            //System.out.println("taskFinished " + event.getTask().getTaskName());
        }

        /**
         * Fired whenever a message is logged.
         *
         * @param event BuildEvent instance
         * @see BuildEvent#getMessage()
         * @see BuildEvent#getPriority()
         */
        public void messageLogged(final BuildEvent event) {
            if (event.getPriority() > logLevel) {
                // ignore event
                return;
            }

            if (event.getPriority() == Project.MSG_INFO
                || event.getPriority() == Project.MSG_WARN
                || event.getPriority() == Project.MSG_ERR) {
                logBuffer.append(event.getMessage()).append(LINE_SEP);
            }
            fullLogBuffer.append(event.getMessage()).append(LINE_SEP);
        }
    }

}
