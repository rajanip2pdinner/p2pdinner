//
//  P2PServiceHandler.h
//  P2PDinner
//
//  Created by sudheerkumar on 2/28/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ServiceHandler.h"
//TypeDef POST GET
NSString * const RequestTypeGet = @"GET";
NSString * const RequestTypePost = @"POST";
//RequestType
NSString * const MIMETypeJSON = @"application/json";
NSString * const MIMETypeFormURLEncoded = @"application/x-www-form-urlencoded";
NSString * const MIMETypeXML = @"application/xml";
NSString * const MIMETypeTextXML = @"text/xml";
NSString * const MIMETypeTextHtml = @"text/html";
NSString * const MIMETypeTextJson = @"text/json";
NSString * const MIMETypeTextPlain = @"text/plain";

typedef void(^P2PServiceResultBlock)(NSError *error, id response);

@interface P2PServiceHandler : NSObject
+(id)sharedP2PServiceHandler;
-(void)execute:(NSString *)url getRequst:(NSString *)requestObject serviceCallBack:(P2PServiceResultBlock)serviceCallBack;
-(void)execute:(NSString *)url postRequst:(NSString *)requestObject serviceCallBack:(P2PServiceResultBlock)serviceCallBack;

@end
