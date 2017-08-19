//
//  SharedLogin.m
//  P2PDinner
//
//  Created by Selvam M on 3/8/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "SharedLogin.h"
static SharedLogin *_sharedInstance=nil;
@implementation SharedLogin
+ (id)sharedLogin
{
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        _sharedInstance = [[self alloc] init];
    });
    return _sharedInstance;
}
- (void)setUserProfileDetails:(NSDictionary *)userProfile{
    self.userId=[userProfile objectForKey:@"id"];
    self.emailAddress=[userProfile objectForKey:@"emailAddress"];
    self.addressLine1=[userProfile objectForKey:@"addressLine1"];
    self.addressLine2=[userProfile objectForKey:@"addressLine2"];
    self.city=[userProfile objectForKey:@"city"];
    self.state=[userProfile objectForKey:@"state"];
    self.zip=[userProfile objectForKey:@"zip"];
    self.longitude=[userProfile objectForKey:@"longitude"];
    self.latitude=[userProfile objectForKey:@"latitude"];
    self.password=[userProfile objectForKey:@"password"];
    self.accountBalance=[userProfile objectForKey:@"accountBalance"];
    self.isActive=[userProfile objectForKey:@"isActive"];
    self.stripeCustomerId=[userProfile objectForKey:@"stripeCustomerId"];
    self.userCertificates=[userProfile objectForKey:@"certificates"];
    
}
@end
