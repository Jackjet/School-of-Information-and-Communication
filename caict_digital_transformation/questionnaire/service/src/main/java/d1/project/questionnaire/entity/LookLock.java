package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

;

/**
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LookLock", description = "看挂锁")
@Entity
@Table(name = "d1_look_lock")
@Data
public class LookLock extends BaseCreateEntity {
}
