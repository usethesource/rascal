package lang.rascal.tutor.repl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.repl.RascalInterpreterREPL;
import org.rascalmpl.shell.ShellEvaluatorFactory;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;

public class TutorCommandExecutor {
    private final RascalInterpreterREPL repl;
    private final ByteArrayOutputStream shellStandardOutput;
    private final ByteArrayOutputStream shellErrorOutput;
    private final FirefoxDriver browser;

    public TutorCommandExecutor(PathConfig pcfg) throws IOException, URISyntaxException{
        shellStandardOutput = new ByteArrayOutputStream();
        shellErrorOutput = new ByteArrayOutputStream();
        ByteArrayInputStream shellInputNotUsed = new ByteArrayInputStream("***this inputstream should not be used***".getBytes());

        repl = new RascalInterpreterREPL(false, false, null) {
            @Override
            protected Evaluator constructEvaluator(InputStream input, OutputStream stdout, OutputStream stderr, IDEServices services) {
                Evaluator eval = ShellEvaluatorFactory.getDefaultEvaluator(input, stdout, stderr);
                eval.getConfiguration().setRascalJavaClassPathProperty(javaCompilerPathAsString(pcfg.getJavaCompilerPath()));
                eval.setMonitor(services);
                // eval.addClassLoader(new SourceLocationClassLoader(pcfg.getClassloaders(), System.class.getClassLoader()));
                return eval;
            }
        };

        repl.initialize(shellInputNotUsed, shellStandardOutput, shellErrorOutput, null);
        repl.setMeasureCommandTime(false); 

        this.browser = getBrowser();
    }

    private String javaCompilerPathAsString(IList javaCompilerPath) {
        StringBuilder b = new StringBuilder();

        for (IValue elem : javaCompilerPath) {
            ISourceLocation loc = (ISourceLocation) elem;

            if (b.length() != 0) {
                b.append(File.pathSeparatorChar);
            }

            assert loc.getScheme().equals("file");
            String path = loc.getPath();
            if (path.startsWith("/") && path.contains(":\\")) {
                // a windows path should drop the leading /
                path = path.substring(1);
            }
            b.append(path);
        }

        return b.toString();
    }

    public void reset() {
        try {
            // make sure previously unterminated commands are cleared up
            repl.handleInput("", new HashMap<>(), new HashMap<>());
        }
        catch (InterruptedException e) {
           // nothing needed
        }
        repl.cleanEnvironment();
        shellStandardOutput.reset();
        shellErrorOutput.reset();
    }

    public String getPrompt() {
        return repl.getPrompt();
    }
    
    public Map<String, String> eval(String line) throws InterruptedException, IOException {
        Map<String, InputStream> output = new HashMap<>();
        Map<String, String> result = new HashMap<>();
        Map<String, String> metadata = new HashMap<>();

        repl.handleInput(line, output, metadata);

        for (String mimeType : output.keySet()) {
            InputStream content = output.get(mimeType);

            if (mimeType.startsWith("text/plain")) {
                result.put(mimeType, Prelude.consumeInputStream(new InputStreamReader(content, StandardCharsets.UTF_8)));
            }
            else {
                result.put(mimeType, uuencode(content));
            }
            
            FirefoxDriver browser = getBrowser();
            if (metadata.get("url") != null && browser != null) {
                try {
                    browser.get(metadata.get("url"));
                    browser.manage().window().maximize();

                    String screenshot = browser.findElement(By.tagName("body"))
                        .getScreenshotAs(OutputType.BASE64);

                    result.put("application/rascal+screenshot", screenshot);
                }
                catch (Throwable e) {
                    shellErrorOutput.write(e.getMessage().getBytes("UTF-8"));
                }
            } 
        }

        result.put("application/rascal+stdout", getPrintedOutput());
        result.put("application/rascal+stderr", getErrorOutput());

        return result;
    }

    private FirefoxDriver getBrowser() {
        if (this.browser != null) {
            return browser;
        }

        FirefoxOptions options = new FirefoxOptions()
            .setAcceptInsecureCerts(true)
            .setHeadless(true)
            ;

        // System.setProperty("webdriver.gecko.driver", "/Users/jurgenv/Downloads/geckodriver");

        if (System.getProperty("webdriver.gecko.driver") == null) {
            return null;
        }

        FirefoxProfile profile = options.getProfile();
        profile.setPreference("layout.css.devPixelsPerPx", "3");

        options = options.setProfile(profile);

        
        FirefoxDriver driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        
        return driver;
    }

    public String uuencode(InputStream content) throws IOException {
        int BUFFER_SIZE = 3 * 512;
        Base64.Encoder encoder = Base64.getEncoder();
        
        try  (BufferedInputStream in = new BufferedInputStream(content, BUFFER_SIZE); ) {
            StringBuilder result = new StringBuilder();
            byte[] chunk = new byte[BUFFER_SIZE];
            int len = 0;
            
            // read multiples of 3 until not possible anymore
            while ( (len = in.read(chunk)) == BUFFER_SIZE ) {
                 result.append( encoder.encodeToString(chunk) );
            }
            
            // read final chunk which is not a multiple of 3
            if ( len > 0 ) {
                 chunk = Arrays.copyOf(chunk,len);
                 result.append( encoder.encodeToString(chunk) );
            }
            
            return result.toString();
        }
    }

    public boolean isStatementComplete(String line){
        return repl.isStatementComplete(line);
    }

    private String getPrintedOutput() throws UnsupportedEncodingException{
        try {
            repl.getOutputWriter().flush();
            String result = shellStandardOutput.toString(StandardCharsets.UTF_8.name());
            shellStandardOutput.reset();
            return result;
        }
        catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    private String getErrorOutput() {
        try {
            repl.getErrorWriter().flush();
            String result = shellErrorOutput.toString(StandardCharsets.UTF_8.name());
            shellErrorOutput.reset();
            return result;
        }
        catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}
