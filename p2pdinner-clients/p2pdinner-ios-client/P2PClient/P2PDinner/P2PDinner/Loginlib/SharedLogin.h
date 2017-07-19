//
//  SharedLogin.h
//  P2PDinner
//
//  Created by Selvam M on 3/8/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SharedLogin : NSObject
@property(nonatomic,strong) NSNumber *userId;
@property(nonatomic,strong) NSString *emailAddress;
@property(nonatomic,strong) NSString *addressLine1;
@property(nonatomic,strong) NSString *addressLine2;
@property(nonatomic,strong) NSString *city;
@property(nonatomic,strong) NSString *state;
@property(nonatomic,strong) NSString *zip;
@property(nonatomic,strong) NSString *latitude;
@property(nonatomic,strong) NSString *longitude;
@property(nonatomic,strong) NSString *password;
@property(nonatomic,strong) NSString *accountBalance;
@property(nonatomic,strong) NSString *isActive;
@property(nonatomic,strong) NSString *stripeCustomerId;
@property(nonatomic,strong) NSString *userCertificates;
+ (id)sharedLogin;
- (void)setUserProfileDetails:(NSDictionary *)userProfile;
@end
