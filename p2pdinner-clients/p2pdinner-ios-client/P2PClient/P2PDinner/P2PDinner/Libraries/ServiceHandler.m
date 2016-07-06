//
//  P2PServiceHandler.m
//  P2PDinner
//
//  Created by sudheerkumar on 2/28/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "ServiceHandler.h"
#import "Utility.h"
#import "AFNetworking.h"
static ServiceHandler *_sharedInstance=nil;
@interface ServiceHandler(){
    AFHTTPRequestOperationManager *manager;
}
@property(nonatomic,retain)AFHTTPRequestOperation *operation;
@end
@implementation ServiceHandler
@synthesize operation;

- (void)newOauthRequestOperation:(NSString *)refreshToken withCompletion:(ServiceResultBlock)returnBlock{
    manager = [[AFHTTPRequestOperationManager manager] initWithBaseURL:[NSURL URLWithString:@"https://dev-p2pdinner-services.herokuapp.com/"]];
    manager.requestSerializer = [AFHTTPRequestSerializer serializer];
    [manager.requestSerializer setAuthorizationHeaderFieldWithUsername:@"7568CL2AX2Z5HJXJ6VUAWDUD7" password:@"kfpZC6leleyLezvC3xxaSJhm6SSaAl65wwG2/xJ/278"];
    [manager.requestSerializer setValue:@"NzU2OENMMkFYMlo1SEpYSjZWVUFXRFVENzprZnBaQzZsZWxleUxlenZDM3h4YVNKaG02U1NhQWw2NXd3RzIveEovMjc4" forHTTPHeaderField:@"Authorization"];
    NSDictionary *request;
    if ([refreshToken length]>5) {
        request=@{@"grant_type":@"refresh_token" , @"username":@"selvam@p2pdinner.com" , @"password":@"Welcome123",@"refresh_token":refreshToken};
    }else{
    request=@{@"grant_type":@"password" , @"username":@"selvam@p2pdinner.com" , @"password":@"Welcome123"};
    }
    [manager.requestSerializer  setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [manager.requestSerializer  setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    operation=[manager POST:@"api/v1/oauth/token" parameters:request success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSDictionary *responseDictionary=(NSDictionary *)responseObject;
        double value=[[responseDictionary objectForKey:@"expires_in"] doubleValue];
        [self saveOauthToken:[responseDictionary objectForKey:@"access_token"] andRefreshToken:[responseDictionary objectForKey:@"refresh_token"] andTokenType:[responseDictionary objectForKey:@"token_type"]  andExpiryTime:value];
        returnBlock(nil,[responseDictionary objectForKey:@"access_token"]);
        
        NSLog(@"%@",responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"%@",error);
        returnBlock(error,nil);
    }];
}
-(void)saveOauthToken:(NSString *)accessToken andRefreshToken:(NSString *)refreshToken andTokenType:(NSString *)tokenType andExpiryTime:(double)expiryTime{
    //Need to be keychain
    [[NSUserDefaults standardUserDefaults]setObject:accessToken forKey:@"oAuthAccessToken"];
    [[NSUserDefaults standardUserDefaults]setObject:[self getNextExpiryTimeWiht:expiryTime] forKey:@"oAuthExpiry"];
    [[NSUserDefaults standardUserDefaults]setObject:refreshToken forKey:@"oAuthrefreshToken"];
    [[NSUserDefaults standardUserDefaults]setObject:tokenType forKey:@"tokenType"];
}
-(NSDate *)getNextExpiryTimeWiht:(double)value{
    //Need to be keychain
    return [[NSDate date]dateByAddingTimeInterval:value];
}
-(void)validateAccessToken:(ServiceResultBlock)returnBlock{
    NSDate *expiryDate=[[NSUserDefaults standardUserDefaults] objectForKey:@"oAuthExpiry"];
    if (!expiryDate) {
        //Need to call NewOAuthRequest
        [self newOauthRequestOperation:[[NSUserDefaults standardUserDefaults] objectForKey:@"oAuthrefreshToken"] withCompletion:returnBlock];
    }else if ([expiryDate timeIntervalSinceNow] < 0.0) {
        // Date has passed
        //Need to call refreshOAuthRequest
        [self newOauthRequestOperation:nil withCompletion:returnBlock];
    }
    else{//have valid OauthToken start service{
        returnBlock(nil,[[NSUserDefaults standardUserDefaults] objectForKey:@"oAuthAccessToken"]);
      }
}

+ (id)sharedServiceHandler
{
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        _sharedInstance = [[self alloc] init];
    });
    return _sharedInstance;
}
- (void)execute:(NSString *)url requestObject:(NSString *)requestValue contentType:(NSString *)contentType requestMethodGetServiceCallBack:(ServiceResultBlock)serviceCallBackBlock{
    
    [self validateAccessToken:^(NSError *error, id response) {
        if(error){
            serviceCallBackBlock(error,nil);
        }else{
            manager = [AFHTTPRequestOperationManager manager];
            NSString *accessToken=[NSString stringWithFormat:@"%@ %@",[[NSUserDefaults standardUserDefaults] objectForKey:@"tokenType"],response];
            
            
            manager.requestSerializer = [AFJSONRequestSerializer serializer];
            [manager.requestSerializer setValue:accessToken forHTTPHeaderField:@"Authorization"];

            
            operation=[manager GET:url parameters:requestValue success:^(AFHTTPRequestOperation *operation, id responseObject) {
                
                NSLog(@"JSON: %@", responseObject);
                serviceCallBackBlock(nil,responseObject);
                
            } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
                
                //        if (error.code==3840) {
                //            UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Service Unavailable" message:@"service temporarily unavailable" preferredStyle:UIAlertControllerStyleAlert];
                //
                //            UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
                //            [alertController addAction:ok];
                //            UIWindow *alertWindow = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
                //            [self.rootViewController presentViewController:alertController animated:YES completion:nil];
                //        }
                
                NSLog(@"Error: %@", error);
                serviceCallBackBlock(error,nil);
                
            }];
        }
    }];
    
    
}
- (void)cancelRequest{
    [operation cancel];
}
- (void)execute:(NSString *)url requestObject:(NSString *)requestValue contentType:(NSString *)contentType requestMethodPostServiceCallBack:(ServiceResultBlock)serviceCallBackBlock{
    
    [self validateAccessToken:^(NSError *error, id response) {
        if(error){
            serviceCallBackBlock(error,nil);
        }else{
    manager = [AFHTTPRequestOperationManager manager];
    NSData* requestData = [requestValue dataUsingEncoding:NSUTF8StringEncoding];
    NSError *error=nil;
    NSDictionary *requestDict;
    if (!requestValue) {
        requestDict=@{};
    }else{
    requestDict=[NSJSONSerialization JSONObjectWithData:requestData options:kNilOptions error:&error];
    }
    
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    NSString *accessToken=[NSString stringWithFormat:@"%@ %@",[[NSUserDefaults standardUserDefaults] objectForKey:@"tokenType"],response];
            
            
            
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager.requestSerializer setValue:accessToken forHTTPHeaderField:@"Authorization"];

    operation=[manager POST:url parameters:requestDict success:^(AFHTTPRequestOperation *operation, id responseObject){
        
        NSLog(@"JSON: %@", responseObject);
        serviceCallBackBlock(nil,responseObject);
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        NSLog(@"Error: %@", error);
        
//        if (error.code==3840) {
//            UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Service Unavailable" message:@"service temporarily unavailable" preferredStyle:UIAlertControllerStyleAlert];
//            
//            UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
//            [alertController addAction:ok];
//            UIWindow *alertWindow = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
//            [alertWindow.rootViewController presentViewController:alertController animated:YES completion:nil];
//        }
        serviceCallBackBlock(error,nil);
        
    }];
        }
    }];
}
- (void)execute:(NSString *)url requestObject:(NSString *)requestValue contentType:(NSString *)contentType requestMethod:(NSString *)requestMethod serviceCallBack:(ServiceResultBlock)serviceCallBackBlock{
    resultBlock=serviceCallBackBlock;
    if ([requestMethod isEqualToString:@"GET"]) {
        url=[NSString stringWithFormat:@"%@",url];
        [self execute:url requestObject:requestValue contentType:contentType requestMethodGetServiceCallBack:serviceCallBackBlock];
    }
    else{
        [self execute:url requestObject:requestValue contentType:contentType requestMethodPostServiceCallBack:serviceCallBackBlock];
    }
    
}
@end
