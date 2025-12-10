# react-native-fly-verify

[![npm version](https://badge.fury.io/js/react-native-fly-verify.svg)](https://badge.fury.io/js/react-native-fly-verify)

Android SDK Version: 13.7.3

iOS SDK Version: 13.7.8

此项目为无UI版SDK集成模块

## 开始

`$ npm install react-native-fly-verify -E`

## 配置

### 2.1 Android

* build.gradle

  ```sh
  android {
        defaultConfig {
            applicationId "yourApplicationId"           //在此替换你的应用包名
            ...
            manifestPlaceholders = [
                FLY_VERIFY_KEY: "yourAppKey",         //在此替换你的APPKey
                FLY_VERIFY_SECRET: "yourSecret"        //在此替换你的APPSecret
            ]
        }
    }
  ```

### 2.2 iOS

打开`ios/Podfile`文件，添加以下自建百川仓库，可以自己fork

```Podfile
require_relative '../node_modules/@react-native-community/cli-platform-ios/native_modules'
·······
target 'App' do
  # tag 为 https://github.com/bashen1/FlyVerify-NoUI-Specs.git仓库实际tag
  pod 'FlyVerify-NoUI-Specs', :git=> 'https://github.com/bashen1/FlyVerify-NoUI-Specs.git', :tag=> '1.0.0'

·······
target
```

## 插件接口文档

```javascript
import FlyVerify from 'react-native-fly-verify';

// 初始化
let init = await FlyVerify.init({
    appKey: string, //仅iOS
    appSecret: string, //仅iOS
    isAgreePrivacy: boolean, //用户是否同意隐私协议
});

// 预登录
let ret = await FlyVerify.preVerifyLogin();

// 一键登录获取Token
let ret = await FlyVerify.verifyLogin();

```

## License

MIT
