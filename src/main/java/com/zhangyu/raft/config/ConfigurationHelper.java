package com.zhangyu.raft.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ConfigurationHelper {
    public static final Logger LOG = LoggerFactory.getLogger(ConfigurationHelper.class);
    public static final String DEFAULT_PATH = "config.json";
    private static final String BASE_PATH = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/src/main/resources/";

    public static Configuration load() throws IOException {
        return load(DEFAULT_PATH);
    }

    public static Configuration load(String path) throws IOException {
        InputStream inputStream = ConfigurationHelper.class.getClassLoader().getResourceAsStream(path);
        if (inputStream != null) {
            try {
                System.out.println(BASE_PATH);
                inputStream = new FileInputStream(new File(BASE_PATH, path));
                LOG.debug("found '{}' in user.dir", path);
            } catch (FileNotFoundException e) {
                LOG.error("file not found: {}", e.getMessage());
                throw new FileNotFoundException();
            }
        } else {
            LOG.debug("found '{}' in classpath", path);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        return mapper.readerFor(Configuration.class).readValue(inputStream);
    }

    public static void save(Configuration configuration) throws IOException {
        save(configuration, DEFAULT_PATH);
    }

    public static void save(Configuration configuration, String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        mapper.writeValue(new File(DEFAULT_PATH), configuration);
    }

    public static boolean validate(Configuration configuration) {
        LOG.error("validation not passed");
        return false;
    }
}
