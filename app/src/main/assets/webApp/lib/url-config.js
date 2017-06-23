/**
 * Created by wizard on 2016/12/29.
 */

var web = web || {};
web.urlConfig = {
  agentURL : "http://test.api2.app.yuanfenba.net/", //测试服
  // agentURL : "http://api2.app.yuanfenba.net/", //正式服
};

web.urlType = {
  Php: 'php',
  Go: 'go',
  Pay: 'pay',
  Image: 'image'
}

web.urlMethod = {
  MostSendList: 'gift/mostsendlist',
  MostGetList: 'gift/mostgetlist',
  ReceiveGifts: 'gift/getmyreceiveGifts',
  MySendGifts: 'gift/getmysendGifts',
  BuyYCoinList: 'user/getActivityList',
  BuyVIPList: 'user/getChargeList',
  GetPhoneSMS: 'public/sendSMS',
  CheckPhoneNum: 'user/bindCellPhone',
  GetActivity: 'user/Activity',
  GetLiveList: 'user/get_live_list',
  SetLiveRegister: 'user/set_live_register',
  QQServiceNum: 'user/serviceQQ',
  ServicePhone: 'user/getserviceqq',
  ReceiveActivity: 'user/receiveActivity',
  GetMyTurnConfig: 'xs/minigame/GetMyTurnConfig',
  GetOtherTurnConfig: 'xs/minigame/GetTurnConfig',
  SetTurnConfig: 'xs/minigame/SetTurnConfig',
  TurntableStart: 'xs/minigame/Rotate',
  QunCount: '/xs/discovery/QunCount',
  VCGroupCount:'/xs/message/VCGroupCount'
};

web.pages = {
  GetPhoneChargePage:'http://test.game.xiaoyaoai.cn:30081/static/YfbWebApp/pages/setting/tollCollection.html',
  PrePaidPage:"http://test.game.xiaoyaoai.cn:30081/static/YfbWebApp/pages/prepaid/prepaid.html",
  Novel: 'http://api2.app.yuanfenba.net/novel/index',
  live_download_url_ios: 'https://itunes.apple.com/cn/app/小友直播/id1218187039?mt=8',
  live_download_url_android: 'https://itunes.apple.com/cn/app/小友直播/id1218187039?mt=8',
};

web.liveConfig = {
  ios: {
    url: "https://itunes.apple.com/cn/app/id1218187039?mt=8",
    package:  "com.xiaoyou.juxin",
    entrance:  "xiaoyoo"
  },
  android: {
    url: "http://123.59.187.32/other/XiaoYouYuLe.apk",
    package:  "com.juxin.xiaoyoulive",
    entrance:  "com.juxin.xiaoyoulive.ui.WelcomeActivity"
  }
}

