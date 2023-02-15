package d1.project.cms.enums;

/**
 * @author libin
 */
public enum ResourceFileType {
    FOLDER("0", "文件夹"),
    PICTURE("1", "图片"),
    ZIP("2", "zip"),
    PDF("2", "pdf");

    private String name;
    private String chineseName;

    ResourceFileType(String name, String chineseName) {
        this.name = name;
        this.chineseName = chineseName;
    }

    public static ResourceFileType getResourceFileTypeByName(String name) {
        for (ResourceFileType resourceFileType : ResourceFileType.values()) {
            if (resourceFileType.getName().equals(name.toLowerCase())) {
                return resourceFileType;
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
