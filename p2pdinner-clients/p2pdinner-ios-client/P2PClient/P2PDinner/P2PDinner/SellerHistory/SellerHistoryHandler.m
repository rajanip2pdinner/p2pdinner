//
//  SellerHistoryHandler.m
//  P2PDinner
//
//  Created by Selvam M on 3/9/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//
//Gokul 163
//Selvam 144

#import "SellerHistoryHandler.h"
#import "AFHTTPRequestOperationManager.h"
#import "MyOrderItemHandler.h"
static ServiceHandler *_sharedInstance=nil;
@implementation SellerHistoryHandler
+ (id)sharedSellerHistoryHandler
{
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        _sharedInstance = [[self alloc] init];
    });
    return _sharedInstance;
}
- (void)getUserHistory:(NSString *)useId serviceCallBack:(SellerHistoryResultBlock)service{
    requestType=RequestTypeGet;
    conType=MIMETypeJSON;
    NSString *urlString=[NSString stringWithFormat:@"https://p2pdinner-services.herokuapp.com/api/v1/menu/view/%@",useId];
    [self execute:urlString requestObject:useId contentType:conType requestMethod:requestType serviceCallBack:^(NSError *error, id response) {
        NSArray *arrayOfObject=(NSArray *)response;
        if (!error) {
            if ([arrayOfObject count]>0) {
                ItemDetails *itemDetails=[[ItemDetails alloc]init];
                arrayOfObject=[itemDetails getHistoryDinnerDetails:arrayOfObject];
                //service(nil,arrayOfObject);
            }
            
            service(nil,arrayOfObject);
            
            
        } else
            service(error,response);
        
    }];
}
- (void)addDinnerListingMenuItem:(ItemDetails *)itemDetails serviceCallBack:(addDinnerListingResultBlock)service{
    [UIApplication sharedApplication].networkActivityIndicatorVisible=YES;
    requestType=RequestTypePost;
    conType=MIMETypeJSON;
    NSString *requestObject=[itemDetails getAddDinnerJsonValue];
    NSLog(@"%@",[itemDetails getAddDinnerJsonValue]);
    NSString *urlString=[NSString stringWithFormat:@"https://p2pdinner-services.herokuapp.com/api/v1/listing/add"];
    
    // NSLog(@"\nRequest : \n%@\n\n",requestObject);
    [self execute:urlString requestObject:requestObject contentType:conType requestMethod:requestType serviceCallBack:^(NSError *error, id response) {
        [UIApplication sharedApplication].networkActivityIndicatorVisible=NO;
        if(!error){
            if ([[response objectForKey:@"code"] isEqualToString:@"p2pdinner.invalid.starttime"]||[[response objectForKey:@"code"] isEqualToString:@"p2pdinner.missing.orderdate"]||[[response objectForKey:@"code"] isEqualToString:@"p2pdinner.invalid.closetime"]) {
                NSError *error=[NSError errorWithDomain:@"p2pdinner.add_to_list_invalid_dates" code:420 userInfo:[NSDictionary dictionaryWithObject:[response objectForKey:@"message"] forKey:@"message"]];
                service(error,nil);
            }else if ([[response objectForKey:@"code"] isEqualToString:@"p2pdinner.missing.title"]){
                NSError *error=[NSError errorWithDomain:@"p2pdinner.missing.title" code:421 userInfo:[NSDictionary dictionaryWithObject:[response objectForKey:@"message"] forKey:@"message"]];
                service(error,nil);
            }
            else if ([[response objectForKey:@"code"] isEqualToString:@"p2pdinner.invalid.category"]){
                NSError *error=[NSError errorWithDomain:@"p2pdinner.invalid.category" code:422 userInfo:[NSDictionary dictionaryWithObject:[response objectForKey:@"message"] forKey:@"message"]];
                service(error,nil);
            }
            else if ([[response objectForKey:@"code"] isEqualToString:@"p2pdinner.invalid.address"]){
                NSError *error=[NSError errorWithDomain:@"p2pdinner.invalid.address" code:423 userInfo:[NSDictionary dictionaryWithObject:[response objectForKey:@"message"] forKey:@"message"]];
                service(error,nil);
                
            }
            else{
                if ([response isKindOfClass:[NSDictionary class]]) {
                    AddDinnerListItem *addDinnerListItem=[[AddDinnerListItem alloc]init];
                    service(nil,[addDinnerListItem createAddDinnerItemFromDictionary:response]);
                }
            }
        }
        else
            service(error,nil);
        // NSLog(@"\nResponse : \n%@\n\n",requestObject);
        
    }];
    
    
}
- (void)updateMenuItem:(ItemDetails *)itemDetails serviceCallBack:(SellerItemResultBlock)service{
    [UIApplication sharedApplication].networkActivityIndicatorVisible=YES;
    requestType=RequestTypePost;
    conType=MIMETypeJSON;
    NSString *requestObject;
    if ([itemDetails.dinnerId intValue]==0) {
        NSLog(@"Need to register New");
        requestObject=[itemDetails jsonValue:CreatNewItem];
    }else{
        requestObject=[itemDetails jsonValue:UpdateOldItem];
    }
    NSString *urlString=[NSString stringWithFormat:@"https://p2pdinner-services.herokuapp.com/api/v1/menu/add"];
    
    // NSLog(@"\nRequest : \n%@\n\n",requestObject);
    [self execute:urlString requestObject:requestObject contentType:conType requestMethod:requestType serviceCallBack:^(NSError *error, id response) {
        [UIApplication sharedApplication].networkActivityIndicatorVisible=NO;
        if ([response isKindOfClass:[NSDictionary class]]) {
            if ([[response objectForKey:@"code"] isEqualToString:@"p2pdinner.missing.title"]){
                NSError *error=[NSError errorWithDomain:@"p2pdinner.missing.title" code:421 userInfo:[NSDictionary dictionaryWithObject:[response objectForKey:@"message"] forKey:@"message"]];
                service(error,nil);
            }else if ([[response objectForKey:@"code"] isEqualToString:@"p2pdinner.invalid.category"]){
                NSError *error=[NSError errorWithDomain:@"p2pdinner.invalid.category" code:422 userInfo:[NSDictionary dictionaryWithObject:[response objectForKey:@"message"] forKey:@"message"]];
                service(error,nil);
            }
            else if ([[response objectForKey:@"code"] isEqualToString:@"p2pdinner.invalid.address"]){
                NSError *error=[NSError errorWithDomain:@"p2pdinner.invalid.address" code:423 userInfo:[NSDictionary dictionaryWithObject:[response objectForKey:@"message"] forKey:@"message"]];
                service(error,nil);
                
            }else
            {
                ItemDetails *itemDeatilsTemp=[[ItemDetails alloc]init];
                service(nil,[itemDeatilsTemp setDinnerDetails:response]);
            }
        }
        else
            service(error,nil);
        // NSLog(@"\nResponse : \n%@\n\n",requestObject);
        
    }];
}
- (NSString *)getImageNameFromUploadedURL:(NSDictionary *)imagUploadDictonary{
    if([[imagUploadDictonary objectForKey:@"status"] isEqualToString:@"OK"]){
        NSString *imageURL=[imagUploadDictonary objectForKey:@"url"];
        return [[[[imageURL componentsSeparatedByString:@"?"] objectAtIndex:0] componentsSeparatedByString:@"/"]lastObject];
    }
    else return @"Error";
    
}
- (NSString *)updatePhotoName:(ItemDetails *)itemDetails imageName:(NSString *)imageName{
    if ([itemDetails.imageUri length]>0) {
        return [NSString stringWithFormat:@"%@,%@",itemDetails.imageUri,imageName];
    }
    else{
        return [NSString stringWithFormat:@"%@",imageName];
    }
    
}
- (void)photoUpload:(UIImage *)dinnerImage imageTag:(NSInteger)imageTag buttonValue:(UIButton *)imageButton itemDetails:(ItemDetails *)itemDetail responceCallBack:(ImageUplaodResultBlock)response{
    [UIApplication sharedApplication].networkActivityIndicatorVisible=YES;
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    NSData *imageData=UIImageJPEGRepresentation(dinnerImage, 1.0);
    
    NSDictionary *dictonary=[NSDictionary dictionaryWithObjectsAndKeys:[NSString stringWithFormat:@"%ld",(long)imageTag],@"userInfo", nil];
    
    [manager POST:@"https://p2pdinner-services.herokuapp.com/api/v1/menu/upload" parameters:dictonary constructingBodyWithBlock:^(id<AFMultipartFormData> formData) {
        [formData appendPartWithFileData:imageData
                                    name:@"imagefile"
                                fileName:@"dinnerImage.png" mimeType:@"image/jpeg"];
        // etc.
    } success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [UIApplication sharedApplication].networkActivityIndicatorVisible=NO;
        NSLog(@"Response: %@", responseObject);
        if ([responseObject isKindOfClass:[NSDictionary class]]) {
            
            if ([[responseObject objectForKey:@"message"] isEqualToString:@"Unknown error"]) {
                
                NSError *error = [NSError errorWithDomain:@"world" code:200 userInfo:responseObject];
                response(error,nil);
            }else
            {
               // NSString *imageName=[self getImageNameFromUploadedURL:(NSDictionary *)responseObject];
                response(nil,[NSString stringWithFormat:@"\"%@\"",[responseObject objectForKey:@"url"]]);
            }
        }
        [imageButton setTitle:@"" forState:UIControlStateNormal];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [UIApplication sharedApplication].networkActivityIndicatorVisible=NO;
        [imageButton setTitle:@"Fail!" forState:UIControlStateNormal];
        response(error,nil);
        NSLog(@"Error: %@", error);
    }];
}
- (void)getItemListing:(NSString *)request serviceCallBack:(SellerHistoryResultBlock)service{
    conType=MIMETypeJSON
    requestType=RequestTypeGet
    NSString *urlString=@"https://p2pdinner-services.herokuapp.com/api/v1/listing/view/";
    // urlString=[NSString stringWithFormat:@"%@2015-05-06/144",urlString];
    urlString=[NSString stringWithFormat:@"%@%@",urlString,request];
    urlString = [urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    [self execute:urlString requestObject:@"" contentType:conType requestMethod:requestType serviceCallBack:^(NSError *error, id response) {
        [[UIApplication sharedApplication]setNetworkActivityIndicatorVisible:NO];
        if (!error) {
            service(nil,[[MyOrderItemHandler sharedSellerHistoryHandler] getMyOrderItemFromServiceResponce:response]);
        }
        else
            service(error,nil);
    }];
}
- (void)getMyOldOrderReceived:(NSString *)request serviceCallBack:(SellerHistoryResultBlock)service{
    conType=MIMETypeJSON
    requestType=RequestTypeGet
    request=[request stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    NSString *urlString=@"https://p2pdinner-services.herokuapp.com/api/v1/cart/placedorders/";
    urlString=[NSString stringWithFormat:@"%@%@",urlString,request];
    [self execute:urlString requestObject:@"" contentType:conType requestMethod:requestType serviceCallBack:^(NSError *error, id response) {
        [[UIApplication sharedApplication]setNetworkActivityIndicatorVisible:NO];
        if (!error) {
            service(nil,[[MyOrderItemHandler sharedSellerHistoryHandler] getResultsArryFromCartReceivedResponce:response]);
        }
        
        
        else{
            service(error,nil);
        }
    }];
}
- (void)getMyOrderReceived:(NSString *)request serviceCallBack:(SellerHistoryResultBlock)service{
    conType=MIMETypeJSON
    requestType=RequestTypeGet
    
    NSString *urlString=@"https://p2pdinner-services.herokuapp.com/api/v1/cart/orders/";
    urlString=[NSString stringWithFormat:@"%@%@",urlString,request];
    [self execute:urlString requestObject:@"" contentType:conType requestMethod:requestType serviceCallBack:^(NSError *error, id response) {
        [[UIApplication sharedApplication]setNetworkActivityIndicatorVisible:NO];
        if (!error) {
            service(nil,[[MyOrderItemHandler sharedSellerHistoryHandler] getResultsArryFromCartReceivedResponce:response]);
        }
        
        
        else{
            service(error,nil);
        }
    }];
    
}
//Demo
- (id)loadLocalJson{
    NSString *pathStringToLocalFile = [NSString stringWithContentsOfFile:[[NSBundle mainBundle] pathForResource: @"File" ofType: @"txt"] usedEncoding:nil error:nil];
    
    NSError *deserializingError;
    
    id object = [NSJSONSerialization JSONObjectWithData:[pathStringToLocalFile dataUsingEncoding:NSUTF8StringEncoding]
                                                options:NSJSONReadingMutableContainers
                                                  error:&deserializingError];
    return object;
}
//Need to move buyerHistoryHandler
- (void)getAllItemListing:(NSString *)request serviceCallBack:(SellerHistoryResultBlock)service{
    conType=MIMETypeJSON
    requestType=RequestTypeGet
    NSString *urlString=@"https://p2pdinner-services.herokuapp.com/api/v1/listing/view/current";
    [self execute:urlString requestObject:request contentType:conType requestMethod:requestType serviceCallBack:^(NSError *error, id response) {
        [[UIApplication sharedApplication]setNetworkActivityIndicatorVisible:NO];
        if (!error) {
            service(nil,[[MyOrderItemHandler sharedSellerHistoryHandler] getResultsArryForAllCurrentListRessponce:response forSeachResultType:kDinnerHistoryResult]);
        }
        
        
        else{
            service(error,nil);
        }
    }];
}
//-(void)getMyOrderReceived:(NSString *)request serviceCallBack:(SellerHistoryResultBlock)service
@end
