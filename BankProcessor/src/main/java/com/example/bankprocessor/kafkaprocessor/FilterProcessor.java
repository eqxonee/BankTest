package com.example.bankprocessor.kafkaprocessor;


import com.example.sampledto.SampleDto;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;


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
    public void process(StreamsBuilder streamsBuilder) {

        final Serde<String> stringSerde = Serdes.String();

        KStream<String,String> textLines = streamsBuilder.stream("topic11", Consumed.with(stringSerde,stringSerde));
        KStream<String,String> textLines2;

        textLines2 = textLines.mapValues((key,value) -> jsonParser(value));
        textLines2.peek((key,value) -> System.out.println("Key " + key));
        textLines2.peek((key,value) -> System.out.println("Value " + value));

        textLines2.to("topic13",Produced.with(stringSerde,stringSerde));
    }

    public String jsonParser(String textLines) {
            try {
                SampleDto sampleDto = objectMapper.readValue(textLines,SampleDto.class);
                sampleDto.setMoneyAmount(sampleDto.getMoneyAmount() + 10);
                return objectMapper.writeValueAsString(sampleDto);
            }catch (Exception e){
                throw new RuntimeException();
            }

    }

}
