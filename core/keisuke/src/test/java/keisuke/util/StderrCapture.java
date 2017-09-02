package keisuke.util;

import java.io.BufferedOutputStream;
import java.io.PrintStream;

/**
 * Capturing System.err during a test.
 */
public class StderrCapture extends StdxxxCapture {

	public StderrCapture() {
		super();
		this.setSavedStream(System.err);
		System.setErr(new PrintStream(new BufferedOutputStream(this.actualMessage())));
	}

	/**
	 * get captured text which was printed to System.err.
	 * @return String captured text which was printed to System.err
	 */
	public String getCapturedString() {
		return super.getCapturedString(System.err);
	}

	/**
	 * Finalize of StderrCapture.
	 * set saved original System.err stream to System.err.
	 * print the captured text into System.err.
	 * close the stream used for capturing.
	 */
	public void finish() {
		System.setErr(this.savedStream());
		super.finish(System.err);
	}
}
