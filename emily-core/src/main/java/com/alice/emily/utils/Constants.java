package com.alice.emily.utils;

import org.apache.commons.lang3.SystemUtils;

import java.io.File;

/**
 * Created by lianhao on 2016/12/30.
 */
public class Constants {

    public static final String WORK_DIR = SystemUtils.getUserHome() + File.separator + ".emily";

    public static final String JTA_WORK_DIR = WORK_DIR + File.separator + "jta";

    public static final String INFINISPAN_WORK_DIR = WORK_DIR + File.separator + "infinispan";

}
