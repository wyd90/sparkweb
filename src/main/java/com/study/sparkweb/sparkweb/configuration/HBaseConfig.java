package com.study.sparkweb.sparkweb.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

@Configuration
public class HBaseConfig {

    @Value("${hbase.zookeeper.quorum}")
    private String zookeeperQuorum;

    @Bean
    public HbaseTemplate hbaseTemplate(){
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set("hbase.zookeeper.quorum",zookeeperQuorum);
        return new HbaseTemplate(conf);
    }
}
