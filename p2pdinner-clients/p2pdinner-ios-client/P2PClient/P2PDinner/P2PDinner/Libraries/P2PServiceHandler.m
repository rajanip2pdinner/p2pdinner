//
//  P2PServiceHandler.m
//  P2PDinner
//
//  Created by sudheerkumar on 2/28/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "P2PServiceHandler.h"
static P2PServiceHandler *_sharedInstance=nil;
@implementation P2PServiceHandler
+(id)sharedP2PServiceHandler
{
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        _sharedInstance = [[self alloc] init];
    });
    return _sharedInstance;
}
-(void)execute:(NSString *)url getRequst:(NSString *)requestObject serviceCallBack:(P2PServiceResultBlock)serviceCallBack{
    [[ServiceHandler sharedServiceHandler] execute:url requestObject:requestObject contentType:MIMETypeJSON requestMethod:RequestTypeGet serviceCallBack:serviceCallBack];
    
}
-(void)execute:(NSString *)url postRequst:(NSString *)requestObject serviceCallBack:(P2PServiceResultBlock)serviceCallBack{
    [[ServiceHandler sharedServiceHandler] execute:url requestObject:requestObject contentType:MIMETypeJSON requestMethod:RequestTypePost serviceCallBack:serviceCallBack];
    
}
@end
