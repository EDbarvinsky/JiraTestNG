package com.epam.testng.utils;

import com.epam.testng.utils.constant.FileConstants;
import com.epam.testng.utils.resultconstructor.TestResultConstructor;
import com.epam.testng.utils.resultconstructor.TestResultConstructorManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class JIRAAttachment {
    private static final String ATTACHMENT_PATHNAME = "\\target\\attachments\\";
    private static final long MAX_FILE_SIZE_IN_BYTES = 40000000;
    private static final Logger logger = LogManager.getLogger(JIRAAttachment.class);
    private static TestResultConstructor testResultConstructor = TestResultConstructorManager.getInstance();

    private JIRAAttachment() {
    }

    public static void addAttachment(File file, String JIRATestKey) {
        try {
            if (isFileValid(file)) {
                String newFileName = addJIRATestKeyInFileName(file, JIRATestKey);
                File attachment = copyFileData(file, newFileName);
                testResultConstructor.collectAttachment(attachment);
            }
        } catch (IOException e) {
            logger.catching(e);
        }
    }

    private static File copyFileData(File file, String fileName) throws IOException {
        File attachment = null;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            attachment = new File(ATTACHMENT_PATHNAME + fileName);
            byte[] data = IOUtils.toByteArray(fileInputStream);
            FileUtils.writeByteArrayToFile(attachment, data);
        } catch (FileNotFoundException e) {
            logger.catching(e);
        }
        return attachment;
    }

    private static String addJIRATestKeyInFileName(File file, String JIRATestKey) {
        String fileName = FilenameUtils.getBaseName(file.getName()) + FileConstants.JIRA_KEY_PREFIX;
        String fileExtension = FileConstants.FILE_EXTENSION_SEPARATOR + FilenameUtils.getExtension(file.getName());
        return fileName + JIRATestKey + fileExtension;
    }

    private static boolean isFileValid(File file) {
        boolean validationStatus = false;
        if (file.isDirectory()) {
            logger.error("Finded directory, but not a file");
        } else if (file.length() > MAX_FILE_SIZE_IN_BYTES) {
            logger.error("Current file " + file.getName() + "size is more then 40 MB");
        } else {
            validationStatus = true;
        }
        return validationStatus;
    }
}