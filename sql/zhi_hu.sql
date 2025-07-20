create table zhi_hu_answer
(
    id          bigint   not null
        primary key,
    questionId  bigint   null comment '问题id',
    text        longtext null comment '回答内容, 富文本',
    author      char(32) null,
    star        int      null comment '赞同数量',
    comment     int      null comment '评论数量',
    update_time datetime null comment '编辑时间'
)
    comment '知乎回答';

create table zhi_hu_hot_item
(
    question_id bigint   not null,
    fetch_time  datetime not null,
    `rank`      int      not null,
    primary key (fetch_time, question_id)
)
    comment '知乎热榜数据';

create table zhi_hu_question
(
    id        bigint        not null
        primary key,
    title     varchar(512)  null comment '标题',
    text      longtext      null comment '问题内容, 富文本',
    topics    json          null comment '话题, json格式的数组',
    fetchTime datetime      null comment '抓取时间',
    url       varchar(1024) null
)
    comment '知乎问题';

