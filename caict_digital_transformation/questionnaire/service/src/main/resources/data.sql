-- 初始化数据库库表数据
--1. 管理用户初始化一个admin用户
INSERT INTO d1_web_admin_user (ID,DEPT_ID ,NAME, PASSWORD,AGE,SEX) SELECT
	'admin',
	'0000',
	'admin',
	'e10adc3949ba59abbe56e057f20f883e',
	100,
	0
FROM
	DUAL
WHERE
	NOT EXISTS (
		SELECT
			*
		FROM
			d1_web_admin_user
		WHERE
			ID = 'admin'
	);

--2. 图片素材的 默认分组 富文本编辑框
INSERT INTO d1_material_group (ID,NAME ,TYPE, create_by_id,create_time) SELECT
	'0',
	'默认分组',
	'0',
	'admin',
	'2120-09-30 16:46:21'
FROM
	DUAL
WHERE
	NOT EXISTS (
		SELECT
			*
		FROM
			d1_material_group
		WHERE
			ID = '0'
	);
	INSERT INTO d1_material_group (ID,NAME ,TYPE, create_by_id,create_time) SELECT
	'0_imageRichText',
	'富文本编辑框',
	'0',
	'admin',
	'2120-09-30 16:40:21'
FROM
	DUAL
WHERE
	NOT EXISTS (
		SELECT
			*
		FROM
			d1_material_group
		WHERE
			ID = '0_imageRichText'
	);
	--3. 视频的 默认分组 富文本编辑框
INSERT INTO d1_material_group (ID,NAME ,TYPE, create_by_id,create_time) SELECT
	'1',
	'默认分组',
	'1',
	'admin',
	'2120-09-30 16:46:21'
FROM
	DUAL
WHERE
	NOT EXISTS (
		SELECT
			*
		FROM
			d1_material_group
		WHERE
			ID = '1'
	);
	INSERT INTO d1_material_group (ID,NAME ,TYPE, create_by_id,create_time) SELECT
	'1_videoRichText',
	'富文本编辑框',
	'1',
	'admin',
	'2120-09-30 16:40:21'
FROM
	DUAL
WHERE
	NOT EXISTS (
		SELECT
			*
		FROM
			d1_material_group
		WHERE
			ID = '1_videoRichText'
	);
	--3. 音频的 默认分组 富文本编辑框
INSERT INTO d1_material_group (ID,NAME,TYPE,create_by_id,create_time) SELECT
	'2',
	'默认分组',
	'2',
	'admin',
	'2120-09-30 16:46:21'
FROM
	DUAL
WHERE
	NOT EXISTS (
		SELECT
			*
		FROM
			d1_material_group
		WHERE
			ID = '2'
	);
	INSERT INTO d1_material_group (ID,NAME,TYPE,create_by_id,create_time) SELECT
	'2_audioRichText',
	'富文本编辑框',
	'2',
	'admin',
	'2120-09-30 16:40:21'
FROM
	DUAL
WHERE
	NOT EXISTS (
		SELECT
			*
		FROM
			d1_material_group
		WHERE
			ID = '2_audioRichText'
	);
-- 隐私协议 服务协议管理初始化
INSERT INTO d1_agreement_management (ID,create_by_id,create_time) SELECT
	'ysxy',
	'admin',
	'2120-09-30 16:46:21'
FROM
	DUAL
WHERE
	NOT EXISTS (
		SELECT
			*
		FROM
			d1_agreement_management
		WHERE
			ID = 'ysxy'
	);
	INSERT INTO d1_agreement_management (ID,create_by_id,create_time) SELECT
	'fwxy',
	'admin',
	'2120-09-30 16:46:21'
FROM
	DUAL
WHERE
	NOT EXISTS (
		SELECT
			*
		FROM
			d1_agreement_management
		WHERE
			ID = 'fwxy'
	);

-- 初始化题库分组
INSERT INTO d1_examination_group (ID,NAME,create_by_id,create_time) SELECT
	'1',
	'未分组',
	'admin',
	now()
FROM
	DUAL
WHERE
	NOT EXISTS (
		SELECT
			*
		FROM
			d1_examination_group
		WHERE
			ID = '1'
	);
