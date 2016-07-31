//
//  LocationServiceHandler.h
//  P2PDinner
//
//  Created by Selvam M on 4/5/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "ServiceHandler.h"
#import "AddressRequest.h"
#import "AddressResponce.h"
typedef void(^LocationServiceResultBlock)(NSError *error, NSString *response);
@interface LocationServiceHandler : ServiceHandler
+ (id)sharedLocationHandler;
- (void)getLocationAddressServiceCallBack:(LocationServiceResultBlock)serviceresponceBlock;

- (void)getLocationAdderess:(NSString *)useId serviceCallBack:(LocationServiceResultBlock)serviceresponceBlock;
@end
