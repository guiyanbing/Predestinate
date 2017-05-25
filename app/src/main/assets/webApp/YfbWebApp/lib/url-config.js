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
  QQServiceNum: 'user/serviceQQ',
  ReceiveActivity: 'user/receiveActivity'
};

web.pages = {
  GetPhoneChargePage:'http://test.game.xiaoyaoai.cn:30081/static/YfbWebApp/pages/setting/tollCollection.html',
  PrePaidPage:"http://test.game.xiaoyaoai.cn:30081/static/YfbWebApp/pages/prepaid/prepaid.html",
  Novel: 'http://api2.app.yuanfenba.net/novel/index',
};

