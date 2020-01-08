package com.pivotal.storm.spring.support;

import com.pivotal.storm.spring.support.SpringAwareBasicBoltProxy;
import org.apache.storm.topology.*;

public class SpringAwareTopologyBuilder extends TopologyBuilder {
    public BoltDeclarer setBolt(String id, IBasicBolt bolt, Number parallelism_hint) throws IllegalArgumentException {
        return super.setBolt(id, new SpringAwareBasicBoltProxy(bolt), (Number)parallelism_hint);
    }
}
