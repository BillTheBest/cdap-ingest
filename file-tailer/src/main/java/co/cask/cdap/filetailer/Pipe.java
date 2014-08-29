
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

package co.cask.cdap.filetailer;

import co.cask.cdap.filetailer.metrics.FileTailerMetricsProcessor;
import co.cask.cdap.filetailer.sink.FileTailerSink;
import co.cask.cdap.filetailer.tailer.LogTailer;

/**
 * Flow class contain sink and tailer instances  for one flow
 */

public class Pipe {
    private LogTailer logTailer;
    private FileTailerSink sink;
    private FileTailerMetricsProcessor metricsProcessor;
    public Pipe(LogTailer tailer, FileTailerSink sink, FileTailerMetricsProcessor metricsProcessor) {

        this.logTailer = tailer;
        this.sink = sink;
        this.metricsProcessor = metricsProcessor;
    }

  /**
   * Start metrics processor, tailer and sink
   */
    public void start() {
        metricsProcessor.startWorker();
        logTailer.startWorker();
        sink.startWorker();
    }

  /**
   * Start tailer and sink
   */
  public void startWithoutMetrics() {
    logTailer.startWorker();
    sink.startWorker();
  }

  /**
   * Stop metrics processor, tailer and sink
   */
    public void stop() {
        metricsProcessor.stopWorker();
        logTailer.stopWorker();
        sink.stopWorker();
    }

  /**
   * Stop tailer and sink
   */
  public void stopWithoutMetrics() {
    logTailer.stopWorker();
    sink.stopWorker();
  }
}
