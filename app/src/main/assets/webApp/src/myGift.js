/**
 * Created by bikuili on 17/5/2.
 */
var MyGift = MyGift || (function ($) {
    var that = {};

    //阻尼系数
    var deceleration = mui.os.ios ? 0.003 : 0.0009;
    $('.mui-scroll-wrapper').scroll({
      bounce: false,
      indicators: true, //是否显示滚动条
      deceleration: deceleration
    });
    $.ready(function () {
      var tabindex = 0;
      var dataUrl = '';
      //循环初始化所有下拉刷新，上拉加载。
      $.each(document.querySelectorAll('.mui-slider-group .mui-scroll'), function (index, pullRefreshEl) {

        $(pullRefreshEl).pullToRefresh({
          // down: {
          //   callback: function () {
          //     var self = this;
          //     setTimeout(function () {
          //       var ul = self.element.querySelector('.mui-table-view');
          //       ul.insertBefore(createFragment(ul, index, 10, true), ul.firstChild);
          //       self.endPullDownToRefresh();
          //     }, 1000);
          //   }
          // },
          up: {
            auto: true,
            contentinit: '上拉刷新',
            contentdown: '上拉刷新',
            callback: function () {
              var self = this;
              setTimeout(function () {
                // var ul = self.element.querySelector('.mui-table-view');
                // ul.appendChild(createFragment(ul, index, 5));
                getData(tabindex);
                self.endPullUpToRefresh();
              }, 1000);
            }
          }
        });
      });

      //监听tab切换
      document.getElementById('slider').addEventListener('slide', function(e) {
        if (e.detail.slideNumber === 0) {
          console.log('收到礼物列表');
          tabindex = 0;
          console.log('tabindex' + tabindex);
          getData(tabindex);
        } else if (e.detail.slideNumber === 1) {
          console.log('送出礼物列表');
          tabindex = 1;
          getData(tabindex);
        }
      });
      //获取数据
      var getData = function (tabindex) {
        console.log('tabindex' + tabindex);
        if(tabindex == 0){
          dataUrl = web.urlMethod.ReceiveGifts;
          console.log('Rgifturl =' + dataUrl)
        }else if (tabindex == 1){
          dataUrl = web.urlMethod.MySendGifts;
          console.log('Sgifturl =' + dataUrl)
        };
        window.platform.normalRequestNoUrl('Get',web.urlType.Php,dataUrl,{},{},function (resp) {
          if (resp.status !== 'ok') {
            mui.toast(resp.msg);
          } else {
            console.log('giftData' + JSON.stringify(resp));
            render(tabindex,resp);
          }
        });
      };

      var render = function (tabindex,data) {
        console.log('rendertab' + tabindex);
        var _dataList = data.list;
        var _giftNum = data.num;
        console.log('_giftnum ='+_giftNum);
        var giftRecieveTotal = document.getElementById('giftRecieveTotal');
        var giftSendTotal = document.getElementById('giftSendTotal');
        giftRecieveTotal.innerText = _giftNum;
        giftSendTotal.innerText = _giftNum;
        var table=null;
        var cells=null;
        if (tabindex === 0) {
           table = document.body.querySelector('#scroll1 .mui-scroll .mui-table-view');
           cells = table.querySelectorAll('.padding-box');
        }else {
           table = document.body.querySelector('#scroll2 .mui-scroll .mui-table-view');
           cells = table.querySelectorAll('.padding-box');
        }

        for (var j = 0;j <cells.length;j++) {
          table.removeChild(cells[j]);
        }
        for (var i = 0;i < _dataList.length;i++) {
          var data = _dataList[i];
          var name = data.name;
          var img = data.img;
          var num = data.num;
          var li = document.createElement('li');
          li.className = 'padding-box mui-media';
          li.innerHTML = '<a href="#">\
            <img class="mui-media-object mui-pull-left" src="'+img+'" alt="">\
            <div class="mui-media-body" style="line-height: 46px">'+ name +'<p class="mui-ellipsis mui-pull-right">X'+  num+'</p>\</div></a>';
          table.appendChild(li);
        };

      };

      getData(tabindex);
    });

    return that;
  })(mui);
