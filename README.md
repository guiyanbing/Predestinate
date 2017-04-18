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

- 待文档

## 开发定义说明文档

### 客户端--golang服务HTTP接口

新增三种接口模式(之前小友的/m/和/md/在缘分吧不再适用。xxtea加密即通过本地so进行JNI的加密，so文件已提供)。

> [X加密表示post的DATA直接xxtea加密，不验证get的hash。并且返回的数据也通过 xxtea加密](http://doc.master.yuanfenba.net/pkg/yuanfen/yfb_service/modules/test/#TestModule.X_UserCache)。xs的基本一样，只是多个cookie验证。

- [/s/：不加密只验证用户身份范例,只验证用户AUTH](http://doc.master.yuanfenba.net/pkg/yuanfen/yfb_service/modules/test/#TestModule.S_UserCache)
- [/x/：加密入口范例,加密但不验证用户AUTH](http://doc.master.yuanfenba.net/pkg/yuanfen/yfb_service/modules/test/#TestModule.X_UserCache)
- [/xs/：加密入口范例,加密且验证用户AUTH](http://doc.master.yuanfenba.net/pkg/yuanfen/yfb_service/modules/test/#TestModule.XS_UserCache)

### socket协议

- 待文档