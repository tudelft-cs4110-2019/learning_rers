package RERSlearner;

/**
 * Created by rick on 17/03/2017.
 */

import com.google.common.collect.Lists;
import de.learnlib.api.SUL;
import de.learnlib.api.SULException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import javax.annotation.Nullable;

public class ProcessSUL implements SUL<String, String> {
    private final ProcessBuilder pb;
    private Process process;
    private Writer processInput;
    private BufferedReader processOutput;

    ProcessSUL(String path) {
        ArrayList command = Lists.newArrayList(new String[]{path});
        this.pb = new ProcessBuilder(command);
        this.pb.redirectErrorStream(true);
    }

    public boolean canFork() {
        return false;
    }

    public void pre() throws SULException {
        try {
            this.process = this.pb.start();
            this.processInput = new OutputStreamWriter(this.process.getOutputStream());
            this.processOutput = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
        } catch (IOException var2) {
            throw new SULException(var2);
        }
    }

    public void post() throws SULException {
        try {
            this.processInput.close();
            this.processOutput.close();
            this.process.destroy();
            this.process = null;
            this.processInput = null;
            this.processOutput = null;
        } catch (IOException var2) {
            throw new SULException(var2);
        }
    }

    @Nullable
    public String step(@Nullable String s) throws SULException {
        if(s == null) {
            return null;
        } else {
            try {
                this.processInput.write(s);
                this.processInput.write("\n");
                this.processInput.flush();

                while(!this.processOutput.ready()) {
                    Thread.sleep(1L);
                }

                StringBuilder e = new StringBuilder();

                while(this.processOutput.ready()) {
                    e.append(this.processOutput.readLine());
                    e.append(';');
                    Thread.sleep(1L);
                }

                return e.toString();
            } catch (Exception var3) {
                throw new SULException(var3);
            }
        }
    }
}