//
//  BuyerHandler.h
//  P2PDinner
//
//  Created by Selvam M on 9/4/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "ServiceHandler.h"
typedef void(^SearchResultBlock)(NSError *error, NSArray *response);
typedef void(^CheckoutRestultBlock)(NSError *error,NSString *cartId);
@interface BuyerHandler : ServiceHandler
+ (id)sharedBuyerHandler;
- (void)getSearchResultBasedOnLoactionAndFilterRequest:(NSString *)locationReq resultHandler:(SearchResultBlock)responseCallBack;
-(void)addCart:(NSString *)cartRequest withResponse:(CheckoutRestultBlock)cartResponceCallBack;
-(void)placeOrder:(NSString *)cartId withUserId:(NSString *)userId withResponse:(CheckoutRestultBlock)placeOrderResultCallBack;
-(void)addRating:(NSString *)cartRequest withCartId:(NSString *)cartId withResponse:(CheckoutRestultBlock)cartResponceCallBack;
@end
