//
//  ItemDetailViewController.m
//  P2PDinner
//
//  Created by Selvam M on 10/29/15.
//  Copyright © 2015 P2PDinner. All rights reserved.
//

#import "ItemDetailViewController.h"
#import "AFImageRequestOperation.h"
#import "Utility.h"
#import "FacebookManager.h"
#import "CartRequest.h"
#import "BuyerHandler.h"
#import "DinnerLoginViewController.h"
#import "SharedLogin.h"
#import "AppDelegate.h"
#import "ActivityView.h"


@interface ItemDetailViewController (){
    ActivityView *activityView;
}
@property(nonatomic,assign)int minValue;
@property(nonatomic,assign)int selectedValue;
@property(nonatomic,assign)int maxValue;
@end

@implementation ItemDetailViewController
@synthesize itemDetails;
-(void)getProfileName{
    [[FacebookManager sharedFacebookManager] getProfileNameFromId:itemDetails.sellerName];
}
-(void)calculatePrice{
    AppDelegate *appdelegate=(AppDelegate *)[[UIApplication sharedApplication] delegate];
    NSString *locationString=appdelegate.localLocation;
    if (!locationString||!(locationString.length>0)) {
        locationString=[[NSLocale preferredLanguages] objectAtIndex:0];
    }
    NSLocale* localPrice = [[NSLocale alloc] initWithLocaleIdentifier:locationString];
    NSNumberFormatter *fmtr = [[NSNumberFormatter alloc] init];
    [fmtr setNumberStyle:NSNumberFormatterCurrencyStyle];
    [fmtr setLocale:localPrice];
    [_totalPrice setText:[fmtr stringFromNumber: [NSNumber numberWithFloat:(_selectedValue*[itemDetails.costPerItem floatValue])]]];
    
    //_totalPrice.text=[NSString stringWithFormat:@"%.2f",(_selectedValue*[itemDetails.costPerItem floatValue])];

}
-(IBAction)segmentAction:(id)sender{
    
    UISegmentedControl *segment=(UISegmentedControl *)sender;
    if ([self validateIncrement]) {
        if (segment.selectedSegmentIndex==0) {
            _selectedValue--;
            _selectedValue=(_selectedValue==0)?1:_selectedValue;
        }else{
            _selectedValue++;
            _selectedValue=(_selectedValue==_maxValue+1)?_maxValue:_selectedValue;
        }
        
    }
    [self calculatePrice];
    if(_selectedValue==1){
        _dinnerSelectionCount.text=[NSString stringWithFormat:@"Dinner %d",_selectedValue];
    }else
    _dinnerSelectionCount.text=[NSString stringWithFormat:@"Dinners %d",_selectedValue];
    [segment setSelectedSegmentIndex:UISegmentedControlNoSegment];
}
- (NSArray *)makeImageURLfromimageName:(NSString *)imageNames
{
    if (((NSString *)[NSNull null] !=imageNames)) {
        NSArray *mutableImageURL=[imageNames componentsSeparatedByString:@","];
        if ([mutableImageURL count]>0) {
        NSMutableArray *imageURLArray=[[NSMutableArray alloc]init];
        [mutableImageURL enumerateObjectsUsingBlock:^(NSString *_Nonnull imageName, NSUInteger idx, BOOL * _Nonnull stop) {
            imageName=[imageName stringByReplacingOccurrencesOfString:@"\"" withString:@""];
            [imageURLArray addObject:imageName];
        }];
            return imageURLArray;
        }
       
    }
    return nil;
}
- (void)downloadImageWithURL:(NSString *)url WithImagView:(UIImageView *)imageView;
{
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:url]];
    [request setAccessibilityLabel:@"selvam"];
    AFImageRequestOperation *operation = [AFImageRequestOperation imageRequestOperationWithRequest:request imageProcessingBlock:^UIImage *(UIImage *image) {
        return image;
    } success:^(NSURLRequest *request, NSHTTPURLResponse *response, UIImage *image) {
        imageView.image=image;
    } failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error) {
        imageView.image=[UIImage imageNamed:@"ImageDownloadFaild"];
    }];
    [operation start];
}
- (void)imageRequestOperation:(NSString *)photoUrl witImagView:(UIImageView *)imageView
{
    
    // download the photo
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:photoUrl]];
    [request setAccessibilityLabel:@"selvam"];
    AFImageRequestOperation *operation = [AFImageRequestOperation imageRequestOperationWithRequest:request imageProcessingBlock:^UIImage *(UIImage *image) {
        return image;
    } success:^(NSURLRequest *request, NSHTTPURLResponse *response, UIImage *image) {
        imageView.image=image;
        _foodImageBg.image=image;
        
    } failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error) {
        
    }];
    [operation start];
}
- (BOOL)validateIncrement{
    if (_minValue<=_selectedValue && _maxValue+1>_selectedValue) {
        return true;
    }
    return false;
}
- (void)setValueItemDetails{
    [self.foodName setText:itemDetails.title];
    AppDelegate *appdelegate=(AppDelegate *)[[UIApplication sharedApplication] delegate];
    NSString *locationString=appdelegate.localLocation;
    if (!locationString||!(locationString.length>0)) {
        locationString=[[NSLocale preferredLanguages] objectAtIndex:0];
    }
    NSLocale* localPrice = [[NSLocale alloc] initWithLocaleIdentifier:locationString];
    NSNumberFormatter *fmtr = [[NSNumberFormatter alloc] init];
    [fmtr setNumberStyle:NSNumberFormatterCurrencyStyle];
    [fmtr setLocale:localPrice];
    [self.foodPrice setText:[fmtr stringFromNumber: [NSNumber numberWithFloat:[itemDetails.costPerItem floatValue]]]];
    
  //  [self.foodPrice setText:[NSString stringWithFormat:@"%.2f",[itemDetails.costPerItem floatValue]]];
    if ([itemDetails.sellerName isEqual:[NSNull null]]) {
     [self.foodSellerName setText:@""];
    }else
    [self.foodSellerName setText:itemDetails.sellerName];
    [self.distance setText:itemDetails.distance];
    [self.foodAddress setText:itemDetails.addressLine1];//need to check
    [self.dinnerSelectionCount setText:@"Dinner 1"];//need to check
    NSArray *arrayDeliveryOption=[itemDetails.dinnerDelivery componentsSeparatedByString:@","];
    _minValue =1;
    _selectedValue=_minValue;
    _maxValue=[itemDetails.availableQuantity intValue];
    if (arrayDeliveryOption.count>1) {
        [self.toGoOption setText:[NSString stringWithFormat:@" %@ ",[arrayDeliveryOption objectAtIndex:0]]];
        [self.eatInOption setText:[NSString stringWithFormat:@" %@ ",[arrayDeliveryOption objectAtIndex:1]]];
    }
    else{
        NSString *value=[arrayDeliveryOption objectAtIndex:0];
        if (value.length>0) {
             [self.toGoOption setText:[NSString stringWithFormat:@" %@ ",value]];
        }else{
            [self.toGoOption setHidden:YES];
        }
       
        [self.eatInOption setHidden:YES];
    }
    NSDate *startDate=[Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:itemDetails.startDate timeZone:UTC];
    NSDate *endDate=[Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:itemDetails.endDate timeZone:UTC];
    [self.foodAvailableTime setText:[NSString stringWithFormat:@"Serving between %@ - %@",[Utility dateToStringFormat:@"h:mm a" dateString:startDate timeZone:LOCAL],[Utility dateToStringFormat:@"h:mm a" dateString:endDate timeZone:LOCAL]]];
    [self calculatePrice];
    
}
- (void)viewDidLoad {
    [super viewDidLoad];
    self.title=@"I Want Dinner";
    [self setValueItemDetails];
    [self getProfileName];
    PagedImageScrollView *pageImageScrollView= [[PagedImageScrollView alloc] initWithFrame:CGRectMake(_foodImageViewCell.frame.origin.x, _foodImageViewCell.frame.origin.x, self.view.frame.size.width, _foodImageViewCell.frame.size.height)];
    [pageImageScrollView setScrollViewContents:[self makeImageURLfromimageName:itemDetails.imageUri]];
    //easily setting pagecontrol pos, see PageControlPosition defination in PagedImageScrollView.h
    pageImageScrollView.pageControlPos = PageControlPositionRightCorner;
    [_foodImageViewCell addSubview:pageImageScrollView];
    //[self imageRequestOperation:[self makeImageURLfromimageName:itemDetails.imageUri] witImagView:self.foodImage];
    
    activityView=[[ActivityView alloc]initWithFrame:self.view.frame];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)addCartService:(NSString *)requestStr{
    [[BuyerHandler sharedBuyerHandler] addCart:requestStr withResponse:^(NSError *error, NSString *cartId) {
        NSNumber *currentUseId=(NSNumber *)[[NSUserDefaults standardUserDefaults] objectForKey:@"userId"];
        //NSString *currentUseId=[NSString stringWithFormat:@"%@",[[NSUserDefaults standardUserDefaults] objectForKey:@"userId"]];
        if (!error) {
            [[BuyerHandler sharedBuyerHandler] placeOrder:cartId withUserId:[currentUseId stringValue] withResponse:^(NSError *error, NSString *message) {
                
                if (activityView.isAnimating) {
                    [activityView stopAnimating];
                    [activityView removeFromSuperview];
                }
                NSString *titleStr=(!error)?@"Success":@"Fail";
                
                UIAlertController *alertController = [UIAlertController alertControllerWithTitle:titleStr message:message preferredStyle:UIAlertControllerStyleAlert];
                UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
                    [self.navigationController popToRootViewControllerAnimated:YES];
                     }];
                            [alertController addAction:ok];
                            [self presentViewController:alertController animated:YES completion:nil];

            }];
        }else{
            if (activityView.isAnimating) {
                [activityView stopAnimating];
                [activityView removeFromSuperview];
            }
        }
    }];
    
}

-(void)buyAction{
    CartRequest *cartRequest=[[CartRequest alloc]init];
    [cartRequest setProfile_id:[[SharedLogin sharedLogin] userId]];
    [cartRequest setListing_id:itemDetails.listingId];
    [cartRequest setQuantity:[NSNumber numberWithInt:_selectedValue]];
    [cartRequest setDelivery_type:itemDetails.dinnerDelivery];
    NSDictionary *objectDic=[Utility dictionaryWithPropertiesOfObject:cartRequest];
    NSString *cartRequestStr=[Utility jsonToDictionary:objectDic];
    [self addCartService:cartRequestStr];
}
-(void)removeLoginViewContrller{
    NSMutableArray* tempVCA = [NSMutableArray arrayWithArray:[self.navigationController viewControllers]];
    [tempVCA enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if([obj isKindOfClass:[DinnerLoginViewController class]])
        {
            [tempVCA removeObject:obj];
            self.navigationController.viewControllers=tempVCA;
            *stop=YES;
        }
    }];
}

-(IBAction)buyButtonAction:(id)sender{
    [activityView startAnimating:@"Loading..."];
    [self.view addSubview:activityView];
    
    NSNumber *number=(NSNumber *)[[NSUserDefaults standardUserDefaults] objectForKey:@"userId"];
    if ([number integerValue]>0) {
        [[SharedLogin sharedLogin] setUserId:number];
        [self buyAction];
    }else
    {
        UIStoryboard *mainStoryboard = [UIStoryboard storyboardWithName:@"Main"  bundle: nil];
        DinnerLoginViewController *dinnerLogin = (DinnerLoginViewController*)[mainStoryboard instantiateViewControllerWithIdentifier: @"DinnerLoginViewController"];
        [dinnerLogin getLoginResponse:^(NSError *error, NSString *emailId, BOOL successFull) {
            if (successFull) {
                  NSNumber *userId=(NSNumber *)[[NSUserDefaults standardUserDefaults] objectForKey:@"userId"];
                [[SharedLogin sharedLogin] setUserId:userId];
                [self buyAction];
                [self removeLoginViewContrller];
            }
        }];
        [self.navigationController pushViewController:dinnerLogin animated:YES];
    }
    
   
    
    
}

@end
