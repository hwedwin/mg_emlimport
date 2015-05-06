package mgcommon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
public class Shell {
    static Logger log = LogManager.getLogger(Shell.class);

    static class StreamWorker extends Thread {
        InputStream is;
        String type;

        StreamWorker(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line=null;
                if (type.equals("error")) {
                    while ((line = br.readLine()) != null) {
                        log.error(line);
                    }
                } else if (type.equals("debug")) {
                    while ((line = br.readLine()) != null) {
                        log.debug(line);
                    }
                } else {
                    while ((line = br.readLine()) != null) {
                        log.info(line);
                    }
                }
            } catch (IOException e) {
                log.error("", e);  
            }
        }
    }

    public static int run(String cmd) {
        return run(cmd, "info");
    }

    public static int run(String cmd, String logType) {
        try {
            // http://docs.oracle.com/javase/7/docs/api/java/lang/Process.html
            // Because some native platforms only provide limited
            // buffer size for standard input and output streams,
            // failure to promptly write the input stream or read the
            // output stream of the subprocess may cause the
            // subprocess to block, or even deadlock.
            Process proc = Runtime.getRuntime().exec(cmd);

            StreamWorker output = new StreamWorker(proc.getInputStream(), logType);
            StreamWorker error = new StreamWorker(proc.getErrorStream(), "error");

            output.start();
            error.start();

            // return 0 indicates normal termination
            return proc.waitFor();

        } catch (IOException e) {
            log.error("", e);
        } catch (InterruptedException e) {
            log.error("", e);
        }
        return -1;
    }

    public static String runWithStdout(String cmd) {
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            
            StreamWorker err = new StreamWorker(proc.getErrorStream(), "error");
            err.start();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            String stdout = "";
            while ((line = br.readLine()) != null) {
                stdout += (line + "\n");
            }
            proc.waitFor();
            return stdout;
        } catch (IOException e) {
            log.error("", e);
        } catch (InterruptedException e) {
            log.error("", e);
        }
        return "";
    }

    public static int runWithStdin(String cmd, String input) {
        return runWithStdin(cmd, input, "info");
    }

    public static int runWithStdin(String cmd, String input, String logType) {
        try {
            Process proc = Runtime.getRuntime().exec(cmd);

            OutputStream stdinWriter = proc.getOutputStream();
            stdinWriter.write(input.getBytes(StandardCharsets.UTF_8));
            stdinWriter.close();

            StreamWorker output = new StreamWorker(proc.getInputStream(), logType);
            StreamWorker error = new StreamWorker(proc.getErrorStream(), "error");
            output.start();
            error.start();

            return proc.waitFor();

        } catch (IOException e) {
            log.error("", e);
        } catch (InterruptedException e) {
            log.error("", e);
        }

        return -1;
    }
}
