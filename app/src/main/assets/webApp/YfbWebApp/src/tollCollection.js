/**
 * Created by bikuili on 17/5/5.
 */
var tollCollection = tollCollection || (function () {
    let that = {};
    let clock = null;
    let nums = 60;
    let btn;
    let _serverReqFlag = false;
    let _mobileNum = null;
    that.loadPage = function () {
      btn = document.getElementById('clibtn');
      window.platform.getBindPhoneNum(function (resp) {
        console.log('resp' + JSON.stringify(resp));
        if (resp.num != '') {
          var phoneNumber = document.getElementById('phoneNum');
          phoneNumber.value = resp.num;
        }
        _mobileNum = resp.num;
        console.log('mobileNum' + _mobileNum);
      });

    };
    //验证手机号码格式
    that.checkPhoneNum = function () {
      var phoneNum = document.getElementById('phoneNum').value;
      if (!(/^1[34578]\d{9}$/.test(phoneNum))) {
        mui.toast('手机号错误', {duration: 'short', type: 'div'});
        return false;
      }
      return phoneNum;
    };
    //验证码
    that.checkVerifyCode = function () {
      var vCode = document.getElementById('msCode').value;
      if (!vCode) {
        mui.toast('验证码不能为空');
        return false;
      }
      return vCode;
    };
    const _doLoop = function () {
      nums--;
      if (nums > 0) {
        btn.value = nums + 's';
      } else {
        clearInterval(clock);
        btn.disabled = false;
        btn.value = '立即领取';
        nums = 60;
      }
    };
    //验证码倒计时
    that.sendCode = function () {
      // if (_serverReqFlag) {
      //   return;
      // }
      let phoneNum = that.checkPhoneNum();
      if (!phoneNum) {
        return;
      }
      phoneNum = parseInt(phoneNum);
      console.log(phoneNum);
      var params = {
        cellPhone: phoneNum,
        type: 1
      };
      window.platform.normalRequest("Get", web.urlConfig.agentURL + web.urlMethod.GetPhoneSMS, params, {}, function (resp) {
        console.log('验证码resp' + JSON.stringify(resp))
        if (resp.result == 'error' || resp.respCode === 'error') {
          mui.toast("请求异常，稍后重试");
          return;
        };
        var IdCode = document.getElementById('IdCode');
        IdCode.style.display = "block";
        btn = document.getElementById('clibtn');
        btn.disabled = true;
        btn.value = nums + 's';
        clock = setInterval(_doLoop, 1000);
      });
    };
    that.buyNow = function () {
      var url = web.pages.PrePaidPage;
      window.platform.openWeb('立即购买', url);
    };
    //按钮动作
    that.getPhoneCash = function () {
      let phoneNum = that.checkPhoneNum();
      if (_mobileNum == 'null') {
        if (!phoneNum) {
          return;
        }
      };
      if (_mobileNum === '') {
        if (!phoneNum) {
          return;
        }
        that.sendCode();
      } else {
        that.getCash();
      }
    };
    //验证手机号
    that.checkMobile = function () {
      if (_serverReqFlag) {
        return;
      }
      ;
      let phoneNum = that.checkPhoneNum();
      if (!phoneNum) {
        return;
      };
      let vCode = that.checkVerifyCode();
      if (!vCode) {
        return;
      }
      var params = {
        cellPhone: phoneNum,
        verifyCode: vCode
      }
      window.platform.normalRequest('Get', web.urlConfig.agentURL + web.urlMethod.CheckPhoneNum, params, {}, function (resp) {
        console.log('手机验证resp' + JSON.stringify(resp));
        if (resp.result == 'error' || resp.respCode === 'error') {
          mui.toast('验证失败，请重试');
          return;
        };
        window.platform.refreshUserDetail()
        _mobileNum = phoneNum;
        console.log('手机验证成功');
        mui.toast('手机验证成功')
      });
    };
    //获取客服qq
    that.getServiceQQ = function () {
      window.platform.normalRequest('Get', web.urlConfig.agentURL + web.urlMethod.QQServiceNum, {}, {}, function (resp) {
        var qqService = document.getElementById('qqService');
        qqService.innerHTML = resp.content
        console.log('获取客服qq' + JSON.stringify(resp));
      });
    };
    //话费领取记录
    that.phoneCashRecord = function () {
      window.platform.safeRequest('Get', web.urlConfig.agentURL + web.urlMethod.GetActivity, {"id": 1}, {}, function (resp) {
        console.log('领取话费记录' + JSON.stringify(resp));
        if (resp.item == '') {
          console.log('啥也没有啊');
          var noneList = document.getElementById('noneList');
          noneList.style.display = 'block';
        }else{
          let dataList = resp.item;
          for (var i = 0;i < dataList.length;i++) {
            let name = dataList[i].name;
            let date = dataList[i].date;
            let status = dataList[i].status;
            let li = document.createElement('li');
            li.className = 'mui-table-view-cell mui-row';
            li.innerHTML = '<span class="mui-col-xs-4 mui-col-sm-4">'+name+'</span>\
                            <span class="mui-col-xs-4 mui-col-sm-4">'+date+'</span>\
                            <span class="mui-col-xs-4 mui-col-sm-4">'+status+'</span>';
            var table = document.body.querySelector('.mui-scroll .mui-table-view');
            table.appendChild(li);
          }
        }
      });
    };
    //领取话费
    that.getCash = function () {
      window.platform.normalRequest('Get', web.urlConfig.agentURL + web.urlMethod.ReceiveActivity, {}, {}, function (resp) {
        console.log('领取话费resp'+JSON.stringify(resp));
        if (resp.result === 'error') {
          mui.alert(resp.content, '', '确定');
        }else {
          mui.alert('已提交领取话费申请，请耐心等待', '', '确定');
        }
      });
    };
    that.loadPage();
    that.getServiceQQ();
    that.phoneCashRecord();
    // that.checkMobile();
    return that;
  })();
