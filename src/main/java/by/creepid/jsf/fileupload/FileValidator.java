/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.jsf.fileupload;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author rusakovich
 */
@FacesValidator(value = "fileValidator")
public class FileValidator implements Validator {
    /*
     * If you want to change the maximum size of the file to be uploaded, you can change the value for MAX_FILE_SIZE 
     */

    private static final long MAX_FILE_SIZE = 10485760L; // 10MB.
    /*
     *you can change the extensions of the files to be uploaded 
     */
    private static final String[] VALID_EXTENSIONS = {"jpg", "jpeg", "gif", "bmp", "png"}; //set valid formats

    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {

        try {
            boolean valid = false;

            if (value != null) {
                UploadedFile uploadedFile = (UploadedFile) value;

                if (uploadedFile != null) {
                    String extension = uploadedFile.getFileName().substring(
                            uploadedFile.getFileName().lastIndexOf(".") + 1).toLowerCase();

                    for (String ext : VALID_EXTENSIONS) {
                        if (ext.equals(extension)) {
                            valid = true;
                            break;
                        }
                    }
                    //check if the extension has been set to valid, if not throw an exception
                    if (!valid) {
                        uploadedFile.delete();
                        throw new ValidatorException(new FacesMessage(String.format(
                                "Invalid file format, Reqired formats are of " + getValidExtensions())));
                    }
                    //check the files size
                    if (uploadedFile.getFileSize() > MAX_FILE_SIZE) {
                        uploadedFile.delete();
                        throw new ValidatorException(new FacesMessage(String.format(
                                "File exceeds maximum permitted size of %d Kilo Bytes.", MAX_FILE_SIZE / 1024)));
                    }
                }
            }
        } catch (Exception e) {
            //The exception will be caused by a NullExceptionPointer when no file is uploaded
            e.printStackTrace();
        }

    }

    private String getValidExtensions() {
        String strExtensions = "";
        
        for (String ext : VALID_EXTENSIONS) {
            strExtensions += ext + " ,";
        }
        
        return strExtensions.substring(0, strExtensions.lastIndexOf(" ,"));
    }

}
