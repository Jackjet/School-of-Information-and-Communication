package d1.project.caict.business.model;

import d1.project.caict.business.entity.MyFootprint;

import java.util.List;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public class MyFootprintFindAllVm {

    private String day;
    private List<MyFootprint> child;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<MyFootprint> getChild() {
        return child;
    }

    public void setChild(List<MyFootprint> child) {
        this.child = child;
    }
}
