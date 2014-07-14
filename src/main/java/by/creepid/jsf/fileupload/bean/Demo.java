/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.jsf.fileupload.bean;

import by.creepid.jsf.fileupload.UploadedFile;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author rusakovich
 */
@ManagedBean(name = "demo")
public class Demo {

    private String firstName;
    private String lastName;
    private UploadedFile file1;
    private UploadedFile file2;

    boolean uploaded = false;
    boolean isNotUpload = true;

    public Demo() {
        super();
    }

    /**
     * A bean property the is excuted when the upload button is clicked
     *
     * @return
     */
    public String doUpload() {
        uploaded = true;
        isNotUpload = false;
        
        return "success";
    }

    /**
     * Accessor for file1
     *
     * @return
     */
    public UploadedFile getFile1() {
        return file1;
    }

    public void setFile1(UploadedFile file1) {
        this.file1 = file1;
    }

    /**
     * Accessor for file2
     *
     * @return
     */
    public UploadedFile getFile2() {
        return file2;
    }

    public void setFile2(UploadedFile file2) {
        this.file2 = file2;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Used to enable the panel after the upload have been done
     *
     * @return
     */
    public boolean getIsUploaded() {
        return uploaded;
    }

    /**
     * Used to disable the view of the upload component after the upload has
     * been done
     *
     * @return
     */
    public boolean getIsNotUpload() {
        return isNotUpload;
    }
}
