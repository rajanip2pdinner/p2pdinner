//
//  LocationManger.m
//  P2PDinner
//
//  Created by Selvam
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "LocationManger.h"
static LocationManger *_sharedInstance;
@implementation LocationManger
@synthesize locationManager;
@synthesize delegate;
+ (id)sharedLocationManager
{
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        _sharedInstance = [[self alloc] init];
    });
    return _sharedInstance;
}
- (void)updateLocation{
    self.locationManager = [[CLLocationManager alloc] init];
    
    // Check for iOS 8. Without this guard the code will crash with "unknown selector" on iOS 7.
    if ([self.locationManager respondsToSelector:@selector(requestWhenInUseAuthorization)]) {
        [self.locationManager requestAlwaysAuthorization];
    }
    self.locationManager.delegate = self;
    [self resumeLocationUpate];
}
- (void)stopUpdatingLocation{
    [self.locationManager stopUpdatingLocation];
    self.locationManager=nil;
}
- (void)resumeLocationUpate{
    [self.locationManager startUpdatingLocation];
}
// Location Manager Delegate Methods
- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    [self.delegate currentUserLocation:[locations lastObject]];
}
- (void)locationManager:(CLLocationManager *)manager
       didFailWithError:(NSError *)error
{
    [self.delegate currentUserLocation:nil];
}
@end
