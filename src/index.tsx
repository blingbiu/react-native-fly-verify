import { NativeEventEmitter, NativeModules} from 'react-native';
const {FlyVerifyScope} = NativeModules;

const eventEmitter = new NativeEventEmitter(FlyVerifyScope);

interface FlyVerifyInitParams {}

class FlyVerify {
    static init = (params: FlyVerifyInitParams) => {
        FlyVerifyScope.init(params ?? {});
    }
}

export default FlyVerify
