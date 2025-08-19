package com.raved.content;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers(disabledWithoutDocker = true)
class KafkaIT {

    @Container
    static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.1"));

    @Test
    void kafkaContainerRuns() {
        Assertions.assertTrue(kafka.isRunning());
    }
}

