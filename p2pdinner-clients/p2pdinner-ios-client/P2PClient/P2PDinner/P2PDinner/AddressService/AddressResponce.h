//
//  AddressResponce.h
//  P2PDinner
//
//  Created by sudheerkumar on 2/8/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AddressResponce : NSObject
@property (nonatomic,strong) NSString *formatted_address;
@property (nonatomic,strong) NSDictionary *location;
@property (nonatomic,strong) NSArray *results;

@end
