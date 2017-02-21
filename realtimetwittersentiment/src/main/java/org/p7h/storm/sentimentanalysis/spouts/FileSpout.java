package org.p7h.storm.sentimentanalysis.spouts;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import org.p7h.storm.sentimentanalysis.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This spout reads data from a CSV file. It is only suitable for testing in local mode
 */
public class FileSpout extends BaseRichSpout {
  private static final Logger LOG = LoggerFactory.getLogger(FileSpout.class);
  private String fileName;
  private static final String COMMA_DELIMITER = ",";
  private SpoutOutputCollector _collector;
  private BufferedReader reader;
  private AtomicLong linesRead;

  /**
   * Prepare the spout. This method is called once when the topology is submitted
   * @param conf
   * @param context
   * @param collector
   */
  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    linesRead = new AtomicLong(0);
    _collector = collector;
    try {
      
      reader = new BufferedReader(new FileReader(Resources.getResource(Constants.SVCINPUTFILE_NAME).toString()));
      // read and ignore the header if one exists
    } catch (Exception e) {
    	e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deactivate() {
    try {
      reader.close();
    } catch (IOException e) {
      LOG.warn("Problem closing file");
    }
  }

  /**
   * Storm will call this method repeatedly to pull tuples from the spout
   */
  @Override
  public void nextTuple() {
    try {
      String line = reader.readLine();
      //System.out.println("Line" + line);
      
      if (line != null) {
    	String[] tokens = line.split(COMMA_DELIMITER);
    	if(tokens.length > 1){
    		long id = linesRead.incrementAndGet();
    		//System.out.println("Token : "+tokens[1]);
    		_collector.emit(new Values(tokens[1]),id);
    	}
      } else {
        System.out.println("Finished reading file, " + linesRead.get() + " lines read");
        Thread.sleep(10000);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Storm will call this method when tuples are acked
   * @param id
   */
  @Override
  public void ack(Object id) {
  }

  /**
   * Storm will call this method when tuples fail to process downstream
   * @param id
   */
  @Override
  public void fail(Object id) {
    System.err.println("Failed line number " + id);
  }

  /**
   * Tell storm which fields are emitted by the spout
   * @param declarer
   */
  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    // read csv header to get field info
    declarer.declare(new Fields("tweet"));
  }

}
