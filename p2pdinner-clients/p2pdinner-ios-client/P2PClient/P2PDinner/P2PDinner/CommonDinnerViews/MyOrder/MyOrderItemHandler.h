//
//  MyOrderItemHandler.h
//  P2PDinner
//
//  Created by Selvam M on 7/28/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <Foundation/Foundation.h>
typedef enum {
    kDinnerHistoryResult,
    kOrderSearchResult,
}SearchResultType;

@interface MyOrderItemHandler : NSObject
+ (id)sharedSellerHistoryHandler;
- (NSArray *)getMyOrderItemFromServiceResponce:(id)serviceResponce;
- (NSArray *)getResultsArryFromCartReceivedResponce:(id)serviceResponce;
- (NSArray *)getResultsArryForAllCurrentListRessponce:(id)serviceResponce forSeachResultType:(SearchResultType)searchResultType;
@end
