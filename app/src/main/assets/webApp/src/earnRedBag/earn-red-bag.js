/**
 * Created by long on 2017/6/20.
 */

var EarnRedBag = function () {
  var that = {};

  that.init = function () {
    console.log('earn red bag ');
    window.platform.safeRequestNoUrl("Get", web.urlType.Go, web.urlMethod.QunCount, {} , {}, function (resp) {
      if (resp.status !== 'ok') {
        return;
      }
      document.getElementById('askItem').innerHTML = resp.res.count + '人';
    });
    window.platform.safeRequestNoUrl("Get", web.urlType.Go, web.urlMethod.VCGroupCount, {} , {}, function (resp) {
      if (resp.status !== 'ok') {
        return;
      }
      document.getElementById('videoItem').innerHTML = resp.res.video_quncount + '人';
      document.getElementById('voiceItem').innerHTML = resp.res.audio_quncount + '人';
    });
    window.platform.getUserSelfDetail(function (data) {
      document.getElementById('videoPrice').innerHTML = data.videoPrice + '钻石/分钟';
      document.getElementById('voicePrice').innerHTML = data.voicePrice + '钻石/分钟';
    });

  };

  that.askForGift = function () {
    window.platform.askForGift('normal');
  };

  that.videoBtnClick = function () {
    window.platform.appSlideView(508);
  };

  that.setTurntable = function () {
    window.platform.appSlideView(507);
  };

  that.voiceBtnClick = function () {
    window.platform.appSlideView(509);
  };

  return that;
};