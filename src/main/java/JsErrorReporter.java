package main.java;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

public class JsErrorReporter implements ErrorReporter {

	@Override
	public void error(String message, String sourceName,
                      int line, String lineSource, int lineOffset) {

        System.err.println("\n[WARNING] in " + localFilename);
        if (line < 0) {
            System.err.println("  " + message);
        } else {
            System.err.println("  " + line + ':' + lineOffset + ':' + message);
        }
	}

	@Override
	public EvaluatorException runtimeError(String arg0, String arg1, int arg2, String arg3, int arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void warning(String arg0, String arg1, int arg2, String arg3, int arg4) {
		// TODO Auto-generated method stub

	}
}
