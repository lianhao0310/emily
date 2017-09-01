package com.alice.emily.example.zk.scene;

import java.util.Map;

/**
 * Created by zk_chs on 16/10/24.
 */
public interface ZkSceneProvider {

    void onCreate(Map scene);

    void onUpdate(Map scene);

    void onDelete(Long id);

    String onGet(Long id);
}
