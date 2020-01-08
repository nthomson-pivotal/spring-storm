package com.pivotal.storm.spring.bolt;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class FilteringBolt extends BaseBasicBolt {

    public static Logger logger = LoggerFactory.getLogger(FilteringBolt.class);

    @Autowired
    private TestService testService;

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        this.testService.test();

        int operation = tuple.getIntegerByField("operation");
        if(operation > 0 ) {
            basicOutputCollector.emit(tuple.getValues());
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("operation", "timestamp"));
    }
}

