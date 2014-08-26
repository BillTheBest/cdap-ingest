
/*
 * Copyright 2014 Cask, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package co.cask.cdap.filetailer.tailer;

import co.cask.cdap.filetailer.config.FlowConfiguration;
import co.cask.cdap.filetailer.config.exception.ConfigurationLoadingException;
import co.cask.cdap.filetailer.metrics.FileTailerMetricsProcessor;
import co.cask.cdap.filetailer.queue.FileTailerQueue;
import co.cask.cdap.filetailer.state.FileTailerStateProcessor;
import co.cask.cdap.filetailer.state.FileTailerStateProcessorImpl;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class TailerTest {
  private static final int LINE_SIZE = 20;
  private static final int ENTRY_NUMBER = 121;
  private static final String LOG_FILE_SIZE = "1KB";
  private static final int QUEUE_SIZE = 200;
  private static final int WRITING_INTERVAL = 1000;

  @Before
  public void prepare() throws IOException {
    TailerLogUtils.createTestDirIfNeed();
    TailerLogUtils.clearTestDir();
  }
@After
public void clean() throws IOException {
    TailerLogUtils.deleteTestDir();
  }

  @Test
  public void baseReadingLogDirTest() throws ConfigurationLoadingException, InterruptedException {
    FileTailerQueue queue = new FileTailerQueue(1);
    FlowConfiguration flowConfig = TailerLogUtils.loadConfig();

    String filePath = flowConfig.getSourceConfiguration().getWorkDir()+"/"
      +flowConfig.getSourceConfiguration().getFileName();

    FileTailerStateProcessor stateProcessor =
      new FileTailerStateProcessorImpl(flowConfig.getDaemonDir(), flowConfig.getStateFile());
    FileTailerMetricsProcessor metricsProcessor =
        new FileTailerMetricsProcessor(flowConfig.getDaemonDir(), flowConfig.getStatisticsFile(),
                                       flowConfig.getStatisticsSleepInterval(), flowConfig.getFlowName(),
                                       flowConfig.getSourceConfiguration().getFileName());

    LogTailer tailer = new LogTailer(TailerLogUtils.loadConfig(), queue,stateProcessor, metricsProcessor);
    Logger logger =  getSizeLogger(filePath, LOG_FILE_SIZE);
    RandomStringUtils randomUtils = new RandomStringUtils();
    List<String> logList = new ArrayList<String>(ENTRY_NUMBER);

    for (int i = 0; i < ENTRY_NUMBER; i++) {
      String currLine = randomUtils.randomAlphanumeric(LINE_SIZE);
    logger.debug(currLine);
    logList.add(currLine);
    }
    tailer.startWorker();
    Thread.sleep(3000);
    for (String str:logList)  {
    Assert.assertEquals(true, queue.take().getEventData().contains(str));
    }
    tailer.stopWorker();
  }


  @Test
  public void fileTimeRotationTest() throws ConfigurationLoadingException, InterruptedException {

    FileTailerQueue queue = new FileTailerQueue(QUEUE_SIZE);
    FlowConfiguration flowConfig = TailerLogUtils.loadConfig();

    String filePath = flowConfig.getSourceConfiguration().getWorkDir()+"/"
      +flowConfig.getSourceConfiguration().getFileName();
    FileTailerStateProcessor stateProcessor =
      new FileTailerStateProcessorImpl(flowConfig.getDaemonDir(), flowConfig.getStateFile());

    FileTailerMetricsProcessor metricsProcessor =
      new FileTailerMetricsProcessor(flowConfig.getDaemonDir(), flowConfig.getStatisticsFile(),
                                     flowConfig.getStatisticsSleepInterval(), flowConfig.getFlowName(),
                                     flowConfig.getSourceConfiguration().getFileName());

    LogTailer tailer = new LogTailer(TailerLogUtils.loadConfig(),queue,stateProcessor, metricsProcessor);
    Logger logger =  getTimeLogger(filePath);
    RandomStringUtils randomUtils = new RandomStringUtils();
    List<String> logList = new ArrayList<String>(ENTRY_NUMBER);
    List<String> queueReturnList = new ArrayList<String>(ENTRY_NUMBER);
      tailer.startWorker();
    for (int i = 0; i < ENTRY_NUMBER; i++) {
      String currLine = randomUtils.randomAlphanumeric(LINE_SIZE);
         logger.debug(currLine);
      logList.add(currLine);

         Thread.sleep(WRITING_INTERVAL);
    }
    Thread.sleep(2000);
    for (String str:logList) {
      Assert.assertEquals(true, queue.take().getEventData().contains(str));
    }

    tailer.stopWorker();
  }



  private  Logger getSizeLogger(String file, String fileSize) {

    // creates pattern layout
    PatternLayout layout = new PatternLayout();
    String conversionPattern = "[%d  %-5p %c{1}]  %m%n";
    layout.setConversionPattern(conversionPattern);

    // creates size rolling file appender
    RollingFileAppender rollingAppender = new RollingFileAppender();
    rollingAppender.setFile(file);
    rollingAppender.setMaxFileSize(fileSize);
    rollingAppender.setMaxBackupIndex(20);
    rollingAppender.setLayout(layout);
    rollingAppender.activateOptions();
    rollingAppender.setAppend(true);

    // configures  logger
    Logger rootLogger = Logger.getLogger(TailerTest.class);
    rootLogger.setLevel(Level.DEBUG);
    rootLogger.addAppender(rollingAppender);
    return  rootLogger;

  }

  private  Logger getTimeLogger(String file) {
    // creates pattern layout
    PatternLayout layout = new PatternLayout();
    String conversionPattern = "[%d  %-5p %c{1}]  %m%n";
    layout.setConversionPattern(conversionPattern);

    // creates daily rolling file appender
    DailyRollingFileAppender rollingAppender = new DailyRollingFileAppender();
    rollingAppender.setFile(file);
    rollingAppender.setDatePattern("'.'yyyy-MM-dd-HH-mm");
    rollingAppender.setLayout(layout);
    rollingAppender.activateOptions();
    rollingAppender.setAppend(true);


    // configures the root logger
    Logger rootLogger = Logger.getLogger(TailerTest.class);
    rootLogger.setLevel(Level.DEBUG);
    rootLogger.addAppender(rollingAppender);
    return  rootLogger;

  }

}