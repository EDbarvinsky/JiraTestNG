package com.epam.testng.utils.resultconstructor.mapper;

import com.epam.testng.utils.constant.AttachmentConstants;
import com.epam.testng.utils.constant.FileConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CollectionMapper {
    private static final Logger logger = LogManager.getLogger(CollectionMapper.class);
    private volatile double fileCounter;

    public Map<String, Map<String, String>> map(Map<String, Map<String, String>> map, List<File> list) {
        if (!map.isEmpty() && !list.isEmpty()) {
            for (File file : list) {
                String JIRATestKey = getJIRATestKeyFromFileName(file);
                if (map.containsKey(JIRATestKey)) {
                    String tagName = AttachmentConstants.ATTACHMENT.toString() + fileCounter;
                    fileCounter++;
                    try {
                        map.get(JIRATestKey).put(tagName, file.getCanonicalPath());
                    } catch (IOException e) {
                        logger.catching(e);
                    }
                }
            }
        }
        return map;
    }

    public Map<String, Map<String, String>> map(Map<String, Map<String, String>> map, Map<String, Throwable> throwableMap) {
        if (!throwableMap.isEmpty() && !map.isEmpty()){
            for (String JIRATestKey : throwableMap.keySet()) {
                map.get(JIRATestKey).put(AttachmentConstants.EXCEPTION.toString(), throwableMap.get(JIRATestKey).getMessage());
            }
        }
        return map;
    }

    private String getJIRATestKeyFromFileName(File file){
       return file.getName().substring(file.getName().lastIndexOf(FileConstants.JIRA_KEY_PREFIX.toString()) + 1,
                file.getName().lastIndexOf(FileConstants.FILE_EXTENSION_SEPARATOR.toString()));
    }
}
