//
//  CategoryItems.m
//  P2PDinner
//
//  Created by Selvam M on 3/27/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "CategoryItems.h"
#import "Utility.h"

@implementation CategoryItems

@end
//@property (nonatomic,strong)NSDate *startDate;
//@property (nonatomic,strong)NSDate *endDate;
//@property (nonatomic,strong)NSDate *modifiedDate;
//@property (nonatomic,assign)BOOL isActive;
//@property (nonatomic,strong)NSString *categoryId;
//@property (nonatomic,strong)NSString *name;


@implementation CategoryRespone
- (id)validateNull:(id)object{
    if (!(object == (id)[NSNull null])) {
        return object;
    }
    return @"";
}

- (NSArray *)getCategoryObjcet:(NSArray *)responce{
    NSMutableArray *returnArray=[[NSMutableArray alloc]init];
    
    [responce enumerateObjectsUsingBlock:^(id obj, NSUInteger idx, BOOL *stop) {
        NSDictionary *dictonary=[responce objectAtIndex:idx];
        CategoryItems *categoryItems=[[CategoryItems alloc]init];
        
        [categoryItems setStartDate:[Utility stringToDateFormat:@"MM/dd/yyyy HH:MM:SS" dateString:[self validateNull:[dictonary objectForKey:@"startDate"]]timeZone:LOCAL]];
        [categoryItems setEndDate:[Utility stringToDateFormat:@"MM/dd/yyyy HH:MM:SS" dateString:[self validateNull:[dictonary objectForKey:@"endDate"]]timeZone:LOCAL]];
        [categoryItems setModifiedDate:[Utility stringToDateFormat:@"MM/dd/yyyy HH:MM:SS" dateString:[self validateNull:[dictonary objectForKey:@"modifiedDate"]]timeZone:LOCAL]];
        [categoryItems setIsActive:[dictonary objectForKey:@"isActive"]];
        [categoryItems setCategoryId:[self validateNull:[dictonary objectForKey:@"id"]]];
        [categoryItems setName:[dictonary objectForKey:@"name"]];
        [returnArray addObject:categoryItems];
        
    }];
    
    
    return returnArray;
    
}
@end
