//
//  LocationManger.h
//  P2PDinner
//
//  Created by sudheerkumar on 2/8/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
@protocol LocationManagerDelegate <NSObject>
@required
- (void)currentUserLocation:(CLLocation *)Location;
@end

@interface LocationManger : NSObject<CLLocationManagerDelegate>
{
    id<LocationManagerDelegate> delegate;
}
@property (strong, nonatomic) CLLocationManager *locationManager;
@property (strong, nonatomic) id<LocationManagerDelegate> delegate;
+ (id)sharedLocationManager;
- (void)updateLocation;
- (void)stopUpdatingLocation;

@end
