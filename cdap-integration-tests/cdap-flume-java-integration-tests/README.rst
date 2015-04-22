==================================================================
Cask Data Application Platform (CDAP) Flume Sink Integration Tests
==================================================================

This project contains integration tests for CDAP Flume Sink.

Usage
=====

Please review the general `CDAP ingest documentation <http://docs.cask.co/cdap/current/>`__
for usage instructions. This document contains information about project-specific
configuration files.

Configuration
=============

To configure integration tests against a Standalone CDAP instance, edit::

  src/test/resources/local.conf 


To configure integration tests against a Standalone CDAP instance with authentication
enabled, edit::

  src/test/resources/local_auth.conf 


To configure integration tests against a Standalone CDAP instance with authentication
enabled and ssl turned on, edit::

  src/test/resources/local_auth_ssl.conf 


To configure integration tests against a Distributed CDAP instance, edit::

  src/test/resources/remote.conf 


To configure integration tests against a Distributed CDAP instance with authentication
enabled, edit::

  src/test/resources/remote_auth.conf 


To configure integration tests against a Distributed CDAP instance with authentication
enabled and ssl turned on, edit::

  src/test/resources/remote_auth_ssl.conf 

Please refer to *CDAP Flume Sink* in the `CDAP documentation
<http://docs.cask.co/cdap/current/>`__ for additional information.


License and Trademarks
----------------------
Copyright © 2014-2015 Cask Data, Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the 
License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
either express or implied. See the License for the specific language governing permissions 
and limitations under the License.

Cask is a trademark of Cask Data, Inc. All rights reserved.

Apache, Apache HBase, and HBase are trademarks of The Apache Software Foundation. Used with
permission. No endorsement by The Apache Software Foundation is implied by the use of these marks.
