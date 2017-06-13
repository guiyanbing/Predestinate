/**
 * Created by yinhua on 2017/5/27.
 */
var Turntable = Turntable || (function () {
    var that = {};
    var valueChange = false;
    var _spec_list = ["唱首歌", "跳个舞", "撒个娇", "送我礼物", "卖个萌", "么么哒", "送我礼物", "学猫叫", "做鬼脸", "重来一次", "私密问题", "送我礼物"];
    var _listener = window.Eventuality({});
    var _inputs = [];
    window.platform.clientHandler = _listener;
    window.platform.showLoading(true);
    window.platform.safeRequestNoUrl("Post", web.urlType.Go, web.urlMethod.GetMyTurnConfig,{}, {}, function (resp) {
        window.platform.showLoading(false);
        if(resp.status !== "ok"){
            return;
        }
        if(resp.res.configs !== null){
            _spec_list = resp.res.configs;
        }
        _specListHtml();
    });
    _listener.on('onWatchCommand',function (jcmd, data) {
        console.log('test'+jcmd,JSON.stringify(data));
        if(jcmd !== "header_right_btn_click"){
            return;
        }
        if(!_checkSaveValue()){
            return;
        }
      console.log('save list ' + _spec_list)
      window.platform.showLoading(true);
        window.platform.safeRequestNoUrl("Post", web.urlType.Go, web.urlMethod.SetTurnConfig,{}, {configs:_spec_list}, function (resp) {
          window.platform.showLoading(false);
            if(resp.status !== "ok"){
                return;
            }
            window.platform.userBehavior('','menu_me_redpackage_dzp_save',{items:_spec_list});
            valueChange = false;
            mui.toast('数据保存成功!');
            console.log('testSet'+JSON.stringify(resp));
        });
    })
    that.setValue = function () {
        _listener.fire('onWatchCommand','header_right_btn_click',{});
    }
    var _specListHtml = function () {
        var html = '';
        var giftImg = '';
        var triangleImg = '';
        var disabled = '';
        var cell = '';
        var div = document.querySelector('.special-list');
        for (var i = 0; i < _spec_list.length; i++) {
            if (_spec_list[i] == "送我礼物") {
                giftImg = '<img class="gift-ico" src="../../assets/gift_img.png" alt="gift">';
                triangleImg = '<img class="triangle-ico" src="../../assets/bj_ico.png" alt="">';
                disabled = 'disabled';
                cell = '<button class="getGiftBtn" onclick="Turntable.disClick();">送我礼物</button>';
            } else {
                giftImg = '';
                triangleImg = '';
                disabled = '';
                cell = '<input id=' + i + ' type="text" ' + disabled + ' required="required" maxlength="4" placeholder="' + _spec_list[i] + '">';
            }
            var rank = i + 1;
            rank = rank < 10 ? ("0" + rank) : rank;
            html += '<div class="mui-col-sm-6 mui-col-xs-6" style="padding-right: 20px;">\
                     <li class="mui-table-view-cell dark-gray" style="text-align:center;">\
                     <div class="mui-input-row" style="padding-right:0;">\
                     <label>' + rank + '</label>\
                     ' + cell + '\
                     ' + giftImg + '\
                     </div>\
                     </li>\
                     ' + triangleImg + '\
                     </div>';
        }
        div.innerHTML = html;
        that.checkValue();
    };
    var checkIsChinese = function (str) {
        for (var i = 0; i < str.length; i++) {
            if (str.charCodeAt(i) <= 255) {
                return false;
            }
        }
        return true;
    }
    that.disClick = function () {
      //  window.platform.showLoading(true);
        mui.toast('送我礼物不可修改');
    };
    // setTimeout(function () {
    //   var data = {
    //     jcmd: 'header_right_btn_click',
    //     data : {
    //
    //     }
    //   };
    //   window.platform.appCommand(JSON.stringify(data));
    // }, 10 * 1000);

    var _checkSaveValue = function () {
      for (var i = 0; i <  _inputs.length; i++) {
        if (!_checkEndValue(_inputs[i])) {
            return false;
            break;
        }
      }
      if (!valueChange) {
        mui.toast('数据没有更改!');
          return false;
      }
      return true;
    };
    
    var _checkEndValue = function (input) {
      var currValue = input.value;
      if (!currValue || currValue.trim() == "" || !checkIsChinese(currValue)) {
        input.value = input.getAttribute('placeholder');
        mui.toast('输入内容错误');
        return false;
      } else if (currValue.length < 2) {
        mui.toast('至少输入两个汉字');
        input.value = input.getAttribute('placeholder');
        return false;
      } else {
        var index = input.getAttribute('id');
       console.log('当前' , input.value , input.getAttribute('id'));
       if (_spec_list[index] !== input.value) {
         _spec_list[index] = input.value;
         console.log('hhh',_spec_list[index]);
         valueChange = true;
       }
        return true;
      }
    }
    that.checkValue = function () {
      _inputs = document.querySelectorAll(".mui-input-row input");
      console.log('inputs ', _inputs.length);
        for (var i = 0; i < _inputs.length; i++) {
          var index = _inputs[i].getAttribute('id');
          _inputs[i].value = _spec_list[index];
          _inputs[i].onchange = function () {
                var self = this;
                _checkEndValue(self);
            };
        }
    };
    // _specListHtml();
    return that;
})();



























