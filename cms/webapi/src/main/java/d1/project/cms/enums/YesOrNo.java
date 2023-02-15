package d1.project.cms.enums;

public enum YesOrNo {
    NO("0", "否"),
    YES("1", "是");

    private String name;
    private String chineseName;

    YesOrNo(String name, String chineseName) {
        this.name = name;
        this.chineseName = chineseName;
    }

    public static YesOrNo getYesOrNoByName(String name) {
        for (YesOrNo yesOrNo : YesOrNo.values()) {
            if (yesOrNo.getName().equals(name.toLowerCase())) {
                return yesOrNo;
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
