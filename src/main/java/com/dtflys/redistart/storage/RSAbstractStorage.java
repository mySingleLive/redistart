package com.dtflys.redistart.storage;


import java.io.*;

public abstract class RSAbstractStorage {

    private final static String APP_STORAGE_PATH = ".redistart";


    protected static String getStorageDirPath(String dirPath) {
        return System.getProperty("user.home") + "/" + APP_STORAGE_PATH + "/" + dirPath;
    }

    public String loadStorageAsString(String dirPath, String fileName) {
        String path = getStorageDirPath(dirPath);
        String filePath = path + "/" + fileName;
        File storageDir = new File(path);
        File storageFile = new File(filePath);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        if (storageFile.exists()) {
            try {
                FileReader reader = new FileReader(storageFile);
                StringBuilder builder = new StringBuilder();
                int ch = 0;
                while ((ch = reader.read()) != -1) {
                    builder.append((char) ch);
                }
                return builder.toString();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    public void save(String dirPath, String fileName, String value) {
        String path = getStorageDirPath(dirPath);
        String filePath = path + "/" + fileName;
        File storageDir = new File(path);
        File storageFile = new File(filePath);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        if (!storageFile.exists()) {
            try {
                storageFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (FileWriter writer = new FileWriter(storageFile)) {
            writer.write(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
