//
//  CategoryServiceHandler.h
//  P2PDinner
//
//  Created by Selvam M on 3/27/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "ServiceHandler.h"
typedef void(^CategoryResponceBlock)(NSError *,NSArray *);
@interface CategoryServiceHandler : ServiceHandler
- (void)getCategoryListItem:(CategoryResponceBlock)categoryResponceBlock;
@end
