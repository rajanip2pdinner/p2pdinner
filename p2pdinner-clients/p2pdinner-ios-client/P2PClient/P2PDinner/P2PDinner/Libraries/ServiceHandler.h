//
//  P2PServiceHandler.h
//  P2PDinner
//
//  Created by sudheerkumar on 2/28/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ServiceConstants.h"
@interface ServiceHandler : NSObject{
    ServiceResultBlock resultBlock;
}
+ (id)sharedServiceHandler;
- (void)oauthRequestOperation;
- (void)execute:(NSString *)url requestObject:(NSString *)requestValue contentType:(NSString *)contentType requestMethod:(NSString *)requestMethod serviceCallBack:(ServiceResultBlock)serviceCallBackBlock;
-(void)validateAccessToken:(ServiceResultBlock)returnBlock;
- (void)cancelRequest;
@end

