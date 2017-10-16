//
//  MyOrderItem.h
//  P2PDinner
//
//  Created by Selvam M on 7/27/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <Foundation/Foundation.h>
@interface ItemListing:NSObject
@property(nonatomic,strong)NSNumber *itemListing;
@property(nonatomic,strong)NSNumber *costPerItem;
@property(nonatomic,strong)NSNumber *quantyAvailabel;
@property(nonatomic,strong)NSNumber *quantityOrdered;
@property(nonatomic,strong)NSString *startTime;
@property(nonatomic,strong)NSString *closeTime;
@property(nonatomic,strong)NSString *endDate;
@end
@interface MyOrderItem : NSObject
@property(nonatomic,strong)NSString *title;
@property(nonatomic,strong)NSString *itemDescription;
@property(nonatomic,strong)NSArray *categories;
@property(nonatomic,strong)NSArray *delivery;
@property(nonatomic,strong)NSArray *specialNeeds;
@property(nonatomic,strong)ItemListing *listing;
@end

//MyOrderDetail With passcode
@interface CarRecivedItemDetail : NSObject
@property(nonatomic,strong)NSString *buyerName;
@property(nonatomic,strong)NSString *title;
@property(nonatomic,strong)NSString *itemDescription;
@property(nonatomic,strong)NSString *passCode;
@property(nonatomic,strong)NSNumber *totalPrice;
@property(nonatomic,strong)NSNumber *orderQuantity;
@property(nonatomic,strong)NSNumber *costPerItem;
@property(nonatomic,strong)NSString *deliveryType;
@property(nonatomic,strong)NSString *address_line1;
@property(nonatomic,strong)NSString *address_line2;
@property(nonatomic,strong)NSString *city;
@property(nonatomic,strong)NSString *state;
@property(nonatomic,strong)NSString *imageUri;
@property(nonatomic,strong)NSNumber *startTime;
@property(nonatomic,strong)NSNumber *closeTime;
@property(nonatomic,strong)NSNumber *endTime;
@property(nonatomic,strong)NSNumber *buyer_rating;
@property(nonatomic,strong)NSNumber *seller_rating;
@property(nonatomic,strong)NSNumber *cart_id;
@end


@interface AddDinnerListItem : NSObject
@property(nonatomic,strong)NSString *startTime;
@property(nonatomic,strong)NSString *endTime;
@property(nonatomic,strong)NSString *closeTime;
@property(nonatomic,strong)NSNumber *availableQuantity;
@property(nonatomic,strong)NSNumber *costPerItem;
@property(nonatomic,strong)NSString *menuItemId;
- (AddDinnerListItem *)createAddDinnerItemFromDictionary:(NSDictionary *)dicValue;

@end
