package com.alice.emily.grpc;

import com.alice.emily.core.AbstractAnnotationBeanPostProcessor;
import com.alice.emily.core.AnnotationFilter;
import com.alice.emily.utils.LOG;
import io.grpc.stub.AbstractStub;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * Created by lianhao on 2017/2/6.
 */
public class GRpcClientAnnotationBeanPostProcessor extends AbstractAnnotationBeanPostProcessor {

    private final GRpcClientFactory clientFactory;

    public GRpcClientAnnotationBeanPostProcessor(GRpcClientFactory clientFactory) {
        super(Members.FIELDS, Phase.PRE_INIT, new AnnotationFilter(GRpcClient.class, AnnotationFilter.INJECTABLE_FIELDS));
        this.clientFactory = clientFactory;
    }

    @Override
    protected void withField(Object bean, String beanName, Class<?> targetClass, Field field) {
        GRpcClient annotation = field.getAnnotation(GRpcClient.class);
        Class<?> type = field.getType();
        if (!AbstractStub.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Field " + targetClass.getCanonicalName() + "." + field.getName() +
                    " must be a subtype of " + AbstractStub.class.getCanonicalName());
        }
        ReflectionUtils.makeAccessible(field);
        AbstractStub stub = (AbstractStub) ReflectionUtils.getField(field, bean);
        if (stub == null) {
            stub = clientFactory.buildStub(type, annotation.value());
            ReflectionUtils.setField(field, bean, stub);
            LOG.RPC.debug("Injected gRPC client {}:{} for field {}.{}", annotation.value(), type.getName(),
                    targetClass.getCanonicalName(), field.getName());
        }
    }
}
