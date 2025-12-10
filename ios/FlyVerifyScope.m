#import "FlyVerifyScope.h"
#import <FlyVerify/FlyVerify.h>
#import <FlyVerifyCSDK/FlyVerifyCSDK.h>

@implementation FlyVerifyScope {
}

RCT_EXPORT_MODULE()

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

- (NSArray<NSString *> *)supportedEvents {
    return @[];
}

RCT_EXPORT_METHOD(init:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    @try {
        NSString *appKey = params[@"appKey"];
        NSString *appSecret = params[@"appSecret"];
        BOOL isAgreePrivacy = params[@"isAgreePrivacy"] ? [params[@"isAgreePrivacy"] boolValue] : NO;
        
        if (!appKey || [appKey isKindOfClass:[NSNull class]] || appKey.length == 0) {
            NSDictionary *result = @{
                @"code": @0,
                @"message": @"appKey is required and cannot be empty"
            };
            resolve(result);
            return;
        }
        if (!appSecret || [appSecret isKindOfClass:[NSNull class]] || appSecret.length == 0) {
            NSDictionary *result = @{
                @"code": @0,
                @"message": @"appSecret is required and cannot be empty"
            };
            resolve(result);
            return;
        }

        [FlyVerifyC initKey:appKey secret:appSecret privacyLevel:2];
        [FlyVerifyC agreePrivacy:isAgreePrivacy onResult:nil];
        
        NSDictionary *result = @{
            @"code": @2000,
            @"message": @"初始化成功！"
        };
        resolve(result);
        
    } @catch (NSException *exception) {
        NSDictionary *result = @{
            @"code": @0,
            @"message": [NSString stringWithFormat:@"Failed to initialize FlyVerify SDK: %@", exception.reason ? : @"unknown error"]
        };
        resolve(result);
    }
}

RCT_EXPORT_METHOD(preVerify: (RCTPromiseResolveBlock)resolve  rejecter:(RCTPromiseRejectBlock)reject) {
    [FlyVerify preGetPhoneNumber:^(NSDictionary * _Nullable resultDic, NSError * _Nullable error) {
        //判断预取号是否成功
        if (error == nil && resultDic != nil && [resultDic isKindOfClass:NSDictionary.class]) {
            NSString * securityPhone = resultDic[@"securityPhone"];
            NSString *operator = resultDic[@"operator"];
            NSDictionary * uiElement = resultDic[@"uiElement"];
            NSString *privacyName = uiElement[@"privacyName"];
            NSString *privacyUrl = uiElement[@"privacyUrl"];
            NSString *slogan = uiElement[@"slogan"];
            
            NSDictionary *result = @{
                @"code": @2000,
                @"message": @"",
                @"securityPhone": securityPhone,
                @"operator": operator,
                @"privacyName": privacyName,
                @"privacyUrl": privacyUrl,
                @"slogan": slogan,
            };
            resolve(result);
        }else{
            //失败
            NSDictionary *result = @{
                @"code": @0,
                @"message": @"error",
            };
            if(error != nil){
                result = @{
                    @"code": @(error.code),
                    @"message": [NSString stringWithFormat:@"Domain: %@, %@, %@",
                                 error.domain,
                                 error.localizedDescription,
                                 error.localizedFailureReason],
                };
            }
            resolve(result);
        }
    }];
}

RCT_EXPORT_METHOD(verifyLogin: (RCTPromiseResolveBlock)resolve  rejecter:(RCTPromiseRejectBlock)reject) {
    [FlyVerify getLoginToken:^(NSDictionary * _Nullable resultDic, NSError * _Nullable error) {
        //判断获取Token是否成功
        if (error == nil && resultDic != nil && [resultDic isKindOfClass:NSDictionary.class]) {
            //成功
            NSString *token = resultDic[@"token"];
            NSString *opToken = resultDic[@"opToken"];
            NSString *operator = resultDic[@"operator"];
            
            NSDictionary *result = @{
                @"code": @2000,
                @"message": @"",
                @"token": token,
                @"opToken": opToken,
                @"operator": operator,
            };
            resolve(result);
        }else{
            //失败
            NSDictionary *result = @{
                @"code": @0,
                @"message": @"error",
            };
            if(error != nil){
                result = @{
                    @"code": @(error.code),
                    @"message": [NSString stringWithFormat:@"Domain: %@, %@, %@",
                                 error.domain,
                                 error.localizedDescription,
                                 error.localizedFailureReason],
                };
            }
            resolve(result);
        }
    }];
}

@end
