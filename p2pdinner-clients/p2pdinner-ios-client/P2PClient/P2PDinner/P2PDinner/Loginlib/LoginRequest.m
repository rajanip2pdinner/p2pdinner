//
//  LoginRequest.m
//  P2PDinner
//
//  Created by Selvam M on 3/6/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "LoginRequest.h"

@implementation LoginRequest
- (NSString *)createLoginRequestContent{
    //Need to implement push notification responce and request
   return [NSString stringWithFormat:@"{\"emailAddress\":\"%@\",\"password\":\"%@\",\"name\":\"%@\",\"devices\":[{\"deviceType\":\"apple\",\"registrationId\" : \"%@\",\"notifiationsEnabled\":\"true\"}]}",self.emailAddress,self.password,self.name,[[NSUserDefaults standardUserDefaults]objectForKey:@"devToken"]];
}
@end
