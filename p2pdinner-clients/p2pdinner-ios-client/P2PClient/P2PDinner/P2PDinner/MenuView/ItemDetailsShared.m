//
//  ItemDetailsShared.m
//  P2PDinner
//
//  Created by Selvam M on 7/12/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "ItemDetailsShared.h"
static ItemDetailsShared *_sharedInstance=nil;
@implementation ItemDetailsShared
+ (id)sharedItemDetails
{
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        //Set Default Values
        _sharedInstance = [[self alloc] init];
    });
    return _sharedInstance;
}

@end
