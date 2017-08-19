package keisuke;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Capturing System.err during a test.
 * @author strikebuster
 *
 */
public class StderrCapture {
	private PrintStream savedErr;
	private ByteArrayOutputStream actualErrMessage = null;
	private String messageText = null;

	public StderrCapture() {
		savedErr = System.err;
		actualErrMessage = new ByteArrayOutputStream();
		System.setErr(new PrintStream(new BufferedOutputStream(actualErrMessage)));
	}

	/**
	 * get captured text which was printed to System.err.
	 * @return String captured text which was printed to System.err
	 */
	public String getCapturedString() {
		System.err.flush();
		messageText = actualErrMessage.toString();
		return messageText;
	}

	/**
	 * Finalize of StderrCapture.
	 * set saved original System.err stream to System.err.
	 * print the captured text into System.err.
	 * close the stream used for capturing.
	 */
	public void finish() {
		System.setErr(savedErr);
		if (messageText != null) {
			System.err.print(messageText);
		}
		try {
			if (actualErrMessage != null) actualErrMessage.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
