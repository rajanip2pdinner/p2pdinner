package com.p2p.messaging;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.p2p.messaging.config.P2PMessagingClientContext;
import com.p2p.messaging.config.P2PMessagingServerContext;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		CommandLineParser parser = new BasicParser();
		ApplicationContext context = null;
		try {
			CommandLine line = parser.parse(createOptions(), args);
			if (line.hasOption("s")) {
				LOGGER.info("Starting in server mode....");
				context = new AnnotationConfigApplicationContext(P2PMessagingServerContext.class);
			} else if (line.hasOption("c")) {
				LOGGER.info("Starting in client mode....");
				context = new AnnotationConfigApplicationContext(P2PMessagingClientContext.class);
			} else if (line.hasOption("h")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("java -cp target/classes:target/dependency/* com.p2p.messaging.Main", createOptions());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static Options createOptions() {
		Options options = new Options();
		options.addOption("c", "client", false, "Start application in consumer mode");
		options.addOption("s", "server", false, "Start application in server mode");
		options.addOption("h", "help", false, "Print usage information");
		return options;
	}
}
