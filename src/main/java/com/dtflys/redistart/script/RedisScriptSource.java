package com.dtflys.redistart.script;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Redis脚本源文件
 * @author gongjun[jun.gong@thebeastshop.com]
 * @since 2018-06-21 17:47
 */
public class RedisScriptSource {

    private String name;
    private URL url;
    private String filename;
    private String folder;
    private InputStream inputStream;
    private boolean hasRead = false;
    private String source;

    public RedisScriptSource(Resource resource) throws IOException {
        this(resource.getURL());
    }


    public RedisScriptSource(URL url) throws IOException {
        this.url = url;
        this.inputStream = url.openStream();
        this.filename = url.getFile();
        this.name = getScriptName(filename);
        this.folder = getFolderName(filename);
    }


    /**
     * 读取脚本源文件
     * @throws IOException
     */
    public void read() throws IOException {
        if (hasRead) {
            return;
        }
        try {
            this.source = IOUtils.toString(inputStream, "UTF-8");
            this.hasRead = true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }


    private String getScriptName(String filename) {
        String[] segments = filename.split("/|\\\\");
        return segments[segments.length - 1];
    }

    private String getFolderName(String filename) {
        String[] parts = filename.split("!");
        if (parts.length == 2) {
            filename = parts[1];
        }
        String[] segments = filename.split("/|\\\\");
        String[] folderSeg = ArrayUtils.subarray(segments, 0, segments.length - 1);
//        String os = System.getProperty("os.name");
        String folderName = StringUtils.join(folderSeg, '/');
//        if(os.toLowerCase().startsWith("win")){
//            return folderName.substring(1);
//        }
        return folderName;
    }

    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }

    public String getFilename() {
        return filename;
    }

    public String getFolder() {
        return folder;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public String getSource() {
        if (!hasRead) {
            throw new RuntimeException("This Redis script source [" + this.name + "] has not been read!");
        }
        return source;
    }
}
