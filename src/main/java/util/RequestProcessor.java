package util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RequestProcessor {

    //public static HashMap<String, String>  populate(Object bean, HttpServletRequest request) throws Exception {
    public static HashMap populate(Object bean, HttpServletRequest request, String tempDir) throws Exception {

        // Check that we have a file upload request
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (!isMultipart) {
            throw new Exception("miltipart 형식이 아님");
        }

        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(tempDir));

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding(request.getCharacterEncoding());
        // Set overall request size constraint
        // upload.setSizeMax ( yourMaxRequestSize );

        // Parse the request
        List /* FileItem */items = upload.parseRequest(request);
        //HashMap<String, String> itemMap = new HashMap<String, String>();
        //HashMap<String, FormFile> formFileMap = new HashMap<String, FormFile>();
        HashMap itemMap = new HashMap();
        HashMap formFileMap = new HashMap();

        Iterator iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();

            if (item.isFormField()) {
                String name = item.getFieldName();
                String value = item.getString(request.getCharacterEncoding());

                Object old = itemMap.get(name);
                if (old == null) {
                    itemMap.put(name, value);
                } else {
                    List<String> values = null;
                    if (old instanceof String) {
                        values = new ArrayList<>();
                        values.add((String) old);
                        values.add(value);
                    } else {
                        values = (List<String>) old;
                        values.add(value);
                    }

                    itemMap.put(name, values);
                }

            } else {
//                FormFile formFile = new CommonsFormFile(item);
//
//                String fieldName = item.getFieldName();
//
//                if (formFile.getFileSize() == 0) {
//                    continue;
//                }
//
//                formFileMap.put(fieldName, formFile);
            }
        }

        BeanUtils.populate(bean, itemMap);
        BeanUtils.populate(bean, formFileMap);

        return itemMap;
    }
}
