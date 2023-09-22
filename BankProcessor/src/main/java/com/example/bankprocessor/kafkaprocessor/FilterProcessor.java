package com.example.bankprocessor.kafkaprocessor;


import com.example.sampledto.SampleDto;
import com.example.stepdto.StepDto;
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
        return TopicBuilder.name("topic15").partitions(6).replicas(3).build();
    }

    @Autowired
    public void process(StreamsBuilder streamsBuilder) {

        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        KStream<String,String> textLines = streamsBuilder.stream("topic13", Consumed.with(stringSerde,stringSerde));

        //textLines2 = textLines.mapValues((key,value) -> jsonParserStep(value));
        //KTable<String, String> convertedTable = textLines2.toTable();

        KTable<String, Long> streamTable = textLines
                .peek((key,value) -> System.out.println("Key " + key))
                .peek((key,value) -> System.out.println("Value " + value))
                .groupByKey()
                .count();


        KStream<String, String> textLines2 = textLines.leftJoin(streamTable, this::jsonParserStep);
        textLines2.peek((key,value) -> System.out.println("Key " + key));
        textLines2.peek((key,value) -> System.out.println("Value " + value));

        textLines2.to("topic15",Produced.with(stringSerde,stringSerde));
        //streamTable.toStream().to("topic15",Produced.with(stringSerde,longSerde));
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

    public String jsonParserStep(String textLines,Long count) {
        try {
            StepDto stepDto = objectMapper.readValue(textLines,StepDto.class);
            stepDto.setMoneyAmount(stepDto.getMoneyAmount() + 10);
            stepDto.setStep(Math.toIntExact(count));
            return objectMapper.writeValueAsString(stepDto);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

}
