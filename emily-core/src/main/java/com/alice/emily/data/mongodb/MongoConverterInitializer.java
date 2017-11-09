package com.alice.emily.data.mongodb;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by lianhao on 2017/7/17.
 */
public class MongoConverterInitializer implements InitializingBean {

    @Autowired(required = false)
    private List<MappingMongoConverter> mappingMongoConverters;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (CollectionUtils.isEmpty(mappingMongoConverters)) {
            return;
        }
        for (MappingMongoConverter converter : mappingMongoConverters) {
            DefaultMongoTypeMapper mapper = new DefaultMongoTypeMapper(null, converter.getMappingContext());
            converter.setTypeMapper(mapper);
        }
    }

}
