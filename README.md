# LogAppenderBase
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.codinux.log/log-appender-base/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.codinux.log/log-appender-base)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Basic log functionalities like a async log queue that pushes log records to a remote log store written in Kotlin Multiplatform. 

Provides these base functionalities for Logback, Log4j 2, JBoss Logging, Java Util Log (JUL) and Quarkus Logging.

Used e.g. for [LokiLogAppender](https://github.com/codinux-gmbh/LokiLogAppender) and in the future may also for [ElasticsearchLogger](https://github.com/codinux-gmbh/ElasticsearchLogger).


## Setup

### Gradle

#### Base

```
implementation("net.codinux.log:log-appender-base:0.8.0")
```

#### Specific Log framework, e.g. Logback

```
implementation("net.codinux.log:logback-appender-base:0.8.0")
```

### Maven

```xml
<dependency>
   <groupId>net.codinux.log</groupId>
   <artifactId>log-appender-base-jvm</artifactId>
   <version>0.8.0</version>
</dependency>
```



# License

    Copyright 2023 codinux GmbH & Co. KG

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.