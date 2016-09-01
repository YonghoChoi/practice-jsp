package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MultipartParser {
    private static final int DATA_SEQ_CYCLE = 4;
    private static final int BOUNDARY_LINE = 0;
    private static final int DATA_NAME_LINE = 1;
    private static final int EMPTY_LINE = 2;
    private static final int VALUE_LINE = 3;

    private static MultipartParser instance;

    public static MultipartParser getInstance() {
        if(instance == null) {
            instance = new MultipartParser();
        }

        return instance;
    }

    public Map<String, String[]> parse(InputStream input) throws IOException {
        Map<String, String[]> parameterMap = new HashMap<>();

        BufferedReader rd = new BufferedReader(new InputStreamReader(input));

        String line;
        String key = "";
        String value = "";
        int cycle = 0;
        while((line = rd.readLine()) != null) {
            switch(cycle++) {
                case BOUNDARY_LINE:
                    // skip
                    break;
                case DATA_NAME_LINE:
                    key = parseName(line);
                    break;
                case EMPTY_LINE:
                    // skip
                    break;
                case VALUE_LINE:
                    value = parseValue(line);
                    break;
            }

            if(cycle == DATA_SEQ_CYCLE) {
                cycle = 0;
                String[] values;
                if(parameterMap.containsKey(key)) {
                    StringArray strArr = new StringArray(parameterMap.get(key));
                    strArr.add(value);
                    values = strArr.get();
                } else {
                    values = new String[]{value};
                }
                parameterMap.put(key, values);
            }
        }

        rd.close();
        return parameterMap;
    }

    private String parseName(String str) {
        String name = "";

        if(str.contains("Content-Disposition")) {
            str = str.split("Content-Disposition:")[1];
            String contentDisposition = str.substring(0, str.indexOf(";")).trim();
            System.out.println("Content-Disposition : " + contentDisposition);
            str = str.split(";")[1];
        }

        if(str.contains("name")) {
            str = str.split("name=")[1];
            name = str.replace("\"", "");
            System.out.println("name : " + name);
        }

        return name;
    }

    private String parseValue(String str) {
        System.out.println("value : " + str);
        return str;
    }
}
