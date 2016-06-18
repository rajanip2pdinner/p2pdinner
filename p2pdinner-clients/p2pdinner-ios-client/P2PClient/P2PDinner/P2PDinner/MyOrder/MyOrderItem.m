//
//  MyOrderItem.m
//  P2PDinner
//
//  Created by Selvam M on 7/27/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "MyOrderItem.h"

@implementation MyOrderItem

@end
@implementation ItemListing

@end
@implementation CarRecivedItemDetail

@end
@implementation AddDinnerListItem

- (AddDinnerListItem *)createAddDinnerItemFromDictionary:(NSDictionary *)dicValue{
    AddDinnerListItem *retunObject=[[AddDinnerListItem alloc]init];
    [retunObject setStartTime:[dicValue objectForKey:@"startTime"]?[dicValue objectForKey:@"startTime"]:[dicValue objectForKey:@"startDate"]];
    [retunObject setEndTime:[dicValue objectForKey:@"endTime"]?[dicValue objectForKey:@"endTime"]:[dicValue objectForKey:@"endDate"]];
    [retunObject setCloseTime:[dicValue objectForKey:@"closeTime"]?[dicValue objectForKey:@"closeTime"]:[dicValue objectForKey:@"closeDate"]];
    [retunObject setCostPerItem:[dicValue objectForKey:@"costPerItem"]];
    [retunObject setMenuItemId:[dicValue objectForKey:@"menuItemId"]?[dicValue objectForKey:@"menuItemId"]:[dicValue objectForKey:@"id"]];
    [retunObject setAvailableQuantity:[dicValue objectForKey:@"availableQuantity"]];

    
    return retunObject;
}

@end