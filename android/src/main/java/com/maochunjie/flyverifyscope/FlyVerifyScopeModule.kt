package com.maochunjie.flyverifyscope

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi

import com.facebook.react.bridge.Arguments
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

  @ReactMethod
  fun init(params: ReadableMap, promise: Promise) {
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
