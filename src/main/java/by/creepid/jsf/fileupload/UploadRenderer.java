/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.jsf.fileupload;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * UIInput is a UIComponent that represents a component that both displays
 * output to the user (like UIOutput components do) and processes request
 * parameters on the subsequent request that need to be decoded. There are no
 * restrictions on the data type of the local value, or the object referenced by
 * the value binding expression (if any); however, individual Renderers will
 * generally impose restrictions on the type of data they know how to display.
 *
 * During the Apply Request Values phase of the request processing lifecycle,
 * the decoded value of this component, usually but not necessarily a String,
 * must be stored - but not yet converted - using setSubmittedValue(). If the
 * component wishes to indicate that no particular value was submitted, it can
 * either do nothing, or set the submitted value to null.
 *
 * By default, during the Process Validators phase of the request processing
 * lifecycle, the submitted value will be converted to a typesafe object, and,
 * if validation succeeds, stored as a local value using setValue(). However, if
 * the immediate property is set to true, this processing will occur instead at
 * the end of the Apply Request Values phase.
 *
 * During the Render Response phase of the request processing lifecycle,
 * conversion for output occurs as for UIOutput.
 *
 * When the validate() method of this UIInput detects that a value change has
 * actually occurred, and that all validations have been successfully passed, it
 * will queue a ValueChangeEvent. Later on, the broadcast() method will ensure
 * that this event is broadcast to all interested listeners. This event will be
 * delivered by default in the Process Validators phase, but can be delivered
 * instead during Apply Request Values if the immediate property is set to true.
 * If the validation fails, the implementation must call
 * FacesContext#validationFailed.
 *
 * By default, the rendererType property must be set to "Text". This value can
 * be changed by calling the setRendererType() method.
 *
 * @author rusakovich
 */
@FacesComponent(value = "fileUpload")
public class UploadRenderer extends UIInput implements ClientBehaviorHolder {

    private int i;

    /**
     * Return the identifier of the component family to which this component
     * belongs. This identifier, in conjunction with the value of the
     * rendererType property, may be used to select the appropriate Renderer for
     * this component instance.
     *
     * @return
     */
    @Override
    public String getFamily() {
        return "fileUpload";
    }

    /**
     * If our rendered property is true, render the ending of the current state
     * of this UIComponent.
     *
     * If a Renderer is associated with this UIComponent, the actual encoding
     * will be delegated to Renderer#encodeEnd(FacesContext, UIComponent).
     *
     * @param context
     * @throws IOException
     */
    @Override
    public void encodeEnd(FacesContext context) throws IOException {

        ResponseWriter responseWriter = context.getResponseWriter();
        String clientId = getClientId(context);

        responseWriter.startElement("input", null);
        responseWriter.writeAttribute("id", clientId, "id");
        responseWriter.writeAttribute("name", clientId, "clientId");
        responseWriter.writeAttribute("type", "file", "file");

        //add a style class if the styleClass attribute exist
        UIComponent component = getCurrentComponent(context);
        String styleClass = (String) component.getAttributes().get("styleClass");
        if (styleClass != null) {
            responseWriter.writeAttribute("class", styleClass, "styleClass");
        }

        //add a style if the style attribute exist
        String style = (String) component.getAttributes().get("style");
        if (style != null) {
            responseWriter.writeAttribute("style", style, "style");
        }

        responseWriter.endElement("input");
        responseWriter.flush();
    }

    /**
     * Decode any new state of this UIComponent from the request contained in
     * the specified FacesContext, and store this state as needed.
     *
     * During decoding, events may be enqueued for later processing (by event
     * listeners who have registered an interest), by calling queueEvent().
     *
     * @param context
     */
    @Override
    public void decode(FacesContext context) {
        ExternalContext external = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) external.getRequest();

        String clientId = getClientId(context);
        FileItem item = (FileItem) request.getAttribute(clientId);

        ValueExpression valueExpr = getValueExpression("value");
        if (valueExpr != null) {

            UIComponent component = getCurrentComponent(context);
            if (item.getSize() > 0) {
                String target = getAttributes().get("target").toString();

                String realPath = getServletRealPath(external, target);
                String fileName = item.getName();

                String longFileName = getLongFileName(realPath, fileName);
                String shortFileName = target + "/" + fileName;
                //if the target location ends with the forward slash,
                //just join the target and the filename
                if (target.endsWith("/")) {
                    shortFileName = target + fileName;
                }

                ((EditableValueHolder) component).setSubmittedValue(new UploadedFile(item, longFileName, shortFileName));
            } else {
                ((EditableValueHolder) component).setSubmittedValue(new UploadedFile());
            }
            ((EditableValueHolder) component).setValid(true);
        }

        Object target = getAttributes().get("target");
        if (target != null) {

            String realPath = getServletRealPath(external, target.toString());
            File file = new File(getLongFileName(realPath, item.getName()));

            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }

            try {
                if (item.getSize() > 0 && item.getName().length() > 0) {
                    item.write(file);
                }
            } catch (Exception ex) {
                throw new FacesException(ex);
            }
        }
    }

    private String getLongFileName(String path, String fileName) {
        return path + fileName;
    }

    @Override
    public Collection<String> getEventNames() {
        return null;
    }

    @Override
    public String getDefaultEventName() {
        return "";
    }

    @Override
    public void addClientBehavior(String s, ClientBehavior c) {

    }

    @Override
    public Map<String, List<ClientBehavior>> getClientBehaviors() {
        return null;
    }

    private String getServletRealPath(ExternalContext external, String target) {
        ServletContext servletContext = (ServletContext) external.getContext();

        String realPath = servletContext.getRealPath(target.toString());
        if (!realPath.endsWith("/")) {
            realPath += "/";
        }

        return realPath;
    }
}
