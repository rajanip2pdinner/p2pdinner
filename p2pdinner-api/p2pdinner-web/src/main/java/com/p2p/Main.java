package com.p2p;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * This class launches the web application in an embedded Jetty container.
 * This is the entry point to your application. The Java command that is used for
 * launching should fire this main method.
 *
 */
public class Main {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception{
    	
    	try {
    		String webappDirLocation = System.getenv("WEBAPP_DIR");

            // The port that we should run on can be set into an environment variable
            // Look for that variable and default to 8080 if it isn't there.
            String webPort = System.getenv("PORT");
            
            if (webPort == null || webPort.isEmpty()) {
                webPort = "9128";
            }
            
            if (webappDirLocation == null || webappDirLocation.isEmpty()) {
            	webappDirLocation = "src/main/webapp/";
            }

            Server server = new Server(Integer.valueOf(webPort));
            WebAppContext root = new WebAppContext();

            root.setContextPath("/");
            root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
            root.setResourceBase(webappDirLocation);
            
            LOGGER.info(" **************************************************************** ");
            LOGGER.info(" PORT : {}", webPort);
            LOGGER.info(" WebAppDirLocation : {}", webappDirLocation);
            LOGGER.info(" Root Context Path : /");
            LOGGER.info(" Web Descriptor : {}", webappDirLocation + "/WEB-INF/web.xml");
            LOGGER.info(" **************************************************************** ");

            // Parent loader priority is a class loader setting that Jetty accepts.
            // By default Jetty will behave like most web containers in that it will
            // allow your application to replace non-server libraries that are part of the
            // container. Setting parent loader priority to true changes this behavior.
            // Read more here: http://wiki.eclipse.org/Jetty/Reference/Jetty_Classloading
            root.setParentLoaderPriority(true);

            server.setHandler(root);

            server.start();
            server.join();
    	} catch (Throwable t) {
    		t.printStackTrace();
    	}
        
    }

}
