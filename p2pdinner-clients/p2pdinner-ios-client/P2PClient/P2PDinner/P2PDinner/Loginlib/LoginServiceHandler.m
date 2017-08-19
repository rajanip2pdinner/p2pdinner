//
//  LoginServiceHandler.m
//  P2PDinner
//
//  Created by Selvam M on 3/6/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "LoginServiceHandler.h"
#import  "SharedLogin.h"
#import "Utility.h"
static LoginServiceHandler *_sharedInstance=nil;
@interface LoginServiceHandler()
- (void)checkUserLogin:(LoginRequest *)loginRequest serviceCallBack:(RegisterResultBlock)service;
@end
@implementation LoginServiceHandler
+ (id)sharedServiceHandler
{
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        _sharedInstance = [[self alloc] init];
    });
    return _sharedInstance;
}
- (void)loginServiceHandler:(LoginRequest *)loginRequest serviceCallBack:(RegisterResultBlock)service{
    [self checkUserLogin:loginRequest serviceCallBack:service];
}
- (void)registerUserLogin:(LoginRequest *)loginRequest serviceCallBack:(RegisterResultBlock)service
{
    
    
    requestType=RequestTypePost;
    conType=MIMETypeJSON;
    [self execute:@"api/v1/profile" requestObject:[loginRequest createLoginRequestContent] contentType:conType requestMethod:requestType serviceCallBack:^(NSError *error, id response) {
        
        if (!error) {
            NSLog(@"Register success");
            [self checkUserLogin:loginRequest serviceCallBack:service];
        }
        else service(error,response);
        
    }];
    
    
}
- (void)profileUpdateCertificate:(NSString *)certificateImage serviceCallBack:(RegisterResultBlock)service{
    certificateImage = [certificateImage stringByReplacingOccurrencesOfString:@"\"" withString:@""];
    requestType=RequestTypePut;
    conType=MIMETypeJSON;
    NSString *userId=[[NSUserDefaults standardUserDefaults]objectForKey:@"userId"];
    NSString *requestJson = [NSString stringWithFormat:@"{\"certificates\":\"%@\"}",certificateImage];
     NSString *requestURL=[NSString stringWithFormat:@"/api/v1/profile/%ld/update",(long)[userId integerValue]];
    [self execute:requestURL requestObject:requestJson contentType:conType requestMethod:requestType serviceCallBack:^(NSError *error, id response) {
        
         service(error,response);
        
    }];
}
- (void)getProfileDetails:(NSString *)profileId serviceCallBack:(RegisterResultBlock)service{
    requestType=RequestTypeGet;
    conType=MIMETypeJSON;
    NSString *userId=[[NSUserDefaults standardUserDefaults]objectForKey:@"userId"];
    NSString *requestURL=[NSString stringWithFormat:@"api/v1/profile/%ld",(long)[userId integerValue]];
    [self execute:requestURL requestObject:nil contentType:conType requestMethod:requestType serviceCallBack:^(NSError *error, id response) {
        
        LoginResponce * loginResponce=[[LoginResponce alloc]init];
        
        if (!error) {
            [[SharedLogin sharedLogin] setUserProfileDetails:response];
            [loginResponce setCode:@"p2pdinner.user_found"];
            [loginResponce setMessage:@"User Login Success."];
            service(nil,loginResponce);
        }
        else
            service(error,response);
        
    }];
}
-(BOOL)isRegisteredAlready{
    
    if ([[NSUserDefaults standardUserDefaults]objectForKey:@"devToken"]) {
        return [[NSUserDefaults standardUserDefaults] boolForKey:@"devTokenRegisterd"];
    }
    return TRUE;
}
-(void)validateDeviceRegister:(NSNumber *)userId{
    
    if (![self isRegisteredAlready]) {
        requestType=RequestTypePost;
        conType=MIMETypeJSON;
        NSString *requestURL=[NSString stringWithFormat:@"api/v1/profile/%ld/devices",(long)[userId integerValue]];
        NSString *requestJson=[NSString stringWithFormat:@"{\"deviceType\" : \"apple\",\"registrationId\" : \"%@\",\"notificationsEnabled\" : true }",[[NSUserDefaults standardUserDefaults]objectForKey:@"devToken"]];
        [self execute:requestURL requestObject:requestJson contentType:conType requestMethod:requestJson serviceCallBack:^(NSError *error, id response) {
            
            if (!error) {
                [[NSUserDefaults standardUserDefaults] setBool:TRUE forKey:@"devTokenRegisterd"];
            }
            
            else
                [[NSUserDefaults standardUserDefaults] setBool:false forKey:@"devTokenRegisterd"];
        }];
        
    }
    
}
- (void)checkUserLogin:(LoginRequest *)loginRequest serviceCallBack:(RegisterResultBlock)service{
    requestType=RequestTypeGet;
    conType=MIMETypeJSON;
    NSString *requestURL=[NSString stringWithFormat:@"api/v1/profile/validate?emailAddress=%@&password=%@",loginRequest.emailAddress,loginRequest.password];
    [self execute:requestURL requestObject:[loginRequest createLoginRequestContent] contentType:conType requestMethod:requestType serviceCallBack:^(NSError *error, id response) {
        LoginResponce * loginResponce=[[LoginResponce alloc]init];
        
        if (!error) {
            [loginResponce mapObjectFromResponce:response];
        }
        
        if (!error&&response!=nil&&[loginResponce.code isEqualToString:@"p2pdinner.user_not_found"]) {
            NSLog(@"Need to register");
            [self registerUserLogin:loginRequest serviceCallBack:service];
            
        }
        
        else if (!error)
        {
            NSLog(@"Already register Need to create shared login respoce");
            [[SharedLogin sharedLogin] setUserProfileDetails:response];
            [loginResponce setCode:@"p2pdinner.user_found"];
            [loginResponce setMessage:@"User Login Success."];
            //need to validate device token for register
            [self validateDeviceRegister:[[SharedLogin sharedLogin] userId]];
            service(nil,loginResponce);
            
        }
        
        else if(error){
            [self registerUserLogin:loginRequest serviceCallBack:service];
        }
        
        else service(error,response);
        
    }];
}
@end
