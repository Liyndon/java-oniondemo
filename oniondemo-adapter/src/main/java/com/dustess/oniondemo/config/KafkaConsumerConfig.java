package com.dustess.oniondemo.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConsumerConfig {

}
