//
//  MyOrderViewController.m
//  P2PDinner
//
//  Created by Selvam M on 7/22/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "MyOrderViewController.h"
#import "SellerHistoryHandler.h"
#import "MyOrderItem.h"
#import "Utility.h"
#import "MyOrderDetail.h"
#import "SharedLogin.h"
#import "AppDelegate.h"
#import "BuyerHandler.h"
#import "StarRatingView.h"
#import "MyOrderDetailCell.h"
#import "StringConstants.h"

@interface MyOrderViewController()<RatingDelegate>
{
    NSString *selectedDate;
    NSString *startDate;
    NSString *endDate;
}
@end
@implementation MyOrderViewController
- (NSDate *)getTodayTwellMorning:(NSDate *)inputDate{
    NSCalendar *calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    [calendar setLocale:[NSLocale currentLocale]];
    [calendar setTimeZone:[NSTimeZone localTimeZone]];
    
    NSDateComponents *nowComponents = [calendar components:NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay fromDate:inputDate];
    inputDate = [calendar dateFromComponents:nowComponents];
    return inputDate;
}

- (NSDate *)getEndingOfDate:(NSDate *)inputDate{
    NSDateComponents *components = [[NSDateComponents alloc] init];
    components.day = [[NSCalendar currentCalendar] ordinalityOfUnit:(NSCalendarUnitDay) inUnit:(NSCalendarUnitEra) forDate:inputDate];
    components.day += 1;
    inputDate = [[NSCalendar currentCalendar] dateFromComponents:components];
    return inputDate;
}
- (void)callTodayMyOrderView{
    
   // NSString *timeValue=[NSString stringWithFormat:@"%.0f",([[self getTodayTwellMorning:[NSDate date]] timeIntervalSince1970]*1000)];
    startDate=[Utility dateToStringFormat:kDateAndTimeFormat dateString:[self getTodayTwellMorning:[NSDate date]] timeZone:UTC];
    endDate=[Utility dateToStringFormat:kDateAndTimeFormat dateString:[self getEndingOfDate:[NSDate date]] timeZone:UTC];
    
    [self selectedDateOption:[self getTodayTwellMorning:[NSDate date]]];
}
- (void)viewDidLoad{
    
    self.title=kMyOrders;
    tableView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    [dateToolBar intDateToolBar];
    [dateToolBar setButtonDelegate:self];
    _tableViewArray=[NSArray arrayWithObject:kLoading];
    [tableView reloadData];
    [self performSelector:@selector(callTodayMyOrderView) withObject:nil afterDelay:1.05];
    
    [super viewDidLoad];
    
    
}
#pragma MyOrderTableViewDelegate
- (CGFloat)tableView:(UITableView *)tableView estimatedHeightForRowAtIndexPath:(nonnull NSIndexPath *)indexPath{
    return UITableViewAutomaticDimension;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    id myOrderObject=[_tableViewArray objectAtIndex:indexPath.row];
    if ([myOrderObject isKindOfClass:[MyOrderItem class]]) {
        return 93;
    }
    else if([myOrderObject isKindOfClass:[CarRecivedItemDetail class]]) {
        return UITableViewAutomaticDimension;
    }
    
    return 85;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return [_tableViewArray count];
}
- (NSString *)makeImageURLfromimageName:(NSString *)imageNames
{
    if ((NSString *)[NSNull null] !=imageNames) {
        NSString *imageName;
        NSArray *mutableImageURL=[imageNames componentsSeparatedByString:kComa_String];
        if ([mutableImageURL count]>0) {
            imageName=[mutableImageURL objectAtIndex:0];
            imageName=[imageName stringByReplacingOccurrencesOfString:kSlash_String withString:kEmpty_String];
        }
        return imageName;
    }
    return kEmpty_String;
    
}
-(NSMutableAttributedString *)createTextWith:(NSString *)passcode withPlateCount:(NSNumber *)numberWithString{
    NSMutableAttributedString *passcodeString=[[NSMutableAttributedString alloc]initWithString:[NSString stringWithFormat:kSrtingPattenWithSpace,passcode]];
    [passcodeString addAttribute:NSBackgroundColorAttributeName value:[UIColor orangeColor] range:NSMakeRange(0, passcode.length+2)];
    [passcodeString addAttribute:NSForegroundColorAttributeName value:[UIColor whiteColor] range:NSMakeRange(0, passcode.length+2)];
    NSMutableAttributedString *returnString=[[NSMutableAttributedString alloc]initWithString:kConf];
    [returnString appendAttributedString:passcodeString];
    [returnString appendAttributedString:[[NSAttributedString alloc]initWithString:[NSString stringWithFormat:kForPlates,[numberWithString integerValue]]]];
    
    return returnString;
}
- (UITableViewCell *)tableView:(UITableView *)myOrdertableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    //MyOrderDetailCell
    UITableViewCell *cell;
    MyOrderItem *item;
    CarRecivedItemDetail *cartview;
    UILabel *countLable;
    UILabel *title;
    UILabel *address;
    UILabel *passcode;
    UILabel *price;
    UILabel *sellerName;
    UIImageView *dinnerImage;
    UILabel *deliveryOption;
    StarRatingView *sareView;
    id myOrderObject=[_tableViewArray objectAtIndex:indexPath.row];
    if ([myOrderObject isKindOfClass:[MyOrderItem class]]) {
        item=[_tableViewArray objectAtIndex:indexPath.row];
    }
    else if([myOrderObject isKindOfClass:[CarRecivedItemDetail class]]) {
        cartview=[_tableViewArray objectAtIndex:indexPath.row];
    }
    if (!cell) {
        if ([myOrderObject isKindOfClass:[MyOrderItem class]]|[myOrderObject isKindOfClass:[NSDictionary class]]){
            cell= [myOrdertableView dequeueReusableCellWithIdentifier:kMyOrdersCell];
            countLable=(UILabel *)[cell viewWithTag:111];
        }
        else if([myOrderObject isKindOfClass:[CarRecivedItemDetail class]]){
            MyOrderDetailCell *myOrderDetailcell= [myOrdertableView dequeueReusableCellWithIdentifier:kMyOrderDetailCell];
            [myOrderDetailcell setCartDetail:myOrderObject];
            dinnerImage=(UIImageView *)[myOrderDetailcell viewWithTag:100];
            title=(UILabel *)[myOrderDetailcell viewWithTag:101];
            price=(UILabel *)[myOrderDetailcell viewWithTag:102];
            sellerName=(UILabel *)[myOrderDetailcell viewWithTag:103];
            passcode=(UILabel *)[myOrderDetailcell viewWithTag:104];
            address=(UILabel *)[myOrderDetailcell viewWithTag:105];
            deliveryOption=(UILabel *)[myOrderDetailcell viewWithTag:107];
            sareView=(StarRatingView *)[myOrderDetailcell viewWithTag:108];
            sareView.delegate=self;
            cell=myOrderDetailcell;
        }
        else{
            cell= [myOrdertableView dequeueReusableCellWithIdentifier:kMyOrdersCellLoading];
            UIActivityIndicatorView *activityView=(UIActivityIndicatorView *)[cell viewWithTag:555];
            [activityView startAnimating];
        }
    }
    
    if ([myOrderObject isKindOfClass:[MyOrderItem class]]){
        cell.textLabel.text=[item title];
        countLable.text=[item.listing.quantityOrdered stringValue];
        countLable.layer.cornerRadius = 5.0f;
        [countLable setClipsToBounds:YES];
        NSString *startServingTime=[Utility dateToStringFormat:kTimeMinOnly12hrsFormat2Digit dateString:[Utility stringToDateFormat:kDateAndTimeFormat dateString:item.listing.startTime  timeZone:UTC]  timeZone:LOCAL];
        NSString *stopServingTime=[Utility dateToStringFormat:kTimeMinOnly12hrsFormat2Digit dateString:[Utility stringToDateFormat:kDateAndTimeFormat dateString:item.listing.endDate  timeZone:UTC]  timeZone:LOCAL];
        cell.detailTextLabel.text=[NSString stringWithFormat:kServingTo,startServingTime,stopServingTime];
    }
    else if ([myOrderObject isKindOfClass:[CarRecivedItemDetail class]]){
        if ((NSString *)[NSNull null] != cartview.imageUri) {
            [Utility imageRequestOperation:[self makeImageURLfromimageName:cartview.imageUri] witImagView:dinnerImage];
        }
        else{
            [dinnerImage setImage:[UIImage imageNamed:knoImage]];
        }
        dinnerImage.layer.cornerRadius = 48.0f;
        dinnerImage.layer.masksToBounds = YES;
        //dinnerImage.layer.borderWidth = .5f;
        dinnerImage.layer.shadowColor = [UIColor orangeColor].CGColor;
        dinnerImage.layer.shadowOpacity = 0.4;
        dinnerImage.layer.shadowRadius = 48.0f;
        [dinnerImage setContentMode:UIViewContentModeScaleAspectFill];
        
        NSString *startServingTime=[Utility dateToStringFormat:kTimeMinOnly12hrsFormat2Digit dateString:[Utility epochToDate:cartview.startTime]  timeZone:LOCAL];
        NSString *stopServingTime=[Utility dateToStringFormat:kTimeMinOnly12hrsFormat2Digit dateString:[Utility epochToDate:cartview.endTime]  timeZone:LOCAL];
        title.text=cartview.title;
        
        AppDelegate *appdelegate=(AppDelegate *)[[UIApplication sharedApplication] delegate];
        NSString *locationString=appdelegate.localLocation;
        if (!locationString||!(locationString.length>0)) {
            locationString=[[NSLocale preferredLanguages] objectAtIndex:0];
        }
        NSLocale* localPrice = [[NSLocale alloc] initWithLocaleIdentifier:locationString];
        NSNumberFormatter *fmtr = [[NSNumberFormatter alloc] init];
        [fmtr setNumberStyle:NSNumberFormatterCurrencyStyle];
        [fmtr setLocale:localPrice];
        [price setText:[fmtr stringFromNumber: cartview.totalPrice]];
        sellerName.text=cartview.buyerName;
        passcode.attributedText=[self createTextWith:cartview.passCode withPlateCount:cartview.orderQuantity];
        if (!cartview.address_line1) {
            cartview.address_line1=kEmpty_String;
        }
        NSArray *addressArray=[NSArray arrayWithObjects:cartview.address_line1,cartview.address_line2,cartview.city,cartview.state,nil];
        address.text=[addressArray componentsJoinedByString: kComa_String];
        countLable.text=[NSString stringWithFormat:@"%ld Plats",(long)[cartview.orderQuantity integerValue]];
        deliveryOption.text=[NSString stringWithFormat:kServingBetween,startServingTime,stopServingTime];
        [sareView setMaxrating:[cartview.buyer_rating intValue]*20];
        [sareView setCartId:[cartview.cart_id stringValue]];
        
    }
    else if([myOrderObject isKindOfClass:[NSDictionary class]]){
        cell.textLabel.text=[myOrderObject objectForKey:@"message"];
        cell.detailTextLabel.text=@"     ";
        countLable.text= kEmpty_String;
        [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
        
    }
    cell.selectionStyle =UITableViewCellSelectionStyleNone;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if ([[_tableViewArray objectAtIndex:indexPath.row] isKindOfClass:[MyOrderItem class]]) {
        MyOrderItem *item=[_tableViewArray objectAtIndex:indexPath.row];
        if([item.listing.quantityOrdered integerValue]>0)
            [self performSegueWithIdentifier:@"MyOrderDetail" sender:self];
    }
    
}
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    NSIndexPath *indexPath = [tableView indexPathForSelectedRow];
    MyOrderDetail *viewController=(MyOrderDetail *)[segue destinationViewController];
    //    MyOrderDetail *viewController=(MyOrderDetail *)[navController topViewController];
    MyOrderItem *item;
    id myOrderObject=[_tableViewArray objectAtIndex:indexPath.row];
    if ([myOrderObject isKindOfClass:[MyOrderItem class]]) {
        item=[_tableViewArray objectAtIndex:indexPath.row];
        [viewController setMyOrderArray:[NSArray arrayWithObject:item]];
        [viewController setSelectedMenuId:item.listing.itemListing];
        [viewController setSelectedMenu:item.title];
    }
}
- (void)selectedDateOption:(NSDate *)dateValue{
    startDate=[Utility dateToStringFormat:kDateAndTimeFormat dateString:dateValue timeZone:UTC];
    endDate=[Utility dateToStringFormat:kDateAndTimeFormat dateString:[dateValue dateByAddingTimeInterval:24*60*60] timeZone:UTC];
    NSDate *sourceDate = [NSDate dateWithTimeIntervalSinceNow:3600 * 24 * 60];
    NSTimeZone* destinationTimeZone = [NSTimeZone systemTimeZone];
    float timeZoneOffset = [destinationTimeZone secondsFromGMTForDate:sourceDate] / 3600.0;
    selectedDate=[NSString stringWithFormat:@"%.0f",[[dateValue dateByAddingTimeInterval:-(timeZoneOffset*60*60)] timeIntervalSince1970]*1000];
    [self myOrderDinnerOptions:segmentedControl];

}
-(IBAction)myOrderDinnerOptions:(id)sender{
    UISegmentedControl *segmentControll=(UISegmentedControl *)sender;
    _tableViewArray=[NSArray arrayWithObject:kLoading];
    [tableView reloadData];
    
    
    if (segmentControll.selectedSegmentIndex==0) {
        //need to call dinner received options
        NSString *requestStr=[NSString stringWithFormat:@"%@?startDate=%@&endDate=%@",[[SharedLogin sharedLogin] userId],startDate,endDate];
        [[SellerHistoryHandler sharedServiceHandler] getMyOldOrderReceived:requestStr serviceCallBack:^(NSError *error, NSArray *response) {
            
            if (!error) {
                
                if ([response count]>0) {
                    _tableViewArray=[NSArray arrayWithArray:response];
                    [tableView reloadData];

                }else{
                    NSMutableDictionary *errorMsg=[[NSMutableDictionary alloc]init];
                    [errorMsg setObject:@"p2pdinner.no_listings" forKey:@"code"];
                    [errorMsg setObject:@"No your order found in system." forKey:@"message"];
                    [errorMsg setObject:@"200" forKey:@"status"];
                    _tableViewArray=[NSArray arrayWithObject:errorMsg];
                    [tableView reloadData];
                }
                
                
            }
            
        }];
        
    }
    else{
        //Need to call dinner selled options
        
        [[SellerHistoryHandler sharedSellerHistoryHandler] getItemListing:[NSString stringWithFormat:@"%@?inputDate=%@",[[NSUserDefaults standardUserDefaults] objectForKey:@"userId"],selectedDate] serviceCallBack:^(NSError *error, NSArray *response) {
            if (!error) {
                _tableViewArray=[NSArray arrayWithArray:response];
                [tableView reloadData];
            }
            
        }];
    }
}
-(void)updatedRatingValue:(int)ratingValue withCartId:(NSString *)cartId{
    [self buyerRatingUpdate:[NSString stringWithFormat:@"%d",(ratingValue/20)] withCartId:cartId];
}
-(void)buyerRatingUpdate:(NSString *)sellerRating withCartId:(NSString *)cartId{
    NSString *requstObject=[NSString stringWithFormat:@"{\"buyer_rating\": %@}",sellerRating];
    [[BuyerHandler sharedBuyerHandler] addRating:requstObject withCartId:cartId withResponse:nil];
}
@end
