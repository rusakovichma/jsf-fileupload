/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.jsf.fileupload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import org.apache.commons.fileupload.FileItem;

/**
 *
 * @author rusakovich
 */
public class UploadedFile implements Serializable {

    private static final long serialVersionUID = 3932877942216518622L;
    private String fileName;
    private String contentType;
    private long fileSize;
    private String longFileName;
    private String fileContent;
    private InputStream inputStream = null;
    private String shortFileName;

    public UploadedFile() {
    }

    public UploadedFile(FileItem fileItem, String longFileName, String shortFileName) {
        fileName = fileItem.getName();
        contentType = fileItem.getContentType();
        fileSize = fileItem.getSize();
        fileContent = fileItem.getString();
        this.shortFileName = shortFileName;
        this.longFileName = longFileName;

        try {
            inputStream = fileItem.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the file
     *
     * @return
     */
    public boolean delete() {
        File file = new File(longFileName);
        return file.delete();
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * @return Returns the orginal name of the uploaded file
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns the content type for the uploaded file
     *
     * @return
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Returns the size of the file in bytes
     *
     * @return
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * Returns the abosulute path of the uploaded file
     *
     * @return
     */
    public String getLongFileName() {
        return longFileName;
    }

    /**
     * Returns the contents of the file in string form
     *
     * @return
     */
    public String getFileContent() {
        return fileContent;
    }

    /**
     * Returns the relative path of the uploaded file
     *
     * @return
     */
    public String getShortFileName() {
        return shortFileName;
    }
}
