# 说明文档

- [开发中常用的工具推荐](./RECOMMEND.md)
- [开发流程及命名规范](./RULES.md)
- [开发人员及任务列表](./RE_DEV.md)

## 项目结构说明
- 项目`module`说明：
    - `library`存放统一的库文件，方便以后项目迁移和重构
    - `app`为实际开发module。其他module视功能进行添加
- `app`目录具体文件夹功能：
    - `./bean/`目录为统一实体类存放目录，在该目录下根据具体的模块分别建立二级目录存放bean对象。如`./bean/center`存放个人中心的bean对象。
    - `./module/`目录为数据层，其下分为三个目录：
        - `./local/`：该目录为本地逻辑处理目录，其下二级目录的建立同上`bean`目录。
        - `./logic/`：该目录为一些系统及软件本身状态处理的逻辑，为全局统一的模块，一般情况下请勿修改。
        - `./util/`：该目录为一些工具类。
    - `./ui/`目录为界面层，所有界面层的东西均按照模块放置到该目录；
- `drawable`及`xml`命名及存放见[RULES.md](./RULES.md)中说明。

## 注意事项
1. 软件中所有的统一使用模块：
    - 日志打印：`PLogger`；
    - Toast提示：`PToast`；
    - Sharedpreferences：`PSP`；
    - 请求缓存：`PCache`；
    - 消息派发：`MsgMgr`
2. 软件中数据库使用[GreenDao](https://github.com/greenrobot/greenDAO)，进行数据库操作时各自学习使用。`./bean/db/`目录下的文件为GreenDao自动生成，请勿上传。

## 测试接口
- 清除已经心动过、忽略过状态 测试接口：http://test.app.xiaoyouapp.cn:8681/l/gm/CleanHeartCache?uid=xxx
- 心动测试接口：http://test.app.xiaoyouapp.cn:8681/l/heart/HeartServer?uid=xxx&to_uid=xxx&type=1
- 活动消息的测试接口：http://test.app.xiaoyouapp.cn:8681/test/TestNews?uid=xxx
- 系统消息的测试接口：http://test.app.xiaoyouapp.cn:8681/test/TestSys?uid=xxx
- 加好友消息的测试接口：http://test.app.xiaoyouapp.cn:8681/l/userrelation/AddFriend?uid=xxx&toid=xxx
- 钻石 金币测试接口：http://test.app.xiaoyouapp.cn:8681/l/gm/TestGemAndCoins?uid=xxx&coin_type=1&num=1000 (coin_type：1-钻石， 2-金币 num：数量 大于0为加，小于零为减)
- 消费货币(宝石、金币)：http://test.app.xiaoyouapp.cn:8681/l/gm/CostCoin?uid=xxx&to_uid=xxx&coin_type=1&cnt=100 (coin_type:1-钻石， 2-金币 cnt:消耗数量 to_uid:消费的目标对象,to_uid == uid则自己消耗)
- 开通vip: http://test.app.xiaoyouapp.cn:8681/l/gm/ChangeVipEndTime?uid=xxx&months=1
- 开通包月: http://test.app.xiaoyouapp.cn:8681/l/gm/ChangeMonthEndTime?uid=xxx&months=1
- 清除绑定手机号：http://test.app.xiaoyouapp.cn:8681/l/gm/CleanBindPhone?uid=xxx&phone=186xxx&tag=303 (功能标记301-小友密码重置，302-小友绑定手机号,303 解绑手机号)
- 清除短信次数限制：http://test.app.xiaoyouapp.cn:8681/l/gm/CleanShortMsgTimes?phone=xxx&tag=301 (功能标记301-小友密码重置，302-小友绑定手机号)
- 添加粉丝接口：：http://test.app.xiaoyouapp.cn:8681/l/gm/AddFuns?uid=xxx&to_uid=xxx (to_uid:目标用户id,成为to_uid的粉丝)
- 添加红包：：http://test.app.xiaoyouapp.cn:8681/l/gm/AddRedOwn?uid=xxx&num=100 (num:单位分)
- 游戏发奖励接口(post提交)：http://test.app.xiaoyouapp.cn:8681/l/prize/Award {"uidlist":[80000044],"type":10200}(uidlist:用户排行榜，从第一名到第N名 type:10100-魅力榜日榜 10200-魅力榜周榜 20100-富豪榜日榜 20200-富豪榜周榜 40100-过关榜日榜 40200-过关榜周榜)
- 测试接口：http://doc.api.yuanfenba.net/pkg/yuanfen/yfb_service/modules/gm/
- 通用接口测试地址：http://test.msg.yuanfenba.net:8681/test/TestPage

## 开发定义说明文档

### 客户端--golang服务HTTP接口

新增三种接口模式(之前小友的/m/和/md/在缘分吧不再适用。xxtea加密即通过本地so进行JNI的加密，so文件已提供)。

> [X加密表示post的DATA直接xxtea加密，不验证get的hash。并且返回的数据也通过 xxtea加密](http://doc.master.yuanfenba.net/pkg/yuanfen/yfb_service/modules/test/#TestModule.X_UserCache)。xs的基本一样，只是多个cookie验证。

- [/s/：不加密只验证用户身份范例,只验证用户AUTH](http://doc.master.yuanfenba.net/pkg/yuanfen/yfb_service/modules/test/#TestModule.S_UserCache)
- [/x/：加密入口范例,加密但不验证用户AUTH](http://doc.master.yuanfenba.net/pkg/yuanfen/yfb_service/modules/test/#TestModule.X_UserCache)
- [/xs/：加密入口范例,加密且验证用户AUTH](http://doc.master.yuanfenba.net/pkg/yuanfen/yfb_service/modules/test/#TestModule.XS_UserCache)

### socket协议

要点：
1. 消息通过socket长连接进行接收，通过http短连接进行发送。
2. socket登录时，须先发送一次65535类型的心跳消息，再发送登录消息。
3. 心跳回送机制：保持长连接的心跳消息类型为65532，客户端发送该类型心跳之后，服务端会回复一条同类型的空消息作为应答。
如果服务端未在规定时间内应答的话socket重新进行连接。
4. 多账号登录踢下线机制[待服务器对接]
5. 所有发送和接收的消息体经过xxtea加密，socket内部逻辑已做处理，外部无需关心。

周隆的联调服务器：103.25.36.7:8823

具体实现见：[IMProxy.java](./friends/src/main/java/com/xiaoyou/friends/module/service/IMProxy.java)与[AutoConnectMgr.java](./friends/src/main/java/com/xiaoyou/friends/module/service/AutoConnectMgr.java)
