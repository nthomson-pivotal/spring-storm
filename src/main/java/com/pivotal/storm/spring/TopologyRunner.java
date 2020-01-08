package com.pivotal.storm.spring;

import com.pivotal.storm.spring.bolt.AggregatingBolt;
import com.pivotal.storm.spring.bolt.FileWritingBolt;
import com.pivotal.storm.spring.bolt.FilteringBolt;
import com.pivotal.storm.spring.spout.RandomNumberSpout;
import com.pivotal.storm.spring.support.SpringAwareTopologyBuilder;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseWindowedBolt;

public class TopologyRunner {
    public static void main(String[] args) throws Exception {
        runTopology();
    }

    public static void runTopology() throws Exception {
        String filePath = "./src/main/resources/operations.txt";
        TopologyBuilder builder = new SpringAwareTopologyBuilder();
        builder.setSpout("randomNumberSpout", new RandomNumberSpout());
        builder.setBolt("filteringBolt", new FilteringBolt()).shuffleGrouping("randomNumberSpout");
        builder.setBolt("aggregatingBolt", new AggregatingBolt()
                .withTimestampField("timestamp")
                .withLag(BaseWindowedBolt.Duration.seconds(1))
                .withWindow(BaseWindowedBolt.Duration.seconds(5))).shuffleGrouping("filteringBolt");
        builder.setBolt("fileBolt", new FileWritingBolt(filePath)).shuffleGrouping("aggregatingBolt");

        Config config = new Config();
        config.setDebug(false);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("Test", config, builder.createTopology());
    }
}