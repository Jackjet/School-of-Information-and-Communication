package d1.project.questionnaire.util;

import org.springframework.stereotype.Repository;

/**
 * 异常
 *
 * @author Kikki 2019/8/29 11:04
 */
@Repository
public class ThrowsException {

    /**
     * 是否抛出异常
     *
     * @param b   boolean
     * @param msg 异常信息
     * @author Kikki 2019/8/29 18:04
     **/
    public void throwsException(boolean b, String msg) throws Exception {
        if (b) {
            throw new Exception(msg);
        }
    }

    /**
     * 用户修改之前通用异常
     *
     * @author Kikki 2019/8/29 14:49
     **/
    private void userBaseBeforeUpdate(int findOtherUserByNameOrPhone) throws Exception {
        throwsException(findOtherUserByNameOrPhone > 0, Constants.USERNAME_OR_PHONE_ALREADY_EXISTS_CANNOT_UPDATE);
    }

    /**
     * app用户修改之前异常
     *
     * @author Kikki 2019/8/29 14:51
     **/
    public void appUserBeforeUpdate(int findOtherUserByNameOrPhone) throws Exception {
        userBaseBeforeUpdate(findOtherUserByNameOrPhone);
    }

    /**
     * app用户修改信息以及获取用户信息前验证
     *
     * @param id     用户信息获取的用户ID
     * @param userId 库表中的用户ID
     * @throws Exception 异常处理
     */
    public void userCheck(String id, String userId) throws Exception {
        throwsException(!id.equals(userId), Constants.THIS_USER_HAS_NO_PRIVILEGES);
    }

    /**
     * webAdmin用户修改之前异常
     *
     * @author Kikki 2019/8/29 14:52
     **/
    public void webAdminBeforeUpdate(int findOtherUserByNameOrPhone) throws Exception {
        userBaseBeforeUpdate(findOtherUserByNameOrPhone);
    }

    /**
     * webAdmin用户创建之前异常
     *
     * @author Kikki 2019/8/29 14:52
     **/
    public void webAdminBeforeInsert(String name, String password, boolean existsByName) throws Exception {
        userBeforeUpdate(name, password);
        throwsException(existsByName, Constants.USERNAME_ALREADY_EXISTS_CANNOT_CREATE);
    }

    /**
     * 用户注册之前通用异常
     *
     * @author Kikki 2019/8/29 14:49
     **/
    public void appUserBeforeInsert(String name, String password, String phone, boolean existsByPhoneOrName) throws Exception {
        userBeforeUpdate(name, password);
        throwsException(phone == null, Constants.USER_PHONE_CANNOT_NULL);
        throwsException(existsByPhoneOrName, Constants.USERNAME_OR_PHONE_ALREADY_EXISTS_CANNOT_REGISTER);
    }

    /**
     * 用户创建之前通用异常
     *
     * @author Kikki 2019/8/29 14:49
     **/
    private void userBeforeUpdate(String name, String password) throws Exception {
        throwsException(name == null, Constants.USERNAME_CANNOT_NULL);
        throwsException(password == null, Constants.USER_PASSWORD_CANNOT_NULL);
    }

    /**
     * 检查通知消息标题长度
     *
     * @param length 标题长度
     * @throws Exception 异常处理
     */
    public void messageTitleLength(int length) throws Exception {
        int standardLength = 30;
        throwsException(length > standardLength, Constants.MESSAGE_TITLE_LENGTH);
    }
}
