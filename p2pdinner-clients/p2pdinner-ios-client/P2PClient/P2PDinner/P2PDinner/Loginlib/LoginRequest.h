//
//  LoginRequest.h
//  P2PDinner
//
//  Created by Selvam M on 3/6/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "P2PRequestObject.h"

@interface LoginRequest : P2PRequestObject
@property NSString *emailAddress;
@property NSString *password;
@property NSString *name;
- (NSString *)createLoginRequestContent;
@end
