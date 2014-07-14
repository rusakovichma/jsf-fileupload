/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.jsf.fileupload;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.*;
import java.io.File;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rusakovich
 */
public class UploadFilter implements Filter {

    private String repositoryPath;

    @Override
    public void init(FilterConfig config) throws ServletException {
        setRepositoryPath(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        if ((request instanceof HttpServletRequest)) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            if (ServletFileUpload.isMultipartContent(httpRequest)) {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setRepository(new File(repositoryPath));

                ServletFileUpload upload = new ServletFileUpload(factory);

                try {

                    List<FileItem> items = (List<FileItem>) upload.parseRequest(httpRequest);
                    final Map<String, String[]> map = new HashMap<String, String[]>();

                    for (FileItem item : items) {
                        if (item.isFormField()) {
                            processFormField(item, map);
                        } else {
                            processFileField(item, httpRequest);
                        }
                    }

                    request = UploadFilter.wrapRequest(httpRequest, map);

                } catch (FileUploadException ex) {
                    throw new ServletException(ex);
                }
            }
        }
        chain.doFilter(request, response);
    }

    private static HttpServletRequest wrapRequest(HttpServletRequest request,
            final Map<String, String[]> parameterMap) {
        /**
         * Provides a convenient implementation of the HttpServletRequest
         * interface that can be subclassed by developers wishing to adapt the
         * request to a Servlet
         */
        return new HttpServletRequestWrapper(request) {
            // inner methods passed as parameters
            @Override
            public Map<String, String[]> getParameterMap() {
                return parameterMap;
            }

            @Override
            public String[] getParameterValues(String name) {
                return (String[]) parameterMap.get(name);
            }

            @Override
            public String getParameter(String name) {
                String[] params = getParameterValues(name);
                if (params == null) {
                    return null;
                }
                return params[0];
            }

            @Override
            public Enumeration<String> getParameterNames() {
                return Collections.enumeration(parameterMap.keySet());
            }
        };
    }

    private void processFormField(FileItem formField,
            Map<String, String[]> parameterMap) {

        String name = formField.getFieldName();
        String value = formField.getString();
        String[] values = parameterMap.get(name);

        if (values == null) {
            // Not in parameter map yet, so add as new value.
            parameterMap.put(name, new String[]{value});
        } else {
            // Multiple field values, so add new value to existing array.
            int length = values.length;
            String[] newValues = new String[length + 1];
            
            System.arraycopy(values, 0, newValues, 0, length);
            
            newValues[length] = value;
            parameterMap.put(name, newValues);
        }
    }

    private void processFileField(FileItem fileField, HttpServletRequest request) {
        request.setAttribute(fileField.getFieldName(), fileField);
    }

    /**
     * Method for retrieving the temporary directory for storing uploaded file
     * By default the system's temporary directory will be used if no
     * reposistory path is configure in the web.xml
     *
     * @param config
     */
    private void setRepositoryPath(FilterConfig config) {
        String repositoryInitParam = config
                .getInitParameter("by.creepid.jsf.fileupload.UploadFilter.repositoryPath");

        if (repositoryInitParam == null || "".equals(repositoryInitParam)) {
            repositoryInitParam = System.getProperty("java.io.tmpdir");
        }

        if (!repositoryInitParam.endsWith("/")) {
            repositoryInitParam += "/";
        }

        this.repositoryPath = repositoryInitParam;
    }

    @Override
    public void destroy() {
    }

}
