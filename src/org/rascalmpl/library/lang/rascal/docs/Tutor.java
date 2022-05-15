package org.rascalmpl.library.lang.rascal.docs;

import org.rascalmpl.help.HelpManager;
import org.rascalmpl.ideservices.BasicIDEServices;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.shell.CommandOptions;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;

public class Tutor {

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        CommandOptions cmdOpts = new CommandOptions("Tutor Server");
        cmdOpts.pathConfigOptions()
            .boolOption("help")
            .help("Print help message for this command")
            .noModuleArgument()
            .handleArgs(args);

        PathConfig pcfg = new PathConfig(cmdOpts.getCommandLocsOption("src"), cmdOpts.getCommandLocsOption("lib"),
            cmdOpts.getCommandLocOption("bin"));
        PrintWriter stderr = new PrintWriter(System.err);
        IDEServices ideServices = new BasicIDEServices(stderr);
        HelpManager hm = new HelpManager(pcfg, new PrintWriter(System.out), stderr, ideServices, false);
        hm.refreshIndex();
        ideServices.browse(new URI("http://localhost:" + hm.getPort() + "/TutorHome/index.html"));
    }
}
