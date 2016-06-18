//
//  ItemDetailsShared.h
//  P2PDinner
//
//  Created by Selvam M on 7/12/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ItemDetails.h"
@interface ItemDetailsShared : NSObject
@property(nonatomic,strong)ItemDetails *sharedItemDetailsValue;
+ (id)sharedItemDetails;
@end
