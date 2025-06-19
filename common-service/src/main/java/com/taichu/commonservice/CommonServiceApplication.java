package com.taichu.commonservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import com.taichu.commonservice.builder.wordCountsBuilder;
import com.taichu.commonservice.config.StreamsConfigFactory;

import java.util.Arrays;
import java.util.Properties;

@SpringBootApplication
@EnableEurekaClient
public class CommonServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommonServiceApplication.class, args);
        try {
            mainOfKafka();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public static void mainOfKafka() throws Exception {
        Properties props = StreamsConfigFactory.load();
        Topology topology = wordCountsBuilder.notificationTopicBuilder().build();
        KafkaStreams streams = new KafkaStreams(topology, props);
        try(streams) {
            streams.start();
            // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        } finally {
            streams.close();
        }
    }
}
