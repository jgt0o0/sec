package com.tsinghua.sec.cache;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.*;

/**
 * Created by ji on 16-5-23.
 */
public class LogMessageCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogMessageCache.class);

    private static final List<String> messageCache = Collections.synchronizedList(new LinkedList<String>());

    private static final LogMessageCache _INSTANCE = new LogMessageCache();

    private LogMessageCache() {
    }

    public static LogMessageCache getInstance() {
        return _INSTANCE;
    }

    public List<String> getMessageList() {
        List<String> messages = new ArrayList<String>();
        synchronized (messageCache) {
            if (messageCache.size() > 0) {
                messages.addAll(messageCache);
                messageCache.clear();
            }
        }
        return messages;
    }

    public void writeMsg(String message) {
        messageCache.add(getTime() + " " + message);
    }

    public String getTime() {
        return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

}
