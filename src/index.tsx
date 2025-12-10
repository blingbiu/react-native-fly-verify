import {NativeEventEmitter, NativeModules, Platform} from 'react-native';
const {FlyVerifyScope} = NativeModules;

const eventEmitter = new NativeEventEmitter(FlyVerifyScope);

interface FlyVerifyInitParams {
    appKey: string; 
    appSecret: string; 
    isAgreePrivacy: boolean;
}

class FlyVerify {
    /**
     * 初始化
     * @param params 
     * @param params.appKey //仅ios
     * @param params.appSecret //仅ios
     * @param params.isAgreePrivacy //用户是否同意隐私协议
     * @returns 
     */
    static init = async(params: FlyVerifyInitParams) => {
        let ret = null;
        if(Platform.OS == 'android') {
            ret = await FlyVerifyScope.submitPolicyGrantResult(params?.isAgreePrivacy ?? false);
        } else {
            ret = await FlyVerifyScope.init(params ?? {});
        }
        return ret;
    }

    /**
     * 预登录 
     * 此调用将有助于提高拉起授权页的速度和成功率
     * 建议在一键登录前提前调用此方法
     * 不建议频繁多次调用和拉起授权页后调用
     * */
    static preVerifyLogin = async() => {
        return await FlyVerifyScope.preVerify();
    }

    /**
     * 一键登录获取Token
     * 同时返回预登陆信息和token
     * */
    static verifyLogin = async() => {
        return await FlyVerifyScope.verifyLogin();
    }
}

export default FlyVerify
