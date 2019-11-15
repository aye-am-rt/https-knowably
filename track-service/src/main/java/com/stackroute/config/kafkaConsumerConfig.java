package com.stackroute.config;

import com.stackroute.domain.Track;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class kafkaConsumerConfig {
    @Bean
    public ConsumerFactory<String,String> consumerFactory(){
        Map<String,Object> config=new HashMap<>();
        config.putIfAbsent(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"kafka:9092");
        config.putIfAbsent(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        config.putIfAbsent(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.putIfAbsent(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,String > kafkaListenerContainerFactory()
    {
        ConcurrentKafkaListenerContainerFactory<String,String> factory1=new ConcurrentKafkaListenerContainerFactory<>();
        factory1.setConsumerFactory(consumerFactory());
        return factory1;
    }
    /** now we can listen to multiple topics using same consumer and for consuming different-2 objects
     like string and json we have to make another bean of kafka consumer factory **/
    @Bean
    public ConsumerFactory<String,Track> trackConsumerFactory(){
        Map<String,Object> config=new HashMap<>();
        config.putIfAbsent(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"kafka:9092");
        config.putIfAbsent(ConsumerConfig.GROUP_ID_CONFIG, "group_json");
        config.putIfAbsent(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.putIfAbsent(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
                new JsonDeserializer<>(Track.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,Track> track_kafkaListenerContainerFactory()
    {
        ConcurrentKafkaListenerContainerFactory<String,Track> factory2=new ConcurrentKafkaListenerContainerFactory<>();
        factory2.setConsumerFactory(trackConsumerFactory());
        return factory2;
    }
    /**and now go to service and create one more listener for json listener for factory 2 bean**/

}
