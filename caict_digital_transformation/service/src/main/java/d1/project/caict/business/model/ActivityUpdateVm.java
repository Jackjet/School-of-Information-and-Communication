package d1.project.caict.business.model;

import javax.validation.constraints.NotBlank;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-09 10:01
 */
public class ActivityUpdateVm extends ActivityInsertVm {

    @NotBlank(message = "id不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
