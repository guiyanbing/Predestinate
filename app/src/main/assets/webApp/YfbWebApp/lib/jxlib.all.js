/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;
/******/
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// identity function for calling harmony imports with the correct context
/******/ 	__webpack_require__.i = function(value) { return value; };
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, {
/******/ 				configurable: false,
/******/ 				enumerable: true,
/******/ 				get: getter
/******/ 			});
/******/ 		}
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 5);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _helper = __webpack_require__(3);

var _helper2 = _interopRequireDefault(_helper);

var _eventuality = __webpack_require__(2);

var _eventuality2 = _interopRequireDefault(_eventuality);

var _platformHelper = __webpack_require__(1);

var _platformHelper2 = _interopRequireDefault(_platformHelper);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/***/ }),
/* 1 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _urlUtility = __webpack_require__(4);

var _urlUtility2 = _interopRequireDefault(_urlUtility);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var CMD = {
  loadEnd: 'hide_loading',
  playSound: 'play_sound',
  endAll: 'do_finish',
  getUserInfo: 'get_app_data',
  getUserSelfInfo: 'get_user_detail',
  getAppUserInfo: 'get_user_info',
  getDeviceModel: 'getdevicemodel',
  safeRequest: 'safe_request',
  normalRequest: 'normal_request',
  playShock: 'play_shock',
  showAppDialog: 'show_dialog',
  jumpAppChat: "jump_to_chat",
  showAppSlide: "jump_to_app",
  changeAppTitle: "change_title",
  jumpToUserInfo: 'jump_to_userinfo',
  startPay: 'start_pay',
  openGameWeb: 'open_game_web',
  getImageData: 'get_image_data',
  imageDataToURl: 'image_data_to_url',
  getIdentifyStatus: 'get_identify_status',
  jumpToQQService: 'open_qq_service',
  jumpToActivity: 'jump_to_activity',
  getBindPhoneNum: 'get_phone_number',
  refreshUserDetail: 'refresh_userdetail',
  showLoading: 'show_data_loading',
  hideLoading: 'hide_data_loading',
  getAgentUrl: 'get_agent_url',
  userBehavior: 'user_behavior'
}; /**
    * Created by chuhaoyuan on 2016/11/2.
    */
//import AsyncRequest from '../utility/async-request'

var JCMD = {
  visit_other: 'visitOther'
};

var OSType = {
  iOS: 1,
  Android: 2,
  Web: 3,
  unknown: 4
};

var _getOperatingSystem = function _getOperatingSystem() {
  //return OSType.Web;
  var userAgent = navigator.userAgent || navigator.vendor || window.opera;
  if (userAgent.match(/iPad/i) || userAgent.match(/iPhone/i) || userAgent.match(/iPad/i)) {
    return OSType.iOS;
  } else if (userAgent.match(/Android/i)) {
    return OSType.Android;
  } else {
    return OSType.unknown;
  }
};

var int2index = function int2index(val) {
  return val + "";
};

var PlatformHelper = function PlatformHelper() {
  var that = {
    clientHandler: null
  };

  //todo(liuyuchen): tmp code _uid!!
  var _userData = null;

  var _osVer = _getOperatingSystem();
  that.osVer = _osVer;
  // that.osVer = 2;
  console.log('os sys:' + _osVer);

  var _sendCMD = function _sendCMD(body) {
    var content = JSON.stringify(body);
    //console.log('send cmd' + content);
    switch (_osVer) {
      case OSType.iOS:
        console.log('window.webkit.messageHandlers.webViewApp.postMessage: ' + content);
        window.webkit.messageHandlers.webViewApp.postMessage(content);
        break;
      case OSType.Android:
        console.log('command: ' + content);
        window.Android.command(content);
        break;
      default:
        window.alert('no cmd handler' + content);
        break;
    }
  };

  var _callBackIndex = 0;
  var _callbacks = {};
  var _handleResp = function _handleResp(dataString) {
    // let str = dataString.replace(/\\/g,"\\\\");
    // return JSON.parse(str);
    return JSON.parse(dataString);
  };

  var _executeCMD = function _executeCMD(cmd, data, cb) {
    console.log('run cmd: ' + cmd + ' with: ' + JSON.stringify(data));
    if (cb) {
      _callBackIndex++;
      _callbacks[int2index(_callBackIndex)] = cb;

      if (data == undefined) {
        data = {};
      }
      data.callbackName = 'window.platform.appResponse';
      data.callbackID = int2index(_callBackIndex);
    }

    var cmdData = {
      cmd: cmd,
      data: data
    };

    _sendCMD(cmdData);
  };

  that.loadEnd = function () {
    _executeCMD(CMD.loadEnd);
  };

  that.refreshUserDetail = function () {
    _executeCMD(CMD.refreshUserDetail);
  };

  that.playMusic = function (game, url) {};

  that.playSound = function (game, url) {
    console.log('play sound' + url);
    _executeCMD(CMD.playSound, {
      game: game,
      url: url
    });
  };

  that.getOsType = function () {
    if (_osVer === OSType.iOS) {
      return 'iOS';
    } else if (_osVer === OSType.Android) {
      return 'Android';
    }
    return 'Unknown';
  };

  that.endAll = function () {
    console.log('endAll');
    _executeCMD(CMD.endAll);
  };

  that.getUserInfo = function (cb) {
    ////获取 用户uid 跟auth

    _executeCMD(CMD.getUserInfo, null, function (resp) {
      console.log("uid: " + resp.uid + "\n auth: " + resp.auth);
      _userData = {
        uid: parseInt(resp.uid),
        auth: resp.auth,
        gender: resp.gender,
        isVip: resp.is_vip,
        userType: resp.user_type
      };
      cb(_userData);
    });
  };

  that.getUserSelfDetail = function (cb) {
    ////获取 用户数据

    _executeCMD(CMD.getUserSelfInfo, null, function (resp) {
      console.log("uid: " + resp.uid + "\n auth: " + resp.auth);
      _userData = {
        uid: parseInt(resp.uid),
        auth: resp.auth,
        gender: resp.gender,
        isVip: resp.is_vip,
        userType: resp.user_type,
        money: resp.money,
        latitude: resp.latitude,
        longitude: resp.longitude
      };
      cb(_userData);
    });
  };

  that.getUserDetail = function (uid, cb) {
    console.log('get other detail :' + uid);
    _executeCMD(CMD.getAppUserInfo, {
      uid: uid + ''
    }, function (resp) {
      if (resp.hasOwnProperty('avatar_status')) {
        console.log('user avatar status: ' + resp.avatar_status);
        //avatar_status头像状态：0 为未审核，1 为通过，2 为拒绝,3 未上传 4 好，5 很好 6 待复审 7 新版未审核
        if (resp.avatar_status !== 1 && resp.avatar_status !== 4 && resp.avatar_status !== 5) {
          //1审核通过 其他审核未通过
          resp.avatar = '';
        }
      }

      if (cb) {
        cb(resp);
      }
    });
  };

  that.getDeviceModel = function (cb) {
    _executeCMD(CMD.getDeviceModel, null, cb);
  };

  that.getBindPhoneNum = function (cb) {
    _executeCMD(CMD.getBindPhoneNum, null, cb);
  };

  that.getAgentUrl = function (urlType, cb) {
    _executeCMD(CMD.getAgentUrl, { type: urlType }, cb);
  };

  that.jumpToUserInfo = function (target_uid) {
    console.log('jump to userInfo', target_uid);
    _executeCMD(CMD.jumpToUserInfo, {
      target_uid: target_uid
    });
  };

  that.playShock = function () {
    _executeCMD(CMD.playShock);
  };

  that.jumpToQQService = function () {
    _executeCMD(CMD.jumpToQQService);
  };

  var _requestNative = function _requestNative(isSafe, method, url, params, body, cb) {
    var totalURL = url + _urlUtility2.default.ropeParamsString(params);
    var bodyString = JSON.stringify(body);

    var cmd = CMD.normalRequest;
    if (isSafe) {
      cmd = CMD.safeRequest;
    }

    _executeCMD(cmd, {
      method: method,
      url: totalURL,
      body: bodyString
    }, cb);
  };

  that.checkError = function (data) {
    if (data.timeout === 1) {
      if (that.clientHandler) {
        that.clientHandler.fire('onWatchCommand', 'time_out', data);
      }
    }
  };

  // //todo(yuchenl): async version
  // that.safeRequest = function (method, url, params, body) {
  //
  //   return new Promise(function (resolve, reject) {
  //     _requestNative(true, method, url, params, body, (resp)=>{
  //       that.checkError(resp);
  //       resolve(resp);
  //     });
  //   });
  // };

  //todo(yuchenl): sync version
  that.safeRequest = function (method, url, params, body, cb) {
    _requestNative(true, method, url, params, body, function (resp) {
      that.checkError(resp);
      cb(resp);
    });
  };

  that.appResponse = function (cbindex, repString) {
    console.log('app callbackID: ' + cbindex + '\n respString: ' + repString);
    var resp = _handleResp(repString);

    if (_callbacks[cbindex]) {
      console.log('play cb: ' + cbindex);
      _callbacks[cbindex].call(null, resp);
      delete _callbacks[cbindex];
    }
  };

  that.appCommand = function (dataStr) {
    var data = _handleResp(dataStr);
    console.log('addCommand:' + JSON.stringify(data));
    if (that.clientHandler) {
      that.clientHandler.fire('onWatchCommand', data.jcmd, data.data);
    }
  };

  // //todo(yuchenl): async version
  // that.normalRequest = function (method, url, params, body) {
  //   return new Promise(function (resolve, reject) {
  //     _requestNative(false, method, url, params, body, (resp)=> {
  //       that.checkError(resp);
  //       resolve(resp);
  //     });
  //   });
  // };

  //todo(yuchenl): sync version
  that.normalRequest = function (method, url, params, body, cb) {
    _requestNative(false, method, url, params, body, function (resp) {
      that.checkError(resp);
      cb(resp);
    });
  };

  that.requestUrlMaps = {};

  that.normalRequestNoUrl = function (method, urlType, urlMethod, params, body, cb) {
    var url = that.requestUrlMaps[urlType];
    if (url === undefined) {
      that.getAgentUrl(urlType, function (data) {
        that.requestUrlMaps[urlType] = data.url;
        that.normalRequest(method, data.url + urlMethod, params, body, cb);
      });
    } else {
      that.normalRequest(method, url + urlMethod, params, body, cb);
    }
  };

  that.safeRequestNoUrl = function (method, urlType, urlMethod, params, body, cb) {
    var url = that.requestUrlMaps[urlType];
    if (url === undefined) {
      that.getAgentUrl(urlType, function (data) {
        that.requestUrlMaps[urlType] = data.url;
        that.safeRequest(method, data.url + urlMethod, params, body, cb);
      });
    } else {
      that.safeRequest(method, url + urlMethod, params, body, cb);
    }
  };

  that.changeWindowTitle = function (title) {
    _executeCMD(CMD.changeAppTitle, {
      title: title
    });
  };

  that.appSlideView = function (index) {
    _executeCMD(CMD.showAppSlide, {
      code: index
    });
  };

  that.appJumpChat = function (target_uid) {
    _executeCMD(CMD.jumpAppChat, {
      target_uid: target_uid
    });
  };

  that.jumpToActivity = function (activity_id) {
    _executeCMD(CMD.jumpToActivity, {
      activity_id: activity_id
    });
  };

  that.startPay = function (payID, cost, desc) {
    _executeCMD(CMD.startPay, {
      pay_id: payID,
      money: cost,
      desc: desc
    });
  };

  that.openWeb = function (title, url) {
    _executeCMD(CMD.openGameWeb, {
      type: 1,
      url: url,
      title: title
    });
  };

  that.getImageData = function (cb) {
    _executeCMD(CMD.getImageData, null, cb);
  };

  that.showLoading = function (visible) {
    var key = visible ? CMD.showLoading : CMD.hideLoading;
    _executeCMD(key);
  };

  that.imageDataToURl = function (dataList, type, cb) {
    _executeCMD(CMD.imageDataToURl, {
      imageDataList: dataList,
      type: type
    }, cb);
  };

  that.getIdentifyStatus = function (cb) {
    _executeCMD(CMD.getIdentifyStatus, null, cb);
  };

  that.userBehavior = function (to_uid, event_type, event_Data) {
    _executeCMD(CMD.userBehavior, {
      to_uid: to_uid,
      event_type: event_type,
      event_Data: event_Data
    });
  };

  that.executeCMD = _executeCMD;

  return that;
};

var platform = PlatformHelper();

window.platform = platform;

exports.default = platform;

/***/ }),
/* 2 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
    value: true
});
var Eventuality = function Eventuality(obj) {
    var registry = {};

    obj.fire = function (event) {

        var handler = null;

        if (registry.hasOwnProperty(event)) {
            var event_list = registry[event];
            for (var i = 0; i < event_list.length; ++i) {
                handler = event_list[i];
                var args = [];

                //not including event
                for (var n = 1; n < arguments.length; ++n) {
                    args.push(arguments[n]);
                }
                handler.apply(this, args);
            }
        }

        return this;
    };

    obj.on = function (type, method) {
        if (registry.hasOwnProperty(type)) {
            registry[type].push(method);
        } else {
            registry[type] = [method];
        }

        return this;
    };

    obj.removeListener = function (type, method) {
        if (!registry.hasOwnProperty(type)) {
            return false;
        }
        var index = registry[type].indexOf(method);
        if (index >= 0) {
            registry[type].splice(index, 1);
        }
    };

    obj.removeAllListeners = function () {
        registry = {};
    };
    return obj;
};
window.Eventuality = Eventuality;
exports.default = Eventuality;

/***/ }),
/* 3 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };

/**
 * Created by wizard on 16/3/30.
 */

// import ResourceManager from './resource-manager';
//import Tween from 'tween.js'
var helper = helper || {};

helper.reorderNode = function (node) {
  var depthCompare = function depthCompare(a, b) {
    var az = a.zorder;
    var bz = b.zorder;
    if (az == undefined) {
      az = 0;
    }
    if (bz == undefined) {
      bz = 0;
    }
    if (az < bz) return -1;
    if (az > bz) return 1;
    return 0;
  };

  node.children.sort(depthCompare);
};

helper.sleep = function (duration) {
  return new Promise(function (resolve, reject) {
    var action = new TWEEN.Tween({}).to({}, duration).onComplete(function () {
      resolve();
    }).start();
    // setTimeout(()=> {
    //   resolve();
    // }, duration)
  });
};

helper.delay = function (duration, cb) {
  var tween = new TWEEN.Tween();
  tween.delay(duration);
  tween.onComplete(function () {
    if (cb) {
      cb();
    }
  });
  tween.start();
};

helper.floorToFixed = function (value, count) {
  var num = Math.floor(value * Math.pow(10, count)) / Math.pow(10, count);
  // num = parseFloat(num);
  // num.toFixed(2);
  // return num;
  return num.toFixed(count);
  // return (value - 0.5 / Math.pow(10,count)).toFixed(count);
};
helper.isArray = function (obj) {
  return obj && (typeof obj === 'undefined' ? 'undefined' : _typeof(obj)) == "object" && obj.hasOwnProperty('length') && typeof obj.length == 'number';
};

helper.isObject = function (obj) {
  return obj && (typeof obj === 'undefined' ? 'undefined' : _typeof(obj)) == "object" && !helper.isArray(obj);
};

helper.cloneObject = function (sourceObj, targetObj) {

  if (helper.isArray(sourceObj)) {
    if (targetObj == undefined) {
      targetObj = [];
    }

    if (!helper.isArray(targetObj)) {
      return;
    }

    for (var i = 0; i < sourceObj.length; ++i) {
      var subTargetObj;
      if (helper.isArray(sourceObj[i]) || helper.isObject(sourceObj[i])) {
        subTargetObj = helper.cloneObject(sourceObj[i]);
      } else {
        subTargetObj = sourceObj[i];
      }

      targetObj.push(subTargetObj);
    }
  } else if (helper.isObject(sourceObj)) {
    if (targetObj == undefined) {
      targetObj = {};
    }

    if (!helper.isObject(targetObj)) {
      return;
    }

    for (var index in sourceObj) {
      var subTargetObj;
      if (helper.isArray(sourceObj[index]) || helper.isObject(sourceObj[index])) {
        subTargetObj = helper.cloneObject(sourceObj[index]);
      } else {
        subTargetObj = sourceObj[index];
      }

      targetObj[index] = subTargetObj;
    }
  }

  return targetObj;
};

helper.arrayFindOne = function (list, filter) {
  for (var i = 0; i < list.length; i++) {
    if (filter(list[i])) {
      return list[i];
    }
  }
  return null;
};

helper.pSub = function (v1, v2) {
  return { x: v1.x - v2.x, y: v1.y - v2.y };
};

helper.pDistance = function (v1, v2) {
  var v = helper.pSub(v1, v2);
  return Math.sqrt(v.x * v.x + v.y * v.y);
};

helper.randomInt = function (min, max) {
  var range = max - min;
  var random = Math.random();
  var result = min + Math.floor(random * range);
  return result;
};

helper.getQueryString = function (name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) return decodeURI(r[2]);
  return null;
};

helper.getImgUrlWithSize = function (url, size) {
  console.log('getImgUrlWithSize', url);
  var suffix = "jpg";
  var strings = url.split(".");
  if (strings.length > 0) {
    suffix = strings[strings.length - 1];
  }
  var newUrl = url + "@1e_" + size + "w_" + size + "h_1c_0i_1o_90Q_1x." + suffix;
  var resultUrl = url.indexOf("img1") !== -1 || url.indexOf("@1e_") !== -1 ? url : newUrl;
  console.log('getImgUrlWithSize', resultUrl);
  return resultUrl;
};

helper.formatDateString = function (year, month) {
  var str = '' + year + month;
  if (month < 10) {
    str = year + '0' + month;
  }
  return str;
};

helper.getTimestamp = function (timeStr) {
  // 增加一天取值 防止服务器时间不同步
  timeStr += '-02';
  var date = new Date(timeStr);
  return parseInt(date.getTime() / 1000);
};

helper.timestamp2Date = function (timestamp) {
  timestamp = parseInt(timestamp * 1000);
  var date = new Date(timestamp);
  return date;
};

String.prototype.replaceAll = function (s1, s2) {
  return this.replace(new RegExp(s1, "gm"), s2);
};

window.helper = helper;
exports.default = helper;

/***/ }),
/* 4 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
/**
 * Created by wizard on 2017/1/10.
 */
var URLUtility = function URLUtility() {
  var that = {};

  that.ropeParamsString = function (params, isStart) {
    var paramStr = "";
    if (params) {
      var start = isStart != undefined ? isStart : true;
      for (var index in params) {
        if (start) {
          start = false;
          paramStr += "?";
        } else {
          paramStr += "&";
        }
        paramStr += index + "=" + params[index];
      }
    }

    return paramStr;
  };

  return that;
};

var utilityURL = URLUtility();

exports.default = utilityURL;

/***/ }),
/* 5 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__(0);


/***/ })
/******/ ]);
//# sourceMappingURL=jxlib.all.js.map