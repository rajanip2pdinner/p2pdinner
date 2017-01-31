//
//  CategoryItems.h
//  P2PDinner
//
//  Created by Selvam M on 3/27/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <Foundation/Foundation.h>
//{
//    "startDate":"12/21/2014 12:00:00",
//    "endDate":null,
//    "modifiedDate":"12/21/2014 12:00:00",
//    "isActive":true,
//    "id":1,
//    "name":"Mexican"
//},

@interface CategoryItems : NSObject

@property (nonatomic,strong)NSDate *startDate;
@property (nonatomic,strong)NSDate *endDate;
@property (nonatomic,strong)NSDate *modifiedDate;

@property (nonatomic,strong)NSString *categoryId;
@property (nonatomic,strong)NSString *name;

@property (nonatomic,assign)BOOL isActive;

@end
@interface CategoryRespone : NSObject
- (NSArray *)getCategoryObjcet:(NSArray *)responce;
@end
