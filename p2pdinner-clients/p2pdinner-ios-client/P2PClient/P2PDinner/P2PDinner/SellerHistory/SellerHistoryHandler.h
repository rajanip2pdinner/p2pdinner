//
//  SellerHistoryHandler.h
//  P2PDinner
//
//  Created by Selvam M on 3/9/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "ServiceHandler.h"
#import "ItemDetails.h"
#import <UIKit/UIKit.h>
typedef void(^SellerHistoryResultBlock)(NSError *error, NSArray *response);
typedef void(^SellerItemResultBlock)(NSError *error, ItemDetails *response);
typedef void(^addDinnerListingResultBlock)(NSError *error, AddDinnerListItem *response);
typedef void(^ImageUplaodResultBlock)(NSError *error, NSString *imageString);
@interface SellerHistoryHandler : ServiceHandler
{
    NSString *requestType;
    NSString *conType;
}
+(id)sharedSellerHistoryHandler;
-(void)photoUpload:(UIImage *)dinnerImage imageTag:(NSInteger)imageTag buttonValue:(UIButton *)imageButton itemDetails:(ItemDetails *)itemDetail responceCallBack:(ImageUplaodResultBlock)responseBlock;
-(void)getUserHistory:(NSString *)useId serviceCallBack:(SellerHistoryResultBlock)service;
//-(void)createMenuItem:(ItemDetails *)itemDetails;
-(void)updateMenuItem:(ItemDetails *)itemDetails serviceCallBack:(SellerItemResultBlock)service;
//
-(void)getItemListing:(NSString *)request serviceCallBack:(SellerHistoryResultBlock)service;

-(void)getMyOrderReceived:(NSString *)request serviceCallBack:(SellerHistoryResultBlock)service;

- (void)getMyOldOrderReceived:(NSString *)request serviceCallBack:(SellerHistoryResultBlock)service;

-(void)addDinnerListingMenuItem:(ItemDetails *)itemDetails serviceCallBack:(addDinnerListingResultBlock)service;

-(void)getAllItemListing:(NSString *)request serviceCallBack:(SellerHistoryResultBlock)service;
@end
