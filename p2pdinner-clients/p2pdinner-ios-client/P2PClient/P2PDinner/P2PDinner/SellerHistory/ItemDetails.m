//
//  ItemDetails.m
//  P2PDinner
//
//  Created by Selvam M on 3/9/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "ItemDetails.h"
#import <objc/runtime.h>
#import "Utility.h"
@interface ItemDetails()

@end
@implementation ItemDetails
@synthesize dinnerId=_id;
@synthesize dinnerDescription=_description;

- (NSString *)checknilValue:(id)dicVal
{
    if ([dicVal isKindOfClass:[NSNull class]]) {
        return @"";
    }
    return dicVal;
}
- (NSString *)setDefaultDate:(id)dicVal{
    
    if ([dicVal isKindOfClass:[NSNull class]]) {
        return [Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:[NSDate date] timeZone:LOCAL];
    }
    return dicVal;
}
- (NSNumber *)setDefaultNumber:(id)dicVal{
    
    if ([dicVal isKindOfClass:[NSNull class]]) {
        return [NSNumber numberWithInt:5];
    }
    return dicVal;
}
- (ItemDetails *)setDinnerDetails:(NSDictionary *)dinner{
    NSString *userId=[[NSUserDefaults standardUserDefaults]objectForKey:@"userId"];
    ItemDetails *itemDetails=[[ItemDetails alloc]init];
    [itemDetails setTitle:[dinner objectForKey:@"title"]];
    [itemDetails setDinnerDescription:[self checknilValue:[dinner objectForKey:@"description"]]];
    [itemDetails setUserId:[NSNumber numberWithInteger:[userId integerValue]]];
    [itemDetails setIsActive:[[dinner objectForKey:@"isActive"] boolValue]];
    [itemDetails setDinnerCategories:[self checknilValue:[dinner objectForKey:@"dinnerCategories"]]];
    [itemDetails setDinnerSpecialNeeds:[self checknilValue:[dinner objectForKey:@"dinnerSpecialNeeds"]]];
    [itemDetails setDinnerDelivery:[self checknilValue:[dinner objectForKey:@"dinnerDelivery"]]];
    [itemDetails setDinnerId:[dinner objectForKey:@"id"]];
    
    [itemDetails setAddressLine1:[self checknilValue:[dinner objectForKey:@"addressLine1"]]];
    [itemDetails setAddressLine2:[self checknilValue:[dinner objectForKey:@"addressLine2"]]];
    [itemDetails setZipCode:[self checknilValue:[dinner objectForKey:@"zipCode"]]];
    [itemDetails setState:[self checknilValue:[dinner objectForKey:@"state"]]];
    [itemDetails setCity:[self checknilValue:[dinner objectForKey:@"city"]]];
    [itemDetails setAvailableQuantity:[self setDefaultNumber:[dinner objectForKey:@"availableQuantity"]]];
    [itemDetails setCostPerItem:[self setDefaultNumber:[dinner objectForKey:@"costPerItem"]]];
    [itemDetails setImageUri:[self checknilValue:[dinner objectForKey:@"imageUri"]]];
    [itemDetails setStartDate:[self setDefaultDate:[dinner objectForKey:@"startDate"]]];
    [itemDetails setEndDate:[self setDefaultDate:[dinner objectForKey:@"endDate"]]];
    [itemDetails setCloseDate:[self setDefaultDate:[dinner objectForKey:@"closeDate"]]];
    
    return itemDetails;
}
- (AddDinnerListItem *)getAddDinnerListItem{
    AddDinnerListItem *addDinnerListItem=[[AddDinnerListItem alloc]init];
    [addDinnerListItem setStartTime:[self startDate]];
    [addDinnerListItem setEndTime:[self endDate]];
    [addDinnerListItem setCloseTime:[self closeDate]];
    [addDinnerListItem setAvailableQuantity:[self availableQuantity]];
    [addDinnerListItem setCostPerItem:[self costPerItem]];
    [addDinnerListItem setMenuItemId:[[self dinnerId] stringValue]];
    return addDinnerListItem;
}
- (NSString *)getAddDinnerJsonValue{
    return [Utility jsonToDictionary:[Utility dictionaryWithPropertiesOfObject:[self getAddDinnerListItem]]];
}

-(NSArray *)getHistoryDinnerDetails:(NSArray *)dinner{
    NSMutableArray *returnArray=[[NSMutableArray alloc]init];
    for (int i=0; i<[dinner count]; i++) {
        NSDictionary *dict=[dinner objectAtIndex:i];
        [returnArray addObject:[self setDinnerDetails:dict]];
        
    }
    return returnArray;
}
- (void)setUpUTC{
    self.startDate=[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:[Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:self.startDate timeZone:UTC] timeZone:UTC];
    self.endDate=[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:[Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:self.endDate timeZone:UTC] timeZone:UTC];
    self.closeDate=[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:[Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:self.closeDate timeZone:UTC] timeZone:UTC];
}
- (NSString *)jsonValue:(ItemType)itemType{
    NSMutableString *returnString;
    NSMutableDictionary *objectDict=[Utility dictionaryWithPropertiesOfObject:self];
    
    if (itemType==CreatNewItem) {
        [objectDict removeObjectForKey:@"id"];
    }
    [self setUpUTC];
    NSError *error=nil;
    NSData *jsonData=[NSJSONSerialization dataWithJSONObject:objectDict options:NSJSONWritingPrettyPrinted error:&error];
    
    if (!jsonData) {
        NSLog(@"bv_jsonStringWithPrettyPrint: error: %@", error.localizedDescription);
        return @"{}";
    }
    else
    {
        returnString =[NSMutableString stringWithString:[[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding]];
    }
    
    
    return returnString;
}
@end
@implementation ResultItemDetails

- (ResultItemDetails *)getItemResultFrom:(ItemDetails *)itemDeatils{
    self.title=itemDeatils.title;
    self.dinnerDescription=itemDeatils.dinnerDescription;
    self.userId=itemDeatils.userId;
    self.isActive=itemDeatils.isActive;
    self.dinnerCategories=itemDeatils.dinnerCategories;
    self.dinnerSpecialNeeds=itemDeatils.dinnerSpecialNeeds;
    self.dinnerDelivery=itemDeatils.dinnerDelivery;
    self.dinnerId=itemDeatils.dinnerId;
    //
    self.addressLine1=itemDeatils.addressLine1;
    self.addressLine2=itemDeatils.addressLine2;
    self.zipCode=itemDeatils.zipCode ;
    self.state=itemDeatils.state;
    self.city=itemDeatils.city;
    self.availableQuantity=itemDeatils.availableQuantity;
    self.costPerItem=itemDeatils.costPerItem;
    self.imageUri=itemDeatils.imageUri;
    self.startDate=itemDeatils.startDate;
    self.endDate=itemDeatils.endDate;
    self.closeDate=itemDeatils.endDate;
    
    return self;
}

@end
