package d1.project.caict.common;

import java.util.Arrays;
import java.util.List;

/**
 * 一些常量，包括枚举等
 *
 * @author Buter
 * @date 2020/3/18 16:09
 */
public class Constants {
    /**
     * 用户登录后token失效时间，暂定1天
     */
    public final static int TOKEN_EXPIRE_TIME = 24 * 60 * 60;
    /**
     * 不可删除角色
     */
    public final static List<String> CANNOT_DELETED_ROLE = Arrays.asList("admin", "default");
    /**
     * 不可删除菜单
     */
    public final static List<String> CANNOT_DELETED_MENU_TREE = Arrays.asList("5dc34b8badd74d6e83e87b1790645a67", "230c5e506f2545f8a3355d175669f0a1", "1ec22731f0c04f9594213ab5c7eeb512",
            "ce284f012d4a416796cfdfc8306805bd", "c38db1109f024cc8a22c731b8f1aa331", "f36a7d79af404114b2b470ca03ca6f2c", "4065e178bbb845a597ab31ce3e2f5a7d", "e3d87025f04b497b8e76b4dd0b4b60c5",
            "6d648fee82d5421ea44e21b10511157d", "8e57f180e2764bbf832653e73bed9e72", "735892792aa7469fb58a4734d5fdd68a", "29386315b65f4fc0aac7953eb4114993",
            "786a304f06e047db9442bb3d9b8da045", "b7315840b3334a729139731f7bab00ad", "0071610f90bd4fa5ab7cef62314c6f58", "27c80f8ac976408fb586bd6b146ce345", "2c5cf217fbd94ade80fcba2a0a141211",
            "910284b3345d44048baf9b553be45be6", "c7231f8015e94ea3b1dc46a205838cd6", "9de6c3d8618f4586a25b2660250b6c07", "46f1380af87f406b81c6b67d0a087a29", "389cbd1fbcf74406914c23bf8ce565e7",
            "e690850711aa4a5f99afae2a7c89b3ef", "a1a225de54664af7ac9aa46ed049bdc4", "f6dd96d09168486a97b583df8d9c857a", "2b72778c51f24aa39e166964b205e97b", "9777b1935fee4ed6b60995632e1c884c",
            "b97e18927bcb40f4b8d3a6e2f9c12a24", "22b20c1bd5b449eb912e07a14769dc46", "00ea5f1c14e24c24b2eea961a07ba8ad", "61ce957dafe54a54ba2efd5da9366e80", "f051590bf5b0453d8ac9e5b3ef1a3714",
            "882f230b736e4fb1bb71230d85c933c7"
    );
    /**
     * 不可删除部门
     */
    public final static String CANNOT_DELETED_ORGANIZATION = "headquarters";
    public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 用户删除事件
     */
    public final static String USER_DELETE_EVENT = "userDelete";


}
