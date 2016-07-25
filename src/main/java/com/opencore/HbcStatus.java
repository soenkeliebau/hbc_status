package com.opencore;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class HbcStatus {
  private static Logger logger;
  public static void main(String[] args) {
    logger = LoggerFactory.getLogger(HbcStatus.class.getName());

    Properties authProps = new Properties();
    try {
      authProps.load(HbcStatus.class.getClassLoader().getResourceAsStream("auth.properties"));
    } catch (IOException e) {
      logger.error("Error loading auth information from properties file: " + e.toString());
      return;
    }
    if (!(authProps.containsKey("consumerKey") && authProps.containsKey("consumerSecret") && authProps.containsKey("token") && authProps.containsKey("secret"))) {
      logger.error("Mandatory setting missing from properties file aborting!");
      return;
    }

    try {
      // Delay to allow connecting the debugger
      logger.info("Attach debugger now..");
      TimeUnit.SECONDS.sleep(20);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }


    try {
      run(authProps.getProperty("consumerKey"), authProps.getProperty("consumerSecret"), authProps.getProperty("token"), authProps.getProperty("secret"));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void run(String consumerKey, String consumerSecret, String token, String secret) throws InterruptedException {
    // Create an appropriately sized blocking queue
    BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);

    // Define our endpoint: By default, delimited=length is set (we need this for our processor)
    // and stall warnings are on.
    StatusesSampleEndpoint endpoint = new StatusesSampleEndpoint();
    endpoint.stallWarnings(false);

    Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);
    //Authentication auth = new com.twitter.hbc.httpclient.auth.BasicAuth(username, password);

    // Create a new BasicClient. By default gzip is enabled.
    BasicClient client = new ClientBuilder()
        .name("sampleExampleClient")
        .hosts(Constants.STREAM_HOST)
        .endpoint(endpoint)
        .authentication(auth)
        .processor(new StringDelimitedProcessor(queue))
        .build();

    // Establish a connection
    client.connect();

    // Do whatever needs to be done with messages
    for (int msgRead = 0; msgRead < 1000; msgRead++) {
      if (client.isDone()) {
        logger.error("Client connection closed unexpectedly: " + client.getExitEvent().getMessage());
        break;
      }

      String msg = queue.poll(5, TimeUnit.SECONDS);
      if (msg == null) {
        logger.warn("Did not receive a message in 5 seconds");
      } else {
        //logger.info(msg);
      }
    }

    client.stop();

    // Print some stats
    System.out.printf("The client read %d messages!\n", client.getStatsTracker().getNumMessages());
  }
}
