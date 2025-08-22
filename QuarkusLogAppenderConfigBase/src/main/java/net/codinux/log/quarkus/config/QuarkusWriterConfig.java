package net.codinux.log.quarkus.config;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;

import java.time.Duration;
import java.util.Optional;

@ConfigGroup
public interface QuarkusWriterConfig {

    @WithDefault("250")
    int maxLogRecordsPerBatch();

    @WithDefault("25000")
    int maxBufferedLogRecords();

    @WithDefault("40")
    int sendLogRecordsPeriodMillis();

    Optional<Duration> connectTimeout();

    Optional<Duration> requestTimeout();

}
