//
//  LoginServiceHandler.h
//  P2PDinner
//
//  Created by Selvam M on 3/6/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "ServiceHandler.h"
#import "LoginRequest.h"
#import "LoginResponce.h"

typedef void(^RegisterResultBlock)(NSError *error, LoginResponce *response);
@interface LoginServiceHandler : ServiceHandler{
    NSString *requestType;
    NSString *conType;
}
+ (id)sharedServiceHandler;
- (void)loginServiceHandler:(LoginRequest *)loginRequest serviceCallBack:(RegisterResultBlock)service;
- (void)profileUpdateCertificate:(NSString *)certificateImage serviceCallBack:(RegisterResultBlock)service;
- (void)getProfileDetails:(NSString *)profileId serviceCallBack:(RegisterResultBlock)service;
@end
