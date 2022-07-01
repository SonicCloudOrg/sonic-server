
-- ----------------------------
-- Table structure for agents
-- ----------------------------
CREATE TABLE IF NOT EXISTS `agents`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`host` varchar(255)  NOT NULL COMMENT 'agent的ip',
`name` varchar(255)  NOT NULL COMMENT 'agent name',
`port` int(11) NOT NULL COMMENT 'agent的端口',
`rpc_port` int(11) NOT NULL DEFAULT 0 COMMENT 'rpc端口',
`secret_key` varchar(255)  NULL DEFAULT '' COMMENT 'agent的密钥',
`status` int(11) NOT NULL COMMENT 'agent的状态',
`system_type` varchar(255)  NOT NULL COMMENT 'agent的系统类型',
`version` varchar(255)  NOT NULL DEFAULT '' COMMENT 'agent端代码版本',
`lock_version` bigint(20) NOT NULL DEFAULT 0 COMMENT '乐观锁，优先保证上下线状态落库',
`cabinet_id` int(11) NOT NULL DEFAULT 0 COMMENT '机柜Id',
`storey` int(11) NOT NULL DEFAULT 0 COMMENT '机柜层数',
PRIMARY KEY (`id`) 
) ;

-- ----------------------------
-- Table structure for cabinets
-- ----------------------------
CREATE TABLE IF NOT EXISTS `cabinets`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`size` int(11) NOT NULL COMMENT 'size',
`name` varchar(255)  NOT NULL COMMENT 'name',
`secret_key` varchar(255)  NOT NULL COMMENT '机柜密钥',
`low_level` int(11) NOT NULL DEFAULT 40 COMMENT 'lowLevel',
`low_gear` int(11) NOT NULL DEFAULT 1 COMMENT 'lowGear',
`high_level` int(11) NOT NULL DEFAULT 90 COMMENT 'highLevel',
`high_gear` int(11) NOT NULL DEFAULT 14 COMMENT 'highGear',
`high_temp` int(11) NOT NULL DEFAULT 45 COMMENT 'highTemp',
`high_temp_time` int(11) NOT NULL DEFAULT 15 COMMENT 'highTempTime',
`robot_secret` varchar(255)  NOT NULL COMMENT '机器人秘钥',
`robot_token` varchar(255)  NOT NULL COMMENT '机器人token',
`robot_type` int(11) NOT NULL DEFAULT 1 COMMENT '机器人类型',
 PRIMARY KEY (`id`) 
);

-- ----------------------------
-- Table structure for devices
-- ----------------------------
CREATE TABLE IF NOT EXISTS `devices`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`agent_id` int(11) NOT NULL COMMENT '所属agent的id',
`cpu` varchar(255)  NULL DEFAULT '' COMMENT 'cpu架构',
`img_url` varchar(255)  NULL DEFAULT '' COMMENT '手机封面',
`manufacturer` varchar(255)  NULL DEFAULT '' COMMENT '制造商',
`model` varchar(255)  NULL DEFAULT '' COMMENT '手机型号',
`name` varchar(255)  NULL DEFAULT '' COMMENT '设备名称',
`password` varchar(255)  NULL DEFAULT '' COMMENT '设备安装app的密码',
`platform` int(11) NOT NULL COMMENT '系统类型 1：android 2：ios',
`size` varchar(255)  NULL DEFAULT '' COMMENT '设备分辨率',
`status` varchar(255)  NULL DEFAULT '' COMMENT '设备状态',
`ud_id` varchar(255)  NULL DEFAULT '' COMMENT '设备序列号',
`version` varchar(255)  NULL DEFAULT '' COMMENT '设备系统版本',
`nick_name` varchar(255)  NULL DEFAULT '' COMMENT '设备备注',
`user` varchar(255)  NULL DEFAULT '' COMMENT '设备当前占用者',
`chi_name` varchar(255)  NULL DEFAULT '' COMMENT '中文设备',
`temperature` int(11) NULL DEFAULT 0 COMMENT '设备温度',
`level` int(11) NULL DEFAULT 0 COMMENT '设备电量',
`position` int(11) NULL DEFAULT 0 COMMENT 'Hub接口',
`gear` int(11) NULL DEFAULT 0 COMMENT 'Hub档位',
PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for global_params
-- ----------------------------
CREATE TABLE IF NOT EXISTS `global_params`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`params_key` varchar(255)  NOT NULL COMMENT '参数key',
`params_value` varchar(255)  NOT NULL COMMENT '参数value',
`project_id` int(11) NOT NULL COMMENT '所属项目id',
PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for elements
-- ----------------------------
CREATE TABLE IF NOT EXISTS `elements`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`ele_name` varchar(255)  NOT NULL COMMENT '控件名称',
`ele_type` varchar(255)  NOT NULL COMMENT '控件类型',
`ele_value` longtext  NULL COMMENT '控件内容',
`project_id` int(11) NOT NULL COMMENT '所属项目id',
PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for versions
-- ----------------------------
CREATE TABLE IF NOT EXISTS `versions`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`create_time` datetime(0) NOT NULL COMMENT '创建时间内',
`project_id` int(11) NOT NULL COMMENT '所属项目id',
`version_name` varchar(255)  NOT NULL COMMENT '迭代名称',
PRIMARY KEY (`id`) 
);

-- ----------------------------
-- Table structure for users
-- ----------------------------
CREATE TABLE IF NOT EXISTS `users`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`password` varchar(255)  NOT NULL COMMENT '密码',
`role` int(11) NOT NULL COMMENT '角色',
`user_name` varchar(255)  NOT NULL COMMENT '用户名',
`source` varchar(255)  NOT NULL DEFAULT 'local' COMMENT '用户来源',
PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for test_suites_test_cases
-- ----------------------------
CREATE TABLE IF NOT EXISTS `test_suites_test_cases`  (
`test_suites_id` int(11) NOT NULL COMMENT '测试套件id',
`test_cases_id` int(11) NOT NULL COMMENT '测试用例id',
`sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序用'
);

-- ----------------------------
-- Table structure for test_suites_devices
-- ----------------------------
CREATE TABLE IF NOT EXISTS `test_suites_devices`  (
`test_suites_id` int(11) NOT NULL COMMENT '测试套件id',
`devices_id` int(11) NOT NULL COMMENT '设备id',
`sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序用'
);

-- ----------------------------
-- Table structure for test_suites
-- ----------------------------
CREATE TABLE IF NOT EXISTS `test_suites`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`cover` int(11) NOT NULL COMMENT '覆盖类型',
`name` varchar(255)  NOT NULL COMMENT '测试套件名字',
`platform` int(11) NOT NULL COMMENT '测试套件系统类型（android、ios...）',
`project_id` int(11) NOT NULL COMMENT '覆盖类型',
PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for test_cases
-- ----------------------------
CREATE TABLE IF NOT EXISTS `test_cases`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`des` varchar(255)  NOT NULL COMMENT '用例描述',
`designer` varchar(255)  NOT NULL COMMENT '用例设计人',
`edit_time` datetime(0) NOT NULL COMMENT '最后修改日期',
`module` varchar(255)  NOT NULL COMMENT '所属模块',
`name` varchar(255)  NOT NULL COMMENT '用例名称',
`platform` int(11) NOT NULL COMMENT '设备系统类型',
`project_id` int(11) NOT NULL COMMENT '所属项目id',
`version` varchar(255)  NOT NULL COMMENT '版本号',
PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for steps_elements
-- ----------------------------
CREATE TABLE IF NOT EXISTS `steps_elements`  (
`steps_id` int(11) NOT NULL COMMENT '步骤id',
`elements_id` int(11) NOT NULL COMMENT '控件id'
);

-- ----------------------------
-- Table structure for steps
-- ----------------------------
CREATE TABLE IF NOT EXISTS `steps`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`parent_id` int(11) NOT NULL DEFAULT 0 COMMENT '父级id，一般父级都是条件步骤',
`case_id` int(11) NOT NULL COMMENT '所属测试用例id',
`content` longtext  NOT NULL COMMENT '输入文本',
`error` int(11) NOT NULL COMMENT '异常处理类型',
`platform` int(11) NOT NULL COMMENT '设备系统类型',
`project_id` int(11) NOT NULL COMMENT '所属项目id',
`sort` int(11) NOT NULL COMMENT '排序号',
`step_type` varchar(255)  NOT NULL COMMENT '步骤类型',
`text` longtext  NOT NULL COMMENT '其它信息',
`condition_type` int(11) NOT NULL DEFAULT 0 COMMENT '条件类型',
PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for results
-- ----------------------------
CREATE TABLE IF NOT EXISTS `results`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`create_time` datetime(0) NOT NULL COMMENT '任务创建时间',
`end_time` datetime(0) NULL DEFAULT NULL COMMENT '任务结束时间',
`project_id` int(11) NOT NULL COMMENT '所属项目id',
`receive_msg_count` int(11) NOT NULL COMMENT '接受消息数量',
`send_msg_count` int(11) NOT NULL COMMENT '发送消息数量',
`status` int(11) NOT NULL COMMENT '结果状态',
`strike` varchar(255)  NULL DEFAULT '' COMMENT '触发者',
`suite_id` int(11) NOT NULL COMMENT '测试套件id',
`suite_name` varchar(255)  NULL DEFAULT '' COMMENT '测试套件名字',
PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for result_detail
-- ----------------------------
CREATE TABLE IF NOT EXISTS `result_detail`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`case_id` int(11) NOT NULL COMMENT '测试用例id',
`des` varchar(255)  NULL DEFAULT '' COMMENT '描述',
`device_id` int(11) NOT NULL COMMENT '设备id',
`log` longtext  NULL COMMENT '日志信息',
`result_id` int(11) NOT NULL COMMENT '所属结果id',
`status` int(11) NOT NULL COMMENT '步骤执行状态',
`time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '步骤执行状态',
`type` varchar(255)  NULL DEFAULT '' COMMENT '测试结果详情类型',
PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for public_steps_steps
-- ----------------------------
CREATE TABLE IF NOT EXISTS `public_steps_steps`  (
`public_steps_id` int(11) NOT NULL COMMENT '公共步骤id',
`steps_id` int(11) NOT NULL COMMENT '步骤id'
);

-- ----------------------------
-- Table structure for public_steps
-- ----------------------------
CREATE TABLE IF NOT EXISTS `public_steps`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(255)  NOT NULL COMMENT '公共步骤名称',
`platform` int(11) NOT NULL COMMENT '公共步骤系统类型（android、ios...）',
`project_id` int(11) NOT NULL COMMENT '所属项目id',
PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for projects
-- ----------------------------
CREATE TABLE IF NOT EXISTS `projects`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`edit_time` datetime(0) NULL DEFAULT NULL COMMENT '更改时间',
`project_des` varchar(255)  NOT NULL COMMENT '项目描述',
`project_img` varchar(255)  NOT NULL COMMENT '项目封面',
`project_name` varchar(255)  NOT NULL COMMENT '项目名',
`robot_secret` varchar(255)  NOT NULL COMMENT '机器人秘钥',
`robot_token` varchar(255)  NOT NULL COMMENT '机器人token',
`robot_type` int(11) NOT NULL COMMENT '机器人类型',
PRIMARY KEY (`id`) 
);

-- ----------------------------
-- Table structure for modules
-- ----------------------------
CREATE TABLE IF NOT EXISTS `modules`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(255)  NULL DEFAULT NULL,
`project_id` int(11) NOT NULL COMMENT '所属项目名称',
PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for jobs
-- ----------------------------
CREATE TABLE IF NOT EXISTS `jobs`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`cron_expression` varchar(255)  NOT NULL COMMENT 'cron表达式',
`name` varchar(255)  NOT NULL COMMENT '任务名称',
`project_id` int(11) NOT NULL COMMENT '所属项目id',
`status` int(11) NOT NULL COMMENT '任务状态 1：开启 2：关闭',
`suite_id` int(11) NOT NULL COMMENT '测试套件id',
PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS  QRTZ_JOB_DETAILS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    JOB_CLASS_NAME   VARCHAR(250) NOT NULL,
    IS_DURABLE VARCHAR(5) NOT NULL,
    IS_NONCONCURRENT VARCHAR(5) NOT NULL,
    IS_UPDATE_DATA VARCHAR(5) NOT NULL,
    REQUESTS_RECOVERY VARCHAR(5) NOT NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
    );

CREATE TABLE IF NOT EXISTS QRTZ_TRIGGERS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    NEXT_FIRE_TIME BIGINT(13) NULL,
    PREV_FIRE_TIME BIGINT(13) NULL,
    PRIORITY INTEGER NULL,
    TRIGGER_STATE VARCHAR(16) NOT NULL,
    TRIGGER_TYPE VARCHAR(8) NOT NULL,
    START_TIME BIGINT(13) NOT NULL,
    END_TIME BIGINT(13) NULL,
    CALENDAR_NAME VARCHAR(200) NULL,
    MISFIRE_INSTR SMALLINT(2) NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
    REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
    );

CREATE TABLE IF NOT EXISTS QRTZ_SIMPLE_TRIGGERS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    REPEAT_COUNT BIGINT(7) NOT NULL,
    REPEAT_INTERVAL BIGINT(12) NOT NULL,
    TIMES_TRIGGERED BIGINT(10) NOT NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    );

CREATE TABLE IF NOT EXISTS QRTZ_CRON_TRIGGERS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    CRON_EXPRESSION VARCHAR(200) NOT NULL,
    TIME_ZONE_ID VARCHAR(80),
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    );

CREATE TABLE IF NOT EXISTS QRTZ_SIMPROP_TRIGGERS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    );

CREATE TABLE IF NOT EXISTS QRTZ_BLOB_TRIGGERS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    BLOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    );

CREATE TABLE IF NOT EXISTS QRTZ_CALENDARS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    CALENDAR_NAME  VARCHAR(200) NOT NULL,
    CALENDAR BLOB NOT NULL,
    PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
    );

CREATE TABLE IF NOT EXISTS QRTZ_PAUSED_TRIGGER_GRPS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_GROUP  VARCHAR(200) NOT NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
    );

CREATE TABLE IF NOT EXISTS QRTZ_FIRED_TRIGGERS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    ENTRY_ID VARCHAR(95) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    FIRED_TIME BIGINT(13) NOT NULL,
    SCHED_TIME BIGINT(13) NOT NULL,
    PRIORITY INTEGER NOT NULL,
    STATE VARCHAR(16) NOT NULL,
    JOB_NAME VARCHAR(200) NULL,
    JOB_GROUP VARCHAR(200) NULL,
    IS_NONCONCURRENT VARCHAR(1) NULL,
    REQUESTS_RECOVERY VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,ENTRY_ID)
    );

CREATE TABLE IF NOT EXISTS QRTZ_SCHEDULER_STATE
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
    CHECKIN_INTERVAL BIGINT(13) NOT NULL,
    PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
    );

CREATE TABLE IF NOT EXISTS QRTZ_LOCKS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    LOCK_NAME  VARCHAR(40) NOT NULL,
    PRIMARY KEY (SCHED_NAME,LOCK_NAME)
);

-- ----------------------------
-- Table structure for resources
-- ----------------------------
CREATE TABLE IF NOT EXISTS `resources`  (
`id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '资源信息表 id',
`desc` varchar(255)   DEFAULT NULL COMMENT '描述',
`parent_id` bigint(11) NULL DEFAULT 0 COMMENT '父级 id',
`method` varchar(255) DEFAULT NULL COMMENT '请求方法',
`path` varchar(255) DEFAULT NULL COMMENT '资源路径',
`white` tinyint(2) NULL DEFAULT 1 COMMENT '是否是白名单 url，0是 1 不是',
`need_auth` tinyint(2) NULL DEFAULT 1 COMMENT '是否需要鉴权，0 不需要 1 需要',
`create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
`update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
PRIMARY KEY (`id`)
) ;

