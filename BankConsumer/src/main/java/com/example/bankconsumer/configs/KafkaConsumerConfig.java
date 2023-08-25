package com.example.bankconsumer.configs;

import com.example.bankconsumer.models.Account;
import com.example.bankconsumer.repositories.AccountRepository;
import com.example.bankconsumer.service.ConsumerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import org.apache.kafka.clients.KafkaClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.messaging.handler.annotation.Payload;

import javax.management.StringValueExp;
import java.lang.runtime.ObjectMethods;
import java.time.Duration;
import java.util.*;

@Configuration
public class KafkaConsumerConfig {

//    Gson gson = new Gson();
//    private ConsumerService consumerService;
//

//    @Bean
//    public ObjectMapper objectMapper(){
//        return JacksonUtils.enhancedObjectMapper();
//    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory(){
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "group111");
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configProps.put(ConsumerConfig.CLIENT_ID_CONFIG,"money");


        return new DefaultKafkaConsumerFactory<>(configProps);
    }
//
//    @Bean("listenerContainerFactory")
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,String>> listenerContainerFactory(ConsumerFactory<String,String> consumerFactory){
//        var factory = new ConcurrentKafkaListenerContainerFactory<String,String>();
//        factory.setConcurrency(1);
//        factory.setConsumerFactory(consumerFactory);
//        factory.setBatchListener(true);
//        factory.getContainerProperties().setIdleBetweenPolls(1_000);
//        factory.getContainerProperties().setPollTimeout(1_000);
//
//        return factory;
//    }
//
//
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String>
//    kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }


}





