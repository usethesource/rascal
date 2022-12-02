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
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverLogLevel;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
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
    private final ChromeDriverService service;
    private RemoteWebDriver driver; 


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
 
        TutorIDEServices services = new TutorIDEServices();
        repl.initialize(shellInputNotUsed, shellStandardOutput, shellErrorOutput, services);
        repl.setMeasureCommandTime(false); 

        this.service = new ChromeDriverService.Builder()         
            .usingDriverExecutable(new File(System.getProperty("webdriver.chrome.driver")))         
            .usingAnyFreePort()         
            .build();    

        this.service.start();
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
            
            RemoteWebDriver browser = getBrowser();

            if (metadata.get("url") != null && browser != null) {
                try {
                    browser.get(metadata.get("url"));
                    browser.manage().window().maximize();
                    // waiting for a better solution 
                    Thread.sleep(1000);    

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

    private RemoteWebDriver getBrowser() {
        // System.setProperty("webdriver.gecko.driver", "/Users/jurgenv/Downloads/geckodriver");
        if (this.driver != null) {
            return this.driver;
        }
        

        ChromeOptions options = new ChromeOptions()
            .setHeadless(true)
            .setBinary(System.getProperty("webdriver.chrome.browser"))
            .addArguments("--user-data-dir=/tmp/rascal-config/google-chrome")
            .setLogLevel(ChromeDriverLogLevel.OFF)
            ;

        // ?ChromeProfile profile = options.getProfile();
        // profile.setPreference("layout.css.devPixelsPerPx", "3");
        // options = options.setProfile(profile);
        

        RemoteWebDriver driver = new RemoteWebDriver(service.getUrl(), options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(3));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        
        this.driver = driver;
        
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
