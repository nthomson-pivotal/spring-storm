package com.pivotal.storm.spring.support;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class SpringAwareBasicBoltProxy implements IBasicBolt {

    public static Logger logger = LoggerFactory.getLogger(SpringAwareBasicBoltProxy.class);

    private IBasicBolt target;
    private AnnotationConfigApplicationContext context;

    public SpringAwareBasicBoltProxy(IBasicBolt target) {
        this.target = target;
    }

    @Override
    public void prepare(Map<String, Object> map, TopologyContext topologyContext) {
        logger.error("Running prepare for {}", this.target.getClass().getName());

        context = new AnnotationConfigApplicationContext();
        // TODO: Could use this to set spring profile from Storm configuration
        // context.getEnvironment().setActiveProfiles( "myProfile" );
        context.scan(this.target.getClass().getPackageName());
        context.refresh();

        context.getAutowireCapableBeanFactory().autowireBean(this.target);

        this.target.prepare(map, topologyContext);
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        this.target.execute(tuple, basicOutputCollector);
    }

    @Override
    public void cleanup() {
        logger.debug("Running cleanup for {}", this.getClass().getName());

        this.target.cleanup();

        this.context.close();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        this.target.declareOutputFields(outputFieldsDeclarer);
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return this.target.getComponentConfiguration();
    }
}
