package com.zhangyu.raft.util;

import com.zhangyu.raft.config.Configuration;
import com.zhangyu.raft.config.ConfigurationHelper;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ConfigurationHelperTest {
    @Test
    public void saveTest() throws IOException {
        ConfigurationHelper.save(new Configuration());
        Assert.assertTrue(new File(ConfigurationHelper.DEFAULT_PATH).exists());
    }

    @Test
    public void loadTest() throws IOException {
//        Assert.assertNotNull(ConfigurationHelper.load());
        Configuration configuration = ConfigurationHelper.load();
//        System.out.println("test");
        System.out.println(configuration);
    }
}
