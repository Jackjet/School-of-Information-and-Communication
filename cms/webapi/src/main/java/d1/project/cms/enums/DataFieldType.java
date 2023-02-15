package d1.project.cms.enums;

public enum DataFieldType {
    LIST("0", "列表数据模型"),
    DOCUMENT("1", "文档数据模型");

    private String name;
    private String chineseName;

    DataFieldType(String name, String chineseName) {
        this.name = name;
        this.chineseName = chineseName;
    }

    public static DataFieldType getDataFieldTypeByName(String name) {
        for (DataFieldType dataFieldType : DataFieldType.values()) {
            if (dataFieldType.getName().equals(name.toLowerCase())) {
                return dataFieldType;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getChineseName() {
        return chineseName;
    }
}
