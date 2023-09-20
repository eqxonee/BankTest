package com.example.bankprocessor.kafkaprocessor;


import com.example.sampledto.SampleDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

@Component
@EnableKafkaStreams
@AllArgsConstructor
public class FilterProcessor {

    private ObjectMapper objectMapper;

        @Bean
    NewTopic counts() {
        return TopicBuilder.name("topic13").partitions(6).replicas(3).build();
    }

    @Autowired
    public void process(StreamsBuilder streamsBuilder) throws JsonProcessingException {

        final Serde<String> stringSerde = Serdes.String();
        //final Serde<Long> longSerde = Serdes.Long();

        KStream<String,String> textLines = streamsBuilder.stream("topic11", Consumed.with(stringSerde,stringSerde));

        SampleDto sampleDto = objectMapper.readValue(textLines.toString(),SampleDto.class);
        sampleDto.setMoneyAmount(sampleDto.getMoneyAmount() - 10);

        String orderAsMessage = objectMapper.writeValueAsString(sampleDto);

//        KTable<String, Long> values = textLines
//                .flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
//                .groupBy((key,value) -> value, Grouped.with(stringSerde,stringSerde))
//                .count();
//
//        values.toStream().to("topic13", Produced.with(stringSerde,longSerde));
        textLines.mapValues(v -> orderAsMessage).to("topic13",Produced.with(stringSerde,stringSerde));
    }

}
