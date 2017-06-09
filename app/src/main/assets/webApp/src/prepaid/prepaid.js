/**
 * Created by long on 2017/5/3.
 */

var PrePaid = function () {
  var that = {};
  var _eventType = '';
  var _price = null;

  const PAGES = {
    yCoin: 1,
    vip: 2
  };
  var _currentPage = PAGES.yCoin;
  const CARD_TYPES = {
    yCoin50: 'yCoin50',
    yCoin100: 'yCoin100',
    vipMonth: 'vipMonth',
    vipQuarter: 'vipQuarter'
  };
  var _yCoinId = CARD_TYPES.yCoin100;
  var _vipId = CARD_TYPES.vipQuarter;
  const MSG_HEIGHT = 25;
  var _yCoin_limit_count = 3;
  var _vip_limit_count = 3;


  that.yCoinBtnClick = function (id) {
    if (_yCoinId === id) {
      return;
    }
    _yCoinId = id;
    _refreshBuyCard(PAGES.yCoin, _yCoinId);
  };

  that.vipBtnClick = function (id) {
    if (_vipId === id) {
      return;
    }
    _vipId = id;
    _refreshBuyCard(PAGES.vip, _vipId);
  };

  that.payBtnClick = function () {
    var payInfo = null;
    if (_currentPage === PAGES.yCoin) {
      _eventType =  'pay_y_btnljzf';
      if (_yCoinId === CARD_TYPES.yCoin100) {
        payInfo = defines.payInfo.yCoin100;
        _price = 100;
      } else {
        payInfo = defines.payInfo.yCoin50;
        _price = 50;
      }
    } else {
      _eventType = 'pay_vip_btnljzf'
      if (_vipId === CARD_TYPES.vipQuarter) {
        payInfo = defines.payInfo.vipQuarter;
        _price = 100;
      } else {
        payInfo = defines.payInfo.vipMonth;
        _price = 50;

      }
    }

    if (payInfo) {
      window.platform.startPay(payInfo.pay_id, parseInt(payInfo.payNum), payInfo.payName);
      window.platform.userBehavior('',_eventType,{price:_price});
    }

  };

  that.ChatBtnClick = function () {
    console.log('chat btn click ');
    window.platform.jumpToSmallSecretary();
    window.platform.userBehavior('','pay_zixun','');
  };

  var _lunbo = function (id, count) {
    var ul = $(id);
    var liFirst = ul.find('li:lt(' + count + ')');
    if (ul.find('li').length <= count) {
      //默认显示三条 总条数小于等于显示条不滚动
      return;
    }
    var clone = liFirst.clone();
    $(id).append(clone);
    $(id).animate({top: -count * MSG_HEIGHT+'px'}).animate({"top": 0}, 0, function () {
      liFirst.remove();
    })
  };

  var _refreshBuyCard = function (activePage, selectID) {
    switch (activePage) {
      case PAGES.yCoin:
        var y50 = document.getElementById(CARD_TYPES.yCoin50);
        var y100 = document.getElementById(CARD_TYPES.yCoin100);
        if (selectID === CARD_TYPES.yCoin50) {
          y50.querySelector('img').style.visibility = 'visible';
          y100.querySelector('img').style.visibility = 'hidden';
          y50.setAttribute("style", 'border: solid 1px #fd698c');
          y100.setAttribute("style", 'border: solid 1px #e8e8e8');
        } else {
          y50.querySelector('img').style.visibility = 'hidden';
          y100.querySelector('img').style.visibility = 'visible';
          y50.setAttribute("style", 'border: solid 1px #e8e8e8');
          y100.setAttribute("style", 'border: solid 1px #fd698c');
        }
        break;
      case PAGES.vip:
        var vipMonth = document.getElementById(CARD_TYPES.vipMonth);
        var vipQuarter = document.getElementById(CARD_TYPES.vipQuarter);
        if (selectID === CARD_TYPES.vipMonth) {
          vipMonth.querySelector('img').style.visibility = 'visible';
          vipQuarter.querySelector('img').style.visibility = 'hidden';
          vipMonth.setAttribute("style", 'border: solid 1px #fd698c');
          vipQuarter.setAttribute("style", 'border: solid 1px #e8e8e8');
        } else {
          vipMonth.querySelector('img').style.visibility = 'hidden';
          vipQuarter.querySelector('img').style.visibility = 'visible';
          vipMonth.setAttribute("style", 'border: solid 1px #e8e8e8');
          vipQuarter.setAttribute("style", 'border: solid 1px #fd698c');
        }
        break;
      default:
        break;
    }
  };

  var _getTimeRage = function (length) {
    var range = window.helper.randomInt(1, Math.round(length / 2));
    return range;
  };

  var _requestServerData = function () {
    window.platform.normalRequestNoUrl("Get", web.urlType.Php , web.urlMethod.BuyYCoinList, {}, {}, function (data) {
      if (data.status != 'ok') {
        return;
      }
      var ele = document.getElementById('buyer-lists1');
      var html = '';
      var list = data.res.list;
      var length = list.length;
      var count = _getTimeRage(length);
      var time = 1;
      for (var i = 0; i < length; i++) {
        var info = list[i];
        html += '<li><span>[' + info.nickname + ']</span>' + time + '分钟前购买了11000币，<span>获得100元话费</span></li>';
        if (--count <= 0) {
          count = _getTimeRage(length);
          time++;
        }
      }
      ;
      ele.innerHTML = html;
    });
    window.platform.normalRequestNoUrl("Get", web.urlType.Php ,web.urlMethod.BuyVIPList, {}, {}, function (data) {
      if (data.status != 'ok') {
        return;
      }
      var ele = document.getElementById('buyer-lists2');
      var html = '';
      var list = data.res.list;
      var length = list.length;
      var count = _getTimeRage(length);
      var time = 1;
      for (var i = 0; i < length; i++) {
        var info = list[i];
        html += '<li><span>[' + info.nickname + ']</span>' + time + '分钟前成功充值50元VIP</li>';
        if (--count <= 0) {
          time++;
          count = _getTimeRage(length);
        }
      }
      ;
      ele.innerHTML = html;
    });
  };

  var _initTotalPayData = function () {
    var yCoinCount = window.helper.getQueryString('ycoin_person') || 101010;
    var vipCount = window.helper.getQueryString('vip_person') || 101010;
    var documents = document.querySelectorAll('.buy-people-title');
    documents[0].innerHTML = '已购买Y币用户： <span style="color: #ee763a;">' + yCoinCount + '</span>人';
    documents[1].innerHTML = '已开通VIP用户： <span style="color: #ee763a;">' + vipCount + '</span>人';
  };

  var _intervalId = -1;
  var _scrollInfoList = function () {
    if (_intervalId != -1) {
      clearInterval(_intervalId);
    }
    var key = '#buyer-lists' + _currentPage;
    console.log('lunbo key ' + key);
    var msg_count = _currentPage === PAGES.vip ? _vip_limit_count : _yCoin_limit_count;
    _intervalId = setInterval(function () {
      _lunbo(key, msg_count);
    }, 3000);
  };

  var _initBuyInfoList = function () {
    var yLunbo = document.getElementById('lunboYCoin');
    var vipLunbo = document.getElementById('lunboVip');
    var slider = document.getElementById('slider');
    var scrolls = document.querySelectorAll('.mui-scroll');
    var topHeight = document.getElementById('sliderSegmentedControl').offsetHeight;
    console.log('height ', slider.offsetHeight, scrolls[0].offsetHeight, scrolls[1].offsetHeight);
    var bottom = 25;
    var yHeight = slider.offsetHeight - scrolls[0].offsetHeight - topHeight - bottom ;
    var vipHeight = slider.offsetHeight - scrolls[1].offsetHeight - topHeight - bottom  ;
    yLunbo.setAttribute('style', 'height:' + yHeight + 'px');
    vipLunbo.setAttribute('style', 'height:' + vipHeight + 'px');
    _yCoin_limit_count = Math.floor(yHeight / MSG_HEIGHT);
    _vip_limit_count = Math.floor(vipHeight / MSG_HEIGHT);
    console.log('cell count', _yCoin_limit_count, _vip_limit_count);
  };
  var _getService = function () {
      window.platform.normalRequestNoUrl("Get",web.urlType.Php , web.urlMethod.ServicePhone, {}, {},function (data) {
          console.log('service'+JSON.stringify(data));
          var telPhoneY = document.querySelector('#tel-service-y');
          var telPhoneVip = document.querySelector('#tel-service-vip');
          if(data.result !== 'success'){
              return;
          }
          servicePhone = data.tel;
          telPhoneY.innerHTML ='客服电话' + servicePhone;
          telPhoneVip.innerHTML ='客服电话' + servicePhone;
      })
  }
  var _getYcoinSurplus = function () {
      var ycoinSurplus = window.helper.getQueryString('ycoin_surplus') || 0;
      var currentY = document.querySelector('#ycoin-surplus');
      currentY.innerHTML = 'Y币余额：'+ ycoinSurplus;
      console.log('yyy='+ycoinSurplus);
  }
  that.getFare= function () {
      window.platform.appSlideView(503);
  }
  that.getYcoin = function () {
    mui('.mui-slider').slider().gotoItem(0, 0);
  };

  that.init = function (vipType) {
    mui.init();
    mui('.mui-scroll-wrapper').scroll();
    _requestServerData();
    _initBuyInfoList();
    _scrollInfoList();
    _initTotalPayData();
    _getService();
    _getYcoinSurplus();
    console.log('vip type ', vipType);
    if (vipType === 1) {
      // vip页面特殊处理
      setTimeout(function () {
        //bug 延时确保mui page页面初始化完成
        mui('.mui-slider').slider().gotoItem(1, 0);
        // setTimeout(function () {
        document.body.style.visibility = 'visible';
        _refreshBuyCard(PAGES.yCoin, CARD_TYPES.yCoin100);
        _refreshBuyCard(PAGES.vip, CARD_TYPES.vipQuarter);
        // }, 50);

        // document.getElementById('BuyVIP').classList.add(mui.className('active'));
      }, 300);
    } else {
      _refreshBuyCard(PAGES.yCoin, CARD_TYPES.yCoin100);
      _refreshBuyCard(PAGES.vip, CARD_TYPES.vipQuarter);
    }
    ;

  };

  document.getElementById('slider').addEventListener('slide', function (e) {
    if (e.detail.slideNumber === 0) {
      _currentPage = PAGES.yCoin;
    } else if (e.detail.slideNumber === 1) {
      _currentPage = PAGES.vip;
    }
    _scrollInfoList();
  });

  return that;
};



