-- 初始化数据库库表数据
--1. 管理用户初始化一个admin用户
INSERT INTO d1_web_admin_user (ID,NAME, PASSWORD,tel,role_Id,role_Name) SELECT
	'admin',
	'admin',
	'e10adc3949ba59abbe56e057f20f883e',
	'admin',
	'admin',
	'超级管理员'
WHERE
	NOT EXISTS (
		SELECT
			*
		FROM
			d1_web_admin_user
		WHERE
			ID = 'admin'
	);
--2. 初始化菜单
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '1','首页','','/dashboard','menu','1','iconfont icon-tuanduicankaoxian-' WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '1');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '2','数据源管理','','2','menu','2','iconfont icon-zhinengsuo' WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '2');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '2-1','数据源管理','2','/dataSource/index','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '2-1');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '3','接入任务管理','','3','menu','3','iconfont icon-yunweiguanli'  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '3');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '3-1','任务组管理','3','/taskManage/index','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '3-1');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '3-2','任务管理','3','/taskManage/manag','menu','1',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '3-2');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '3-3','任务执行日志','3','/taskManage/executionlog','menu','2',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '3-3');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '3-4','任务组执行日志','3','/taskManage/grouplog','menu','3',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '3-4');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '5','用户管理','','5','menu','4','iconfont icon-yonghushezhi-'  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '5');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '5-1','用户管理','5','/omMager/user','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '5-1');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '5-2','角色管理','5','/omMager/comments','menu','1',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '5-2');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '5-3','用户组管理','5','/omMager/userGroup','menu','2',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '5-3');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '7','质量检查规则','','7','menu','6','iconfont icon-zuoce-zonghejiankong'  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '7');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '7-1','规则管理','7','/rule/manage','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '7-1');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '7-2','规则日志','7','/rule/log','menu','1',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '7-2');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '8','数据模板','','8','menu','5','iconfont icon-yunshujukuRedis'  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '8');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '8-2','模板管理','8','/dataBase/examples','menu','1',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '8-2');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '9','法规与知识','','9','menu','8','iconfont icon-wulianwang- '  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '9');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '9-1','政策法规','9','/Laws/Knowledge','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '9-1');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '9-2','知识广场','9','/Laws/Regulations','menu','1',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '9-2');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '10','配置管理','','10','menu','9','iconfont icon-Icon-yuanchengxiezuo'  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '10');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '10-1','评分体系','10','/Configuration/fore','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '10-1');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '10-2','数据问题分类','10','/Configuration/dataClassification','menu','1',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '10-2');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '11','质量报告','','11','menu','10','iconfont icon-zhishizhongxin'  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '11');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '11-1','质量报告','11','/report/reIndex','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '11-1');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '12','数据质量工单','','12','menu','7','iconfont icon-25'  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '12');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '12-1','工单管理','12','/Dqwt/womanagement','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '12-1');
--3. 初始化管理员角色
INSERT INTO d1_role (id, name, create_time) SELECT 'admin','超级管理员',now()  WHERE NOT EXISTS ( SELECT * FROM d1_role WHERE id = 'admin');
--4. 初始化管理员角色权限
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '1','admin','1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '1');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '2','admin','2'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '2');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '3','admin','2-1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '3');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '4','admin','3'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '4');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '5','admin','3-1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '5');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '6','admin','3-2'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '6');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '7','admin','3-3'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '7');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '8','admin','3-4'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '8');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '14','admin','5'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '14');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '15','admin','5-1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '15');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '16','admin','5-2'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '16');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '17','admin','5-3'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '17');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '23','admin','7'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '23');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '24','admin','7-1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '24');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '25','admin','7-2'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '25');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '28','admin','8'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '28');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '30','admin','8-2'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '30');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '34','admin','9'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '34');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '35','admin','9-1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '35');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '36','admin','9-2'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '36');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '37','admin','9-3'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '37');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '38','admin','10'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '38');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '39','admin','10-1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '39');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '40','admin','10-2'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '40');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '41','admin','11'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '41');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '42','admin','11-1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '42');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '43','admin','12'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '43');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '44','admin','12-1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '44');