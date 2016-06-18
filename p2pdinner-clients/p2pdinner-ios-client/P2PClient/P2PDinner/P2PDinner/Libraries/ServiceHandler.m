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
+ (id)sharedServiceHandler
{
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        _sharedInstance = [[self alloc] init];
    });
    return _sharedInstance;
}
- (void)execute:(NSString *)url requestObject:(NSString *)requestValue contentType:(NSString *)contentType requestMethodGetServiceCallBack:(ServiceResultBlock)serviceCallBackBlock{
    
    manager = [AFHTTPRequestOperationManager manager];
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
- (void)cancelRequest{
    [operation cancel];
}
- (void)execute:(NSString *)url requestObject:(NSString *)requestValue contentType:(NSString *)contentType requestMethodPostServiceCallBack:(ServiceResultBlock)serviceCallBackBlock{
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
