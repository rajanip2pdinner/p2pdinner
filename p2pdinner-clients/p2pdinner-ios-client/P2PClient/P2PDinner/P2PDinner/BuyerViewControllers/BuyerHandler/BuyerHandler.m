//
//  BuyerHandler.m
//  P2PDinner
//
//  Created by Selvam M on 9/4/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "BuyerHandler.h"
#import "ServiceConstants.h"
static ServiceHandler *_sharedInstance=nil;
@implementation BuyerHandler

+ (id)sharedBuyerHandler{
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        _sharedInstance = [[self alloc] init];
    });
    return _sharedInstance;
    
}

- (void)getSearchResultBasedOnLoactionAndFilterRequest:(NSString *)locationReq resultHandler:(SearchResultBlock)responseCallBack{
    NSString *conType=MIMETypeJSON
    NSString *requestType=RequestTypeGet
    locationReq = [locationReq stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    NSString *urlString=@"v1/places/nearbysearch?address=%@";
    urlString =[NSString stringWithFormat:urlString,locationReq];
    [self execute:urlString requestObject:locationReq contentType:conType requestMethod:requestType serviceCallBack:^(NSError *error, id response) {
        if (!error) {
            responseCallBack(nil,response);
        }
        
        
        else{
            responseCallBack(error,nil);
        }
    }];
}

-(void)addCart:(NSString *)cartRequest withResponse:(CheckoutRestultBlock)cartResponceCallBack{
    NSString *conType=MIMETypeJSON
    NSString *requestType=RequestTypePost
    //cartRequest = [cartRequest stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    NSString *urlString=@"v1/cart";
    [self execute:urlString requestObject:cartRequest contentType:conType requestMethod:requestType serviceCallBack:^(NSError *error, id response) {
        if (!error) {
            NSDictionary* responseDic=[(NSDictionary *)response objectForKey:@"response"];
            if ([[responseDic objectForKey:@"status"] isEqualToString:@"OK"]) {
                if ([[responseDic objectForKey:@"cartId"] isKindOfClass:[NSNumber class]]) {
                    cartResponceCallBack(nil,[[responseDic objectForKey:@"cartId"] stringValue]);
                } else  if ([[responseDic objectForKey:@"cartId"] isKindOfClass:[NSString class]]) {
                     cartResponceCallBack(nil,[responseDic objectForKey:@"cartId"]);
                }
            }else{
                cartResponceCallBack(nil,[responseDic objectForKey:@"status"]);
            }
            
        }else{
            cartResponceCallBack(error,nil);
        }
    }];

}
-(void)placeOrder:(NSString *)cartId withUserId:(NSString *)userId withResponse:(CheckoutRestultBlock)placeOrderResultCallBack{
    NSString *conType=MIMETypeJSON
    NSString *requestType=RequestTypePost
    NSString *urlString=@"v1/cart/placeorder/%@/%@";
    urlString =[NSString stringWithFormat:urlString,userId,cartId];
    [self execute:urlString requestObject:nil contentType:conType requestMethod:requestType serviceCallBack:^(NSError *error, id response) {
        if (!error) {
            NSDictionary* responseDic=[(NSDictionary *)response objectForKey:@"response"];
            if ([[responseDic objectForKey:@"status"] isEqualToString:@"OK"]) {
                placeOrderResultCallBack(nil,[responseDic objectForKey:@"message"]);
            }else{
                placeOrderResultCallBack(nil,[responseDic objectForKey:@"message"]);
            }
            
        }else{
            placeOrderResultCallBack(error,nil);
        }
    }];
    
}
-(void)addRating:(NSString *)cartRequest withCartId:(NSString *)cartId withResponse:(CheckoutRestultBlock)cartResponceCallBack{
    NSString *conType=MIMETypeJSON
    NSString *requestType=RequestTypePost
    NSString *urlString=[NSString stringWithFormat:@"v1/cart/%@",cartId];
    [self execute:urlString requestObject:cartRequest contentType:conType requestMethod:requestType serviceCallBack:^(NSError *error, id response) {
        if (!error) {
            NSDictionary* responseDic=[(NSDictionary *)response objectForKey:@"response"];
            if ([[responseDic objectForKey:@"status"] isEqualToString:@"OK"]) {
                if ([[responseDic objectForKey:@"cartId"] isKindOfClass:[NSNumber class]]) {
                    cartResponceCallBack(nil,[[responseDic objectForKey:@"cartId"] stringValue]);
                } else  if ([[responseDic objectForKey:@"cartId"] isKindOfClass:[NSString class]]) {
                    cartResponceCallBack(nil,[responseDic objectForKey:@"cartId"]);
                }
            }else{
                cartResponceCallBack(nil,[responseDic objectForKey:@"status"]);
            }
            
        }else{
            cartResponceCallBack(error,nil);
        }
    }];
    
}

@end
