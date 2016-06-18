//
//  ItemDetails.h
//  P2PDinner
//
//  Created by Selvam M on 3/9/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "MyOrderItem.h"
typedef enum {
    CreatNewItem,
    UpdateOldItem
}ItemType;

@interface ItemDetails : NSObject
@property(nonatomic,strong) NSString *title;
@property(nonatomic,strong) NSString *dinnerDescription;
@property(nonatomic,strong) NSNumber *userId;
@property(nonatomic,assign) BOOL isActive;
@property(nonatomic,strong) NSString *dinnerCategories;
@property(nonatomic,strong) NSString *dinnerSpecialNeeds;
@property(nonatomic,strong) NSString *dinnerDelivery;
@property(nonatomic,strong) NSNumber *dinnerId;

@property(nonatomic,strong)NSString *addressLine1;
@property(nonatomic,strong)NSString *addressLine2;
@property(nonatomic,strong)NSString *zipCode;
@property(nonatomic,strong)NSString *state;
@property(nonatomic,strong)NSString *city;
@property(nonatomic,strong)NSNumber *availableQuantity;
@property(nonatomic,strong)NSNumber *costPerItem;
@property(nonatomic,strong)NSString *imageUri;
@property(nonatomic,strong)NSString *startDate;
@property(nonatomic,strong)NSString *endDate;
@property(nonatomic,strong)NSString *closeDate;

-(NSArray *)getHistoryDinnerDetails:(NSArray *)dinner;
-(ItemDetails *)setDinnerDetails:(NSDictionary *)dinner;
-(NSString *)jsonValue:(ItemType)itemType;
//-(NSMutableDictionary *) dictionaryWithPropertiesOfObject:(id)obj;
-(NSString *)getAddDinnerJsonValue;
@end
@interface ResultItemDetails : ItemDetails
@property(nonatomic,strong) NSString *distance;
@property(nonatomic,strong) NSString *sellerName;
@property(nonatomic,strong) NSString *listingId;
-(ResultItemDetails *)getItemResultFrom:(ItemDetails *)itemDeatils;
@end

