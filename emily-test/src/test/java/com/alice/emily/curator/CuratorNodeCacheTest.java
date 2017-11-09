package com.alice.emily.curator;

import com.google.common.util.concurrent.Uninterruptibles;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.equalTo;

/**
 * Created by lianhao on 2017/6/14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, properties = {
        "emily.curator.connect-string=localhost:2181",
        "emily.zookeeper.embedded.enabled=true",
        "curator.test.path.node-cache=/data/scene" })
public class CuratorNodeCacheTest {

    @Rule
    public OutputCapture capture = new OutputCapture();

    @Autowired
    private CuratorOperations operations;

    @Before
    public void setUp() {
        capture.reset();
    }

    @Test
    public void testNodeCache() {

        operations.createNode(CacheTestListener.SCENE_PATH, "100");
        expectOutput("****** [/data/scene]: 100");

        operations.setNodeData(CacheTestListener.SCENE_PATH, "200");
        expectOutput("****** [/data/scene]: 200");

        operations.setNodeData(CacheTestListener.SCENE_PATH, "400");
        expectOutput("****** [/data/scene]: 400");

        operations.setNodeData(CacheTestListener.SCENE_PATH, "800");
        expectOutput("****** [/data/scene]: 800");

    }

    @Test
    public void testPathChildrenCache() {

        operations.createNode(CacheTestListener.MAP_PATH + "/1", "Created");
        expectOutput("****** [/data/map/1]: CHILD_ADDED Created");

        operations.setNodeData(CacheTestListener.MAP_PATH + "/1", "Updated");
        expectOutput("****** [/data/map/1]: CHILD_UPDATED Updated");

        operations.deleteNode(CacheTestListener.MAP_PATH + "/1");
        expectOutput("****** [/data/map/1]: CHILD_REMOVED Updated");

    }

    @Test
    public void testTreeCache() {

        operations.setNodeData(CacheTestListener.POI_PATH, "1000");
        expectOutput("****** [/data/poi]: NODE_UPDATED 1000");

        operations.createNode(CacheTestListener.POI_PATH + "/1", "10001");
        expectOutput("****** [/data/poi/1]: NODE_ADDED 10001");

        operations.setNodeData(CacheTestListener.POI_PATH + "/1", "10002");
        expectOutput("****** [/data/poi/1]: NODE_UPDATED 10002");

        operations.createNode(CacheTestListener.POI_PATH + "/1/2", "100001");
        expectOutput("****** [/data/poi/1/2]: NODE_ADDED 100001");

        operations.setNodeData(CacheTestListener.POI_PATH + "/1/2", "100002");
        expectOutput("****** [/data/poi/1/2]: NODE_UPDATED 100002");

        operations.deleteNode(CacheTestListener.POI_PATH + "/1/2");
        expectOutput("****** [/data/poi/1/2]: NODE_REMOVED null");

        operations.deleteNode(CacheTestListener.POI_PATH + "/1");
        expectOutput("****** [/data/poi/1]: NODE_REMOVED null");

    }

    private void expectOutput(String output) {
        Uninterruptibles.sleepUninterruptibly(500, TimeUnit.MILLISECONDS);
        Assert.assertThat(capture.toString(), equalTo(output + System.lineSeparator()));
        capture.reset();
    }

}
