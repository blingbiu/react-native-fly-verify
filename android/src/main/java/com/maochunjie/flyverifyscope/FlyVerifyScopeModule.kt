package com.maochunjie.flyverifyscope

import cn.fly.FlySDK
import cn.fly.verify.FlyVerify
import cn.fly.verify.common.callback.OperationCallback
import cn.fly.verify.common.exception.VerifyException
import cn.fly.verify.pure.entity.PreVerifyResult
import cn.fly.verify.pure.entity.VerifyResult
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter


class FlyVerifyScopeModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return "FlyVerifyScope"
  }

  /**
   * 初始化
   * 回传用户隐私授权结果
   * @param isGranted 用户是否同意隐私协议
   */
  @ReactMethod
  fun submitPolicyGrantResult(isGranted: Boolean, promise: Promise) {
    FlySDK.submitPolicyGrantResult(isGranted);

    val writableMap = Arguments.createMap()
    writableMap.putInt("code", 2000)
    writableMap.putString("message", "初始化成功！")

    promise.resolve(writableMap)
  }

  /**
   * 预登录 判断用户当前网络环境是否符合一键登录使用条件
   */
  @ReactMethod
  fun preVerify (promise: Promise) {
    FlyVerify.preVerify(object : OperationCallback<PreVerifyResult>() {
      override fun onComplete(data: PreVerifyResult) {
        //脱敏号码
        val securityPhone = data.securityPhone
        //运营商 移动CMCC 联通CUCC 电信CTCC
        val operator = data.operator
        val uiElement = data.uiElement
        //运营商隐私名称
        val privacyName = uiElement.privacyName
        //运营商隐私链接
        val privacyUrl = uiElement.privacyUrl
        //运营商slogan
        val slogan = uiElement.slogan

        val writableMap = Arguments.createMap()
        writableMap.putInt("code", 2000)
        writableMap.putString("message", "")
        writableMap.putString("securityPhone", securityPhone)
        writableMap.putString("operator", operator)
        writableMap.putString("privacyName", privacyName)
        writableMap.putString("privacyUrl", privacyUrl)
        writableMap.putString("slogan", slogan)

        promise.resolve(writableMap)
      }

      override fun onFailure(e: VerifyException) {
        //错误码
        val code = e.code
        //错误描述
        val message = e.message

        promise.resolve(convertToResult(code, message))
      }
    })
  }

  /**
   * 登录
   */
  @ReactMethod
  fun verifyLogin (promise: Promise) {
    FlyVerify.verify(object: OperationCallback<VerifyResult>() {
      override fun onComplete(verifyResult: VerifyResult) {
        // 获取授权码成功，将token信息传给应用服务端，再由应用服务端进行登录验证，此功能需由开发者自行实现
        // 运营商token
        val opToken = verifyResult.opToken
        // 服务器token
        val token = verifyResult.token
        // 运营商类型，[CMCC:中国移动，CUCC：中国联通，CTCC：中国电信]
        val operator = verifyResult.operator

        val writableMap = Arguments.createMap()
        writableMap.putInt("code", 2000)
        writableMap.putString("message", "")
        writableMap.putString("token", token)
        writableMap.putString("opToken", opToken)
        writableMap.putString("operator", operator)

        promise.resolve(writableMap)
      }

      override fun onFailure(e: VerifyException) {
        //错误码
        val code = e.code
        //错误描述
        val message = e.message
        promise.resolve(convertToResult(code, message))
      }
    })
  }

  private fun convertToResult(code: Int, message: String?): WritableMap {
    val writableMap = Arguments.createMap()
    writableMap.putInt("code", code)
    writableMap.putString("message", message)
    return writableMap
  }

  private fun fireEvent(eventName: String, params: WritableMap) {
    reactApplicationContext.getJSModule(RCTDeviceEventEmitter::class.java)
      .emit(eventName, params)
  }

  private fun ReadableMap.getBooleanOrDefault(key: String, defaultValue: Boolean): Boolean {
    return if (this.hasKey(key)) this.getBoolean(key) else defaultValue
  }

  private fun ReadableMap.getStringOrNull(key: String): String? {
    return if (this.hasKey(key)) this.getString(key) else null
  }
}
