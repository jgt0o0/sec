package com.tsinghua.sec.util;

import com.tsinghua.sec.cache.SolderCache;
import com.tsinghua.sec.domain.Solder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Set;

/**
 * Created by ji on 16-5-26.
 */
public class PropertiesUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);

    private static final PropertiesUtil _INSTANCE = new PropertiesUtil();

    private PropertiesUtil() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("Solders.properties");
            properties.load(new InputStreamReader(inputStream, "utf-8"));

            Set<String> propertyNames = properties.stringPropertyNames();
            if (propertyNames != null && propertyNames.size() > 0) {
                for (String name : propertyNames) {
                    String rank = properties.getProperty(name);
                    Solder solder = new Solder();
                    solder.setName(name);
                    solder.setRank(Integer.parseInt(rank));
                    SolderCache.getInstance().addSolder(name, solder);
                    LOGGER.warn("name:{},rank:{}", name, rank);
                }
                SolderCache.getInstance().initAuth();
            }
        } catch (Exception e) {
            LOGGER.error("读取配置异常", e);
        }
    }

    public static PropertiesUtil getInstance() {
        return _INSTANCE;
    }
}
