package util;

public class StringArray {
    private String[] strArr;

    public StringArray(String[] strArr) {
        this.strArr = strArr.clone();
    }

    public StringArray(String value) {
        this.strArr = new String[]{value};
    }

    public void add(String value) {
        String[] oldValues = strArr;
        strArr = new String[strArr.length + 1];
        copy(oldValues);
        strArr[strArr.length - 1] = value;
    }

    public String[] get() {
        return strArr;
    }

    public void copy(String[] values) {
        int size = values.length;
        if(this.strArr.length < size) {
            throw new RuntimeException("input Array size is big rather than member Array.");
        }

        for(int i = 0; i < size; i++) {
            this.strArr[i] = values[i];
        }
    }
}
