//
//  TimeResponce.h
//  P2PDinner
//
//  Created by sudheerkumar on 2/7/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface TimeResponce : NSObject

@property (nonatomic,strong) NSString *timezone;
@property (nonatomic,strong) NSNumber *year;
@property (nonatomic,strong) NSNumber *month;
@property (nonatomic,strong) NSNumber *day;
@property (nonatomic,strong) NSNumber *hour;
@property (nonatomic,strong) NSNumber *minute;
@property (nonatomic,strong) NSNumber *second;

@end
