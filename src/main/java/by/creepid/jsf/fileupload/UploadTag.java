/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.jsf.fileupload;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;

/**
 * Specializes to allow for properties that take their values from EL API
 * expressions.
 *
 * @author rusakovich
 */
public class UploadTag extends UIComponentELTag {

    private ValueExpression value;
    private ValueExpression target;

    /**
     * Return the component type for the component that is or will be bound to
     * this tag
     *
     * @return
     */
    @Override
    public String getComponentType() {
        return "by.creepid.jsf.fileupload";
    }

    /**
     * Return the rendererType property that selects the Renderer to be used for
     * encoding this component, or null to ask the component to render itself
     * directly.
     *
     * @return
     */
    @Override
    public String getRendererType() {
        return "by.creepid.jsf.fileupload";
    }

    /**
     * Override properties and attributes of the specified component, if the
     * corresponding properties of this tag handler instance were explicitly
     * set.
     *
     * This method must be called ONLY if the specified UIComponent was in fact
     * created during the execution of this tag handler instance, and this call
     * will occur BEFORE the UIComponent is added to the view.
     *
     * Tag subclasses that want to support additional set properties must ensure
     * that the base class setProperties() method is still called. A typical
     * implementation that supports extra properties foo and bar would look
     * something like this:
     *
     * protected void setProperties(UIComponent component) {
     * super.setProperties(component); if (foo != null) {
     * component.setAttribute("foo", foo); } if (bar != null) {
     * component.setAttribute("bar", bar); } }
     *
     * The default implementation overrides the following properties:
     *
     * rendered - Set if a value for the rendered property is specified for this
     * tag handler instance. rendererType - Set if the getRendererType() method
     * returns a non-null value.
     *
     * @param component
     */
    @Override
    public void setProperties(UIComponent component) {
        super.setProperties(component);
        component.setValueExpression("target", target);
        component.setValueExpression("value", value);
    }

    /**
     * Release any resources allocated during the execution of this tag handler.
     */
    @Override
    public void release() {
        super.release();
        value = null;
        target = null;
    }

    public void setValue(ValueExpression value) {
        this.value = value;
    }

    public void setTarget(ValueExpression target) {
        this.target = target;
    }

}
