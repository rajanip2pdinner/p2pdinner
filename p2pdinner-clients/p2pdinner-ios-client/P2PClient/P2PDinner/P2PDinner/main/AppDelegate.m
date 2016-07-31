//
//  ViewController.h
//  P2PDinner
//
//  Created by P2PDinner on 2/3/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "AppDelegate.h"
#import "ServiceHandler.h"
#import "LocationServiceHandler.h"

#define FB_APP_ID @"393349550836785"
@interface AppDelegate ()

@end

@implementation AppDelegate
@synthesize fbAuthSuccessBlock;
@synthesize fbAuthFailureBlock;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
  //  if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0){
        
        UIUserNotificationType types = UIUserNotificationTypeBadge |
        UIUserNotificationTypeSound | UIUserNotificationTypeAlert;
        
        UIUserNotificationSettings *mySettings =
        [UIUserNotificationSettings settingsForTypes:types categories:nil];
        
        [application registerUserNotificationSettings:mySettings];
        [application registerForRemoteNotifications];
        
        
//    }else{
//        [application registerForRemoteNotificationTypes:(UIUserNotificationTypeBadge | UIUserNotificationTypeSound | UIUserNotificationTypeAlert)];
//    }
    return YES;
}
- (void)application:(UIApplication *)app didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
    NSString *token = [[deviceToken description] stringByTrimmingCharactersInSet: [NSCharacterSet characterSetWithCharactersInString:@"<>"]];
    token = [token stringByReplacingOccurrencesOfString:@" " withString:@""];
    [[NSUserDefaults standardUserDefaults]setObject:token forKey:@"devToken"];
}
- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    LocationManger *locationMgr=[LocationManger sharedLocationManager];
    [locationMgr updateLocation];
    locationMgr.delegate=self;
    
    //Need to get updated address.
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}


// During the Facebook login flow, your app passes control to the Facebook iOS app or Facebook in a mobile browser.
// After authentication, your app will be called back with the session information.
// Override application:openURL:sourceApplication:annotation to call the FBsession object that handles the incoming URL
- (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
         annotation:(id)annotation
{
    
    return [FBSession.activeSession handleOpenURL:url];
}
- (void)currentUserLocation:(CLLocation *)Location{
     [[LocationManger sharedLocationManager]stopUpdatingLocation];
    _lastLocation=Location;
     CLGeocoder * geoCoder = [[CLGeocoder alloc] init];
    [geoCoder reverseGeocodeLocation:Location completionHandler:^(NSArray *placemarks, NSError *error) {
        for (CLPlacemark * placemark in placemarks)
        {
            _localLocation=[NSString stringWithFormat:@"en_%@",[placemark ISOcountryCode]];

        }
        }];
    [[LocationServiceHandler sharedLocationHandler]getLocationAddressServiceCallBack:^(NSError *error, NSString *response) {
        
    }];
}
@end
