//
//  AppDelegate.h
//  P2PDinner
//
//  Created by selvam on 2/3/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <FacebookSDK/FacebookSDK.h>
#import "LaunchScreen.h"
#import "LocationManger.h"
typedef void (^FacebookAuthSuccessBlock)(void);
typedef void (^FacebookAuthFailureBlock)(void);

extern NSString *const FBSessionStateChangedNotification;

@interface AppDelegate : UIResponder <UIApplicationDelegate,CLLocationManagerDelegate,LocationManagerDelegate>{
}

@property (strong, nonatomic) UIWindow *window;
@property (nonatomic, copy) FacebookAuthSuccessBlock fbAuthSuccessBlock;
@property (nonatomic, copy) FacebookAuthFailureBlock fbAuthFailureBlock;
@property (nonatomic,strong) CLLocation *lastLocation;
@property (nonatomic,strong) NSString *lastAddress;
@property (nonatomic,strong) NSString *localLocation;
@end

