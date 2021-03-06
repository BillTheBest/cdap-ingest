/*
 * Copyright © 2014 Cask Data, Inc.
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

package co.cask.cdap.filetailer.state;

import co.cask.cdap.filetailer.state.exception.FileTailerStateProcessorException;

/**
 * Stores and retrieves the state of the File Tailer.
 */
public interface FileTailerStateProcessor {

  /**
   * Saves the state of the File Tailer.
   * Used to save current state of File Tailer for its future usage.
   *
   * @param state the state to save
   */
  void saveState(FileTailerState state) throws FileTailerStateProcessorException;

  /**
   * Retrieves the last saved state of the File Tailer.
   * Used for restore File Tailer state after stop or restart.
   *
   * @return last saved state of File Tailer
   */
  FileTailerState loadState() throws FileTailerStateProcessorException;
}
