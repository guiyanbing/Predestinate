/**
 * Created by yinhua on 2017/5/2.
 */
var Ranking = Ranking || (function ($) {
    var that = {};
    var _typeUrl = '';
    var _w = '';
    var _tabIndex = 0;
    const _weekRank = {
      now: 101,
      last: 102
    }
    var _listener = window.Eventuality({});
    window.platform.clientHandler = _listener;

    var _loadingFlag = false;
    var _showLoading = function (visible) {
      if ((_loadingFlag && visible) || (!_loadingFlag && !visible)) {
        return;
      }
      _loadingFlag = visible;
      window.platform.showLoading(visible);
    };

    $.ready(function () {
      //循环初始化所有下拉刷新，上拉加载。
      $('.mui-scroll-wrapper').scroll();
      var weekType = _weekRank.now;
      _listener.on('onWatchCommand', function (jcmd, data) {
        console.log(' test456 ', jcmd, JSON.stringify(data));
        if (jcmd !== 'ranking_btn_click') {
          return;
        }
        var windWeekType = data.type;
        if(windWeekType === weekType){
          return;
        }
        weekType = windWeekType;
        switch (windWeekType) {
          case _weekRank.now:
            _w = '';
            break;
          case _weekRank.last:
            _w = 1;
            break;
          default:
            break;
        }
        _getWindRankData();
      });

      document.getElementById('slider').addEventListener('slide', function (e) {

        if (e.detail.slideNumber === 0) {
          _tabIndex = 0;
        } else if (e.detail.slideNumber === 1) {
          _tabIndex = 1;

        }
        _getWindRankData();
      });
      var _currentRefreshAPI = null;
      var _resetPageRefresh = function () {
        var ele = document.getElementById('scroll' + (_tabIndex + 1));
        _currentRefreshAPI = $(ele).pullRefresh({
          down: {
            callback: function () {
              var self = this;
              _getWindRankData();
              self.endPulldownToRefresh();
            }
          }
        });
      };

      var _firstThreeHtml = function (dataList) {
        var html = '<div class="mui-row" style="padding-bottom:0;">';
        var firstThree = [2, 1, 3];
        var giftsText = '';
        if (_tabIndex === 0) {
          giftsText = '收到'
        } else if (_tabIndex === 1) {
          giftsText = '送出'
        }
        for (var i = 0; i < firstThree.length; i++) {
          var data = dataList[firstThree[i] - 1];
          html += '<div class="mui-col-sm-4">\
                    <li style="padding:11px 15px;padding-top:20px;width:100%;height:100%;position:relative;">\
                        <div>\
                        <img class="avatar"  src="' + data.avatar + '"/>\
                        <img class="avatar-flower"  src="../../assets/one_0' + firstThree[i] + '.png" alt=""/>\
                        <span>第' + firstThree[i] + '名</span>\
                        </div>\
                        <p class="nickLimit" style="margin-bottom:0;margin-top:4px;font-size: 14px; color:#000;">' + data.nickname + '</p>\
                        <p class="giftLimit">' + giftsText + ':' + data.num + '个礼物</p>\
                        </li>\
                        </div>';
        }
        html += '</div><ul class="mui-table-view windRanking-fourth' + (_tabIndex + 1) + '" style="padding:0 15px !important;"></ul>';
        return html;
      };

      var _listsHtml = function (data, index) {
        var html = '';
        var giftsText = '';
        if (_tabIndex === 0) {
          giftsText = '收到'
        } else if (_tabIndex === 1) {
          giftsText = '送出'
        }
        html += '<li class="mui-table-view-cell">\
                    <span style="display: inline-block;float:left;min-width:20px;line-height: 42px;">' + (index + 1) + '</span>\
                    <div style="position: relative;">\
                    <img class="mui-media-object mui-pull-left" src="' + data.avatar + '">\
                    </div>\
                    <div class="mui-media-body" style="height:42px;line-height: 42px; font-size: 16px;"><span class="nickLimit" style="width:45%;line-height: 42px; display: inline-block;">' + data.nickname + '</span>\
                    <span style="text-align: center;float:right;color:#ccc;line-height:42px;font-size: 12px;">' + giftsText + ':' + data.num + '个礼物</span>\
                    </div>\
                    </li>';
        return html;
      };
      var _selfNumHtml = function (mynum) {
        var html = '';
        if (_tabIndex === 0) {
          html += '收到:' + mynum + '个礼物';
        } else if (_tabIndex === 1) {
          html += '送出:' + mynum + '个礼物';
        }
        console.log('html=' + html);

        return html;
      };
      var _noDataHtml = function () {
        var html = '';
        html += '<img src="../../assets/nodata.png" alt="">\
                    <p>哎呀呀，暂时没有数据哦！</p>';
        return html;
      }


      var _firstThree = function (data) {
        var div = document.body.querySelector('#image' + (_tabIndex + 1));
        if (data.length === 0) {
          div.style.display = 'none';
          return;
        }
        div.style.display = 'block';
        div.innerHTML = _firstThreeHtml(data);
      };

      var _listsData = function (dataList) {
        console.log('test 123', _tabIndex, JSON.stringify(dataList));
        var scroll = document.getElementById('scroll' + (_tabIndex + 1));
        var ul = scroll.querySelector('.mui-scroll .ranking-list');
        var html = '';
        for (var i = 3; i < dataList.length; i++) {
          var data = dataList[i];
          html += _listsHtml(data, i)
        }
        ul.innerHTML = html;
        if (dataList.length === 0) {
          _noData('block');
        } else {
          _noData('none');
        }
      };

      var _selfNumData = function (data) {
        var bottomSelf = document.querySelector('#bottom');
        bottomSelf.innerHTML = _selfNumHtml(data.mynum);
        if (data.list.length === 0) {
          bottomSelf.style.display = 'none';
        } else {
          bottomSelf.style.display = 'block';
        }
      }

      var _noData = function (display) {
        var div = document.querySelectorAll('.no-data')[_tabIndex];
        div.style.display = display;
        var listPos = document.querySelector('#scroll' + (_tabIndex + 1) + '');
        if (display === 'block') {
          listPos.classList.add('listPosBox');
        } else if (display === 'none') {
          listPos.classList.remove('listPosBox');
        }
        div.innerHTML = _noDataHtml();
      }

      var  _getWindRankData = function (cb) {
        _resetPageRefresh();
        _rankRequest(function (data) {
          console.log('top three data : = ' + JSON.stringify(data));
          if (!data.list) {
            data.list = [];
          }
          _firstThree(data.list);
          _listsData(data.list);
          _selfNumData(data);
          if (cb) {
            cb();
          }
          if (_currentRefreshAPI) {
            _currentRefreshAPI.scrollTo(0, 0, 0);
          }
        });
      }
      var _rankRequest = function (cb) {
        /*var list = [];
        var length = 5 + 10 * _tabIndex;
         for (var i = 0; i < length; i++) {
         var data = {
         nickname: '昵称昵称昵称昵称昵称' + (i+1),
         avatar: '',
         num: window.helper.randomInt(1, 20)
         }
         list.push(data);
         }
         cb({list: list, mynum: 1});
         return;*/
        _showLoading(true);
        window.platform.getUserSelfDetail(function (data) {
            var params = {
                x: data.longitude,
                y: data.latitude,
                w: _w
            }
            if (_tabIndex == 0) {
                _typeUrl = 'MostGetList'
            } else {
                _typeUrl = 'MostSendList'
            }
            window.platform.normalRequestNoUrl("Get", web.urlType.Php, web.urlMethod[_typeUrl], params, {}, function (resp) {
                _showLoading(false);
                if (resp.result === 'error') {
                    mui.toast(resp.content);
                    return;
                }
                if (cb) {
                    cb(resp);
                }
            });
        });

      };
      _getWindRankData();
    });
    return that;
  })(mui);