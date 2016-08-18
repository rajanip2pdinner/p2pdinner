//
//  LocationServiceHandler.m
//  P2PDinner
//
//  Created by Selvam M on 4/5/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "LocationServiceHandler.h"
#import "AppDelegate.h"
static LocationServiceHandler *_sharedInstance=nil;
@implementation LocationServiceHandler

+ (id)sharedLocationHandler
{
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        _sharedInstance = [[self alloc] init];
    });
    return _sharedInstance;
}

- (void)getLocationAddressServiceCallBack:(LocationServiceResultBlock)serviceresponceBlock{
    AppDelegate *delegate=(AppDelegate *)[[UIApplication sharedApplication] delegate];
    NSString *requestURL=[NSString stringWithFormat:@"https://maps.googleapis.com/maps/api/geocode/json?latlng=%.6f,%.6f",delegate.lastLocation.coordinate.latitude,delegate.lastLocation.coordinate.longitude];
    [self getLocationAdderess:requestURL serviceCallBack:serviceresponceBlock];
}

- (void)getLocationAdderess:(NSString *)useId serviceCallBack:(LocationServiceResultBlock)serviceresponceBlock{
      AppDelegate *appDelegate=(AppDelegate *)[[UIApplication sharedApplication] delegate];
    
    [self execute:useId requestObject:@"" contentType:@"application/json" requestMethod:@"GET" serviceCallBack:^(NSError *error, id response) {
        NSDictionary *results=(NSDictionary *)response;
        if ([response count]>0) {
            if ([[results objectForKey:@"results"] count]>0) {
                NSArray *address=[results objectForKey:@"results"];
                NSString *addressResult=[[address objectAtIndex:0] objectForKey:@"formatted_address"];
                appDelegate.lastAddress=addressResult;
                NSLog(@"%@",addressResult);
                serviceresponceBlock(nil,addressResult);
            }else
            {
                serviceresponceBlock(nil,[results objectForKey:@"status"]);
            }
            
        }
        else
            serviceresponceBlock(error,nil);
        
        
        
    }];
}
//[ServiceHandler execute:requestURL GetTypeRequestObject:req responseClass:[AddressResponce class] serviceCallBack:^(NSError *error, id response)
@end
