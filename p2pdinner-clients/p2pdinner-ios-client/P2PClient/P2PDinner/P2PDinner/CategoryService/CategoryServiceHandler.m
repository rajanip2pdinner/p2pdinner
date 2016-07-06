//
//  CategoryServiceHandler.m
//  P2PDinner
//
//  Created by Selvam M on 3/27/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "CategoryServiceHandler.h"
#import "CategoryItems.h"

@implementation CategoryServiceHandler
- (void)getCategoryListItem:(CategoryResponceBlock)categoryResponceBlock{
    
    
    [self execute:@"https://dev-p2pdinner-services.herokuapp.com/api/v1/dinnercategory/view" requestObject:@"" contentType:@"application/json" requestMethod:@"GET" serviceCallBack:^(NSError *error, id response) {
        
        if (!error) {
            CategoryRespone *categoryRespnce=[[CategoryRespone alloc]init];
            categoryResponceBlock(error,[categoryRespnce getCategoryObjcet:(NSArray *)response]);
            return ;
        }
        categoryResponceBlock(error,nil);
        
        
    }];
}
@end
