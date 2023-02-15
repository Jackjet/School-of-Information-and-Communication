package d1.project.cms.enums;

public enum ContentStatus {
    WAITING_FOR_AUDIT("0", "待审核"),
    ALREADY_PASSED("1", "已通过"),
    REJECTED("2", "已驳回");

    private String name;
    private String chineseName;

    ContentStatus(String name, String chineseName) {
        this.name = name;
        this.chineseName = chineseName;
    }

    public static ContentStatus getContentStatusByName(String name) {
        for (ContentStatus contentStatus : ContentStatus.values()) {
            if (contentStatus.getName().equals(name.toLowerCase())) {
                return contentStatus;
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
