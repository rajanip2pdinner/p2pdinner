//
//  MyOrderItemHandler.m
//  P2PDinner
//
//  Created by Selvam M on 7/28/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "MyOrderItemHandler.h"
#import "MyOrderItem.h"
#import "ItemDetails.h"
#import "Utility.h"

#define itemTitle @"title"
#define itemDescription @"description"
#define itemDelivery @"delivery"
#define itemSpecialNeeds @"special_needs"
#define itemCategories @"categories"
#define itemListing @"listing"
#define itemListingId @"listing_id"
#define itemQuantityAvialable @"quantity_available"
#define itemCostPerItem @"cost_per_item"
#define itemStartTime @"start_time"
#define itemCloseTime @"close_time"
#define itemEndDate @"end_date"
#define itemImage @"image_uri"

#define buyerName @"profile_name"
#define passCode @"pass_code"
#define totalPrice @"total_price"
#define orderQuantity @"order_quantity"
#define costPerItem @"cost_per_item"
#define deliveryType @"delivery_type"
#define addressLine1 @"address_line1"
#define addressLine2 @"address_line2"
#define city @"city"
#define state @"state"

static MyOrderItemHandler *_sharedInstance=nil;
@implementation MyOrderItemHandler
+ (id)sharedSellerHistoryHandler
{
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        _sharedInstance = [[self alloc] init];
    });
    return _sharedInstance;
}

- (NSArray *)getMyOrderItemFromServiceResponce:(id)serviceResponce{
    NSArray *returnArray;
    if ([serviceResponce isKindOfClass:[NSArray class]]) {
        returnArray=[self parseArrayFromArrayResponce:(NSArray *)serviceResponce];
    }
    else if ([serviceResponce isKindOfClass:[NSDictionary class]]) {
        returnArray =[NSArray arrayWithObject:serviceResponce];
    }
    return returnArray;
}
- (NSArray *)parseArrayFromArrayResponce:(NSArray *)responceArray{
    NSMutableArray *mutableArray=[[NSMutableArray alloc]init];
    for (int i=0; i<[responceArray count]; i++) {
        MyOrderItem *myOrderItem=[[MyOrderItem alloc]init];
        if ([[responceArray objectAtIndex:i] isKindOfClass:[NSDictionary class]]) {
            NSDictionary *responceDic=(NSDictionary *)[responceArray objectAtIndex:i];
            [myOrderItem setTitle:[responceDic objectForKey:itemTitle]];
            [myOrderItem setItemDescription:[responceDic objectForKey:itemDescription]];
            [myOrderItem setDelivery:[responceDic objectForKey:itemDelivery]];
            [myOrderItem setSpecialNeeds:[responceDic objectForKey:itemSpecialNeeds]];
            [myOrderItem setCategories:[responceDic objectForKey:itemCategories]];
            NSDictionary *ItemListingDic=(NSDictionary *)[responceDic objectForKey:itemListing];
            //
            ItemListing *itemListion=[[ItemListing alloc]init];
            [itemListion setItemListing:[ItemListingDic objectForKey:itemListingId]];
            [itemListion setQuantyAvailabel:[ItemListingDic objectForKey:itemQuantityAvialable]];
            [itemListion setCostPerItem:[ItemListingDic objectForKey:itemCostPerItem]];
            [itemListion setStartTime:[ItemListingDic objectForKey:itemStartTime]];
            [itemListion setCloseTime:[ItemListingDic objectForKey:itemCloseTime]];
            [itemListion setEndDate:[ItemListingDic objectForKey:itemEndDate]];
            [itemListion setQuantityOrdered:[ItemListingDic objectForKey:@"ordered_quantity"]];
            [myOrderItem setListing:itemListion];
            
        }
        [mutableArray addObject:myOrderItem];
    }
    
    return mutableArray;
}
- (CarRecivedItemDetail *)getCartReceivedItem:(NSDictionary *)cartRecivedDictionary{
    CarRecivedItemDetail *cartRecivedItem=[[CarRecivedItemDetail alloc]init];
    if (![cartRecivedDictionary objectForKey:@"name"]) {
        [cartRecivedItem setBuyerName:[cartRecivedDictionary objectForKey:buyerName]];
    }else
    [cartRecivedItem setBuyerName:[cartRecivedDictionary objectForKey:@"name"]];
    [cartRecivedItem setTitle:[cartRecivedDictionary objectForKey:itemTitle]];
    [cartRecivedItem setItemDescription:[cartRecivedDictionary objectForKey:itemDescription]];
    [cartRecivedItem setPassCode:[cartRecivedDictionary objectForKey:passCode]];
    [cartRecivedItem setTotalPrice:[cartRecivedDictionary objectForKey:totalPrice]];
    [cartRecivedItem setOrderQuantity:[cartRecivedDictionary objectForKey:orderQuantity]];
    [cartRecivedItem setCostPerItem:[cartRecivedDictionary objectForKey:costPerItem]];
    [cartRecivedItem setDeliveryType:[cartRecivedDictionary objectForKey:deliveryType]];
    [cartRecivedItem setAddress_line1:[cartRecivedDictionary objectForKey:addressLine1]];
    [cartRecivedItem setAddress_line2:[cartRecivedDictionary objectForKey:addressLine2]];
    [cartRecivedItem setCity:[cartRecivedDictionary objectForKey:city]];
    [cartRecivedItem setState:[cartRecivedDictionary objectForKey:state]];
    [cartRecivedItem setImageUri:[cartRecivedDictionary objectForKey:@"image_uri"]];
    [cartRecivedItem setStartTime:[cartRecivedDictionary objectForKey:@"start_time"]];
    [cartRecivedItem setEndTime:[cartRecivedDictionary objectForKey:@"end_time"]];
    [cartRecivedItem setCloseTime:[cartRecivedDictionary objectForKey:@"close_time"]];
    if ([Utility validateNilObject:[cartRecivedDictionary objectForKey:@"buyer_rating"]])
            [cartRecivedItem setBuyer_rating:[NSNumber numberWithInt:0]];
        else
            [cartRecivedItem setBuyer_rating:[cartRecivedDictionary objectForKey:@"buyer_rating"]];
    if ([Utility validateNilObject:[cartRecivedDictionary objectForKey:@"seller_rating"]])
            [cartRecivedItem setSeller_rating:[NSNumber numberWithInt:0]];
        else
             [cartRecivedItem setSeller_rating:[cartRecivedDictionary objectForKey:@"seller_rating"]];
    [cartRecivedItem setCart_id:[cartRecivedDictionary objectForKey:@"cart_id"]];
    return cartRecivedItem;
    
}
- (NSArray *)parseArrayFromArrayOfCartReceivedResponce:(NSArray *)cartReceivedArray{
    NSMutableArray *returnArray=[[NSMutableArray alloc]init];
    for (int i=0; i<[cartReceivedArray count]; i++) {
        id cartRecivedArray1=[cartReceivedArray objectAtIndex:i];
        if ([cartRecivedArray1 isKindOfClass:[NSArray class]]) {
            for (int j=0; j<[cartRecivedArray1 count]; j++) {
                [returnArray addObject:[self getCartReceivedItem:(NSDictionary *)[cartRecivedArray1 objectAtIndex:j]]];
            }
        }
        else if ([cartRecivedArray1 isKindOfClass:[NSDictionary class]]){
            [returnArray addObject:[self getCartReceivedItem:(NSDictionary *)cartRecivedArray1]];
        }
    }
    return returnArray;
    
}
#warning need to handle error scenario
- (NSArray *)getResultsArryFromCartReceivedResponce:(id)serviceResponce{
    NSArray *returnArray;
    NSDictionary *reponceDictonary=(NSDictionary *)serviceResponce;
    if ([[reponceDictonary objectForKey:@"results"] isKindOfClass:[NSArray class]]) {
        NSArray *arrayToParse=(NSArray *)[reponceDictonary objectForKey:@"results"];
        returnArray=[self parseArrayFromArrayOfCartReceivedResponce:arrayToParse];
        
    }
    return returnArray;
}

-(ItemDetails *)getItemDetailByUser:(NSDictionary *)menuItemsDictionary{
        ItemDetails *myOrderItem=[[ItemDetails alloc]init];
        NSDictionary *responceDic=menuItemsDictionary;
        [myOrderItem setTitle:[responceDic objectForKey:itemTitle]];
        [myOrderItem setDinnerDescription:[responceDic objectForKey:itemDescription]];
        [myOrderItem setDinnerDelivery:[[[responceDic objectForKey:itemDelivery] valueForKey:@"description"] componentsJoinedByString:@","]];
        if ([[responceDic objectForKey:itemSpecialNeeds] isKindOfClass:[NSArray class]]) {
            [myOrderItem setDinnerSpecialNeeds:[[responceDic objectForKey:itemSpecialNeeds] componentsJoinedByString:@","]];
        }else
            [myOrderItem setDinnerSpecialNeeds:[responceDic objectForKey:itemSpecialNeeds]];
        if ([[responceDic objectForKey:itemCategories] isKindOfClass:[NSArray class]]) {
            [myOrderItem setDinnerCategories:[[responceDic objectForKey:itemCategories] componentsJoinedByString:@","]];
        }else
            [myOrderItem setDinnerCategories:[responceDic objectForKey:itemCategories]];
        NSDictionary *ItemListingDic=(NSDictionary *)[responceDic objectForKey:itemListing];
        
        [myOrderItem setAvailableQuantity:[ItemListingDic objectForKey:itemQuantityAvialable]];
        [myOrderItem setCostPerItem:[ItemListingDic objectForKey:itemCostPerItem]];
        [myOrderItem setStartDate:[ItemListingDic objectForKey:itemStartTime]];
        [myOrderItem setCloseDate:[ItemListingDic objectForKey:itemCloseTime]];
        [myOrderItem setEndDate:[ItemListingDic objectForKey:itemEndDate]];
        [myOrderItem setImageUri:[responceDic objectForKey:itemImage]];
    
    return myOrderItem;
    
}

- (ItemDetails *)getItemDetailsFromResponse:(NSDictionary *)responceArray{
    ItemDetails *myOrderItem=[[ItemDetails alloc]init];
    if ([responceArray isKindOfClass:[NSDictionary class]]) {
        NSDictionary *responceDic=[[responceArray objectForKey:@"menu_items"] objectAtIndex:0];
        [myOrderItem setTitle:[responceDic objectForKey:itemTitle]];
        
        [myOrderItem setDinnerDescription:[responceDic objectForKey:itemDescription]];
        [myOrderItem setDinnerDelivery:[[[responceDic objectForKey:itemDelivery] valueForKey:@"description"] componentsJoinedByString:@","]];
        if ([[responceDic objectForKey:itemSpecialNeeds] isKindOfClass:[NSArray class]]) {
            [myOrderItem setDinnerSpecialNeeds:[[responceDic objectForKey:itemSpecialNeeds] componentsJoinedByString:@","]];
        }else
            [myOrderItem setDinnerSpecialNeeds:[responceDic objectForKey:itemSpecialNeeds]];
        if ([[responceDic objectForKey:itemCategories] isKindOfClass:[NSArray class]]) {
            [myOrderItem setDinnerCategories:[[responceDic objectForKey:itemCategories] componentsJoinedByString:@","]];
        }else
            [myOrderItem setDinnerCategories:[responceDic objectForKey:itemCategories]];
        NSDictionary *ItemListingDic=(NSDictionary *)[responceDic objectForKey:itemListing];
        
        [myOrderItem setAvailableQuantity:[ItemListingDic objectForKey:itemQuantityAvialable]];
        [myOrderItem setCostPerItem:[ItemListingDic objectForKey:itemCostPerItem]];
        [myOrderItem setStartDate:[ItemListingDic objectForKey:itemStartTime]];
        [myOrderItem setCloseDate:[ItemListingDic objectForKey:itemCloseTime]];
        [myOrderItem setEndDate:[ItemListingDic objectForKey:itemEndDate]];
        [myOrderItem setImageUri:[responceDic objectForKey:itemImage]];
        
        return myOrderItem;
    }
    return nil;
}
-(NSArray *)getResultItemsFromCurrentSearchNearListResponce:(NSArray *)responceArray{
      NSMutableArray *arrayOfResult=[[NSMutableArray alloc]init];
    for (int j=0; j<[responceArray count]; j++){
         NSDictionary *responceDictonary =[responceArray objectAtIndex:j];
        
        if ([responceDictonary isKindOfClass:[NSDictionary class]]) {
            NSArray *arrayOfMenuItem=[responceDictonary objectForKey:@"menu_items"];
            for (int i=0; i<[arrayOfMenuItem count]; i++) {
                ResultItemDetails *myOrderItem=[[ResultItemDetails alloc]init];
                myOrderItem=[myOrderItem getItemResultFrom:[self getItemDetailByUser:[arrayOfMenuItem objectAtIndex:i]]];
                NSDictionary *responceDic=[arrayOfMenuItem objectAtIndex:i];
                [myOrderItem setSellerName:[responceDic objectForKey:@"profile_name"]];
                [myOrderItem setUserId:[responceDic objectForKey:@"profile_id"]];
                NSDictionary *location=[responceDic objectForKey:@"location"];
                NSDictionary *itemListingDic=[responceDic objectForKey:itemListing];
                [myOrderItem setDistance:[location objectForKey:@"distance"]];
                [myOrderItem setAddressLine1:[location objectForKey:@"address"]];
                [myOrderItem setListingId:[itemListingDic objectForKey:@"listing_id"]];
                [arrayOfResult addObject:myOrderItem];
            }
            
        }
    }
  
       return arrayOfResult;
}

- (NSArray *)parseCurrentSearchNearListResponce:(NSArray *)responceArray{
    NSMutableArray *mutableArray=[[NSMutableArray alloc]init];
    for (int i=0; i<[responceArray count]; i++) {
        ResultItemDetails *myOrderItem=[[ResultItemDetails alloc]init];
        myOrderItem=[myOrderItem getItemResultFrom:[self getItemDetailsFromResponse:[responceArray objectAtIndex:i]]];
        
        
        
        
        
        
        //Get extra name and location value
        NSDictionary *responceDic=[[[responceArray objectAtIndex:i] objectForKey:@"menu_items"] objectAtIndex:0];
        [myOrderItem setSellerName:[responceDic objectForKey:@"profile_name"]];
        NSDictionary *location=[responceDic objectForKey:@"location"];
        [myOrderItem setDistance:[location objectForKey:@"distance"]];
        [myOrderItem setAddressLine1:[location objectForKey:@"address"]];
        [mutableArray addObject:myOrderItem];
    }
    
    return mutableArray;
}
- (NSArray *)getResultsArryForAllCurrentListRessponce:(id)serviceResponce forSeachResultType:(SearchResultType)searchResultType{
    if ([serviceResponce isKindOfClass:[NSArray class]]) {
        NSArray *responceArray=(NSArray *)serviceResponce;
        if (searchResultType==kOrderSearchResult) {
            return [self getResultItemsFromCurrentSearchNearListResponce:responceArray];
        }else{
            return [self parseCurrentListResponce:responceArray];
        }
        
        
    }
    return @[];
}
- (NSArray *)parseCurrentListResponce:(NSArray *)responceArray{
    NSMutableArray *mutableArray=[[NSMutableArray alloc]init];
    for (int i=0; i<[responceArray count]; i++) {
        ItemDetails *myOrderItem=[self getItemDetailsFromResponse:[responceArray objectAtIndex:i]];
        
        [mutableArray addObject:myOrderItem];
    }
    
    return mutableArray;
}
@end
