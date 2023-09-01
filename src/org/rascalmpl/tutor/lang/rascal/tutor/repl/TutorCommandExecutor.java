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
import java.nio.file.Paths;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverLogLevel;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.repl.RascalInterpreterREPL;
import org.rascalmpl.shell.ShellEvaluatorFactory;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.SourceLocationClassLoader;
import org.rascalmpl.uri.project.ProjectURIResolver;
import org.rascalmpl.uri.project.TargetURIResolver;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class TutorCommandExecutor {
    private static final String BROWSER_BINARY = System.getProperty("webdriver.chrome.browser");
    private static final String DRIVER_BINARY = System.getProperty("webdriver.chrome.driver");
    private final RascalInterpreterREPL repl;
    private final ByteArrayOutputStream shellStandardOutput;
    private final ByteArrayOutputStream shellErrorOutput;
    private final ChromeDriverService service;
    private final RemoteWebDriver driver; 


    public TutorCommandExecutor(PathConfig pcfg) throws IOException, URISyntaxException{
        shellStandardOutput = new ByteArrayOutputStream();
        shellErrorOutput = new ByteArrayOutputStream();
        ByteArrayInputStream shellInputNotUsed = new ByteArrayInputStream("***this inputstream should not be used***".getBytes());
        repl = new RascalInterpreterREPL(false, false, null) {
            @Override
            protected Evaluator constructEvaluator(InputStream input, OutputStream stdout, OutputStream stderr, IDEServices services) {
                GlobalEnvironment heap = new GlobalEnvironment();
                ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
                IValueFactory vf = ValueFactoryFactory.getValueFactory();
                Evaluator eval = new Evaluator(vf, input, stderr, stdout, root, heap);

                eval.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
                eval.setMonitor(services);        
                eval.getConfiguration().setRascalJavaClassPathProperty(javaCompilerPathAsString(pcfg.getJavaCompilerPath()));
                eval.setMonitor(services);

                ISourceLocation projectRoot = inferProjectRoot((ISourceLocation) pcfg.getSrcs().get(0));
                String projectName = new RascalManifest().getProjectName(projectRoot);
                URIResolverRegistry reg = URIResolverRegistry.getInstance();
                reg.registerLogical(new ProjectURIResolver(projectRoot, projectName));
                reg.registerLogical(new TargetURIResolver(projectRoot, projectName));

                for (IValue path : pcfg.getSrcs()) {
                    eval.addRascalSearchPath((ISourceLocation) path); 
                }
    
                for (IValue path : pcfg.getLibs()) {
                    eval.addRascalSearchPath((ISourceLocation) path);
                }
    
                ClassLoader cl = new SourceLocationClassLoader(pcfg.getClassloaders(), ShellEvaluatorFactory.class.getClassLoader());
                eval.addClassLoader(cl);

                return eval;
            }
        };

        TutorIDEServices services = new TutorIDEServices();
        repl.initialize(shellInputNotUsed, shellStandardOutput, shellErrorOutput, services);
        repl.setMeasureCommandTime(false); 
 
        if (DRIVER_BINARY != null && BROWSER_BINARY != null) {
            this.service = new ChromeDriverService.Builder()         
                .usingDriverExecutable(new File(DRIVER_BINARY))         
                .usingAnyFreePort()         
                .build();    

            this.service.start();
            this.driver = getBrowser(service);
        }
        else {
            this.service = null;
            this.driver = null;
        }
    }

    private static ISourceLocation inferProjectRoot(ISourceLocation member) {
        ISourceLocation current = member;
        URIResolverRegistry reg = URIResolverRegistry.getInstance();
        while (current != null && reg.exists(current) && reg.isDirectory(current)) {
            if (reg.exists(URIUtil.getChildLocation(current, "META-INF/RASCAL.MF"))) {
                return current;
            }

            if (URIUtil.getParentLocation(current).equals(current)) {
                // we went all the way up to the root
                return reg.isDirectory(member) ? member : URIUtil.getParentLocation(member);
            }
            
            current = URIUtil.getParentLocation(current);
        }

        return current;
    }
    
    @Override
    protected void finalize() throws Throwable {
        if (driver != null) {
            driver.quit();
        }
        if (service != null) {
            service.stop();
        }
    }

    private String javaCompilerPathAsString(IList javaCompilerPath) {
        StringBuilder b = new StringBuilder();

        for (IValue elem : javaCompilerPath) {
            ISourceLocation loc = (ISourceLocation) elem;

            if (b.length() != 0) {
                b.append(File.pathSeparatorChar);
            }

            // this is the precondition
            assert loc.getScheme().equals("file");

            // this is robustness in case of experimentation in pom.xml
            if ("file".equals(loc.getScheme())) {
                b.append(Paths.get(loc.getURI()).toAbsolutePath().toString());
            }
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
            
            if (metadata.get("url") != null && driver != null) {
                try {
                    // load the page
                    driver.get(metadata.get("url"));
                    driver.manage().window().maximize();
                    
                    // wait for page to render completely
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
                    wait.until(webDriver -> "complete".equals(((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")));

                    // take the screenshot
                    WebElement body = driver.findElement(By.tagName("body"));
                    String screenshot = body.getScreenshotAs(OutputType.BASE64);

                    // store the screenshot as an output
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

    private static RemoteWebDriver getBrowser(ChromeDriverService service) {
        if (BROWSER_BINARY == null || DRIVER_BINARY == null) {
            return null;
        }

        ChromeOptions options = new ChromeOptions()
            .setHeadless(true)
            .setBinary(BROWSER_BINARY)
            .addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors","--disable-extensions","--no-sandbox","--disable-dev-shm-usage")
            .addArguments("--user-data-dir=/tmp/rascal-config/google-chrome")
            .setLogLevel(ChromeDriverLogLevel.OFF)
            ;
        
        RemoteWebDriver driver = new RemoteWebDriver(service.getUrl(), options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(60));
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
