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
@interface MyOrderViewController()
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
    startDate=[Utility dateToStringFormat:@"MM/dd/YYYY HH:mm:ss" dateString:[self getTodayTwellMorning:[NSDate date]] timeZone:UTC];
    endDate=[Utility dateToStringFormat:@"MM/dd/YYYY HH:mm:ss" dateString:[self getEndingOfDate:[NSDate date]] timeZone:UTC];
    
    [self selectedDateOption:[self getTodayTwellMorning:[NSDate date]]];
}
- (void)viewDidLoad{
    
    self.title=@"My Orders";
    tableView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    [dateToolBar intDateToolBar];
    [dateToolBar setButtonDelegate:self];
    _tableViewArray=[NSArray arrayWithObject:@"Loading"];
    [tableView reloadData];
    [self performSelector:@selector(callTodayMyOrderView) withObject:nil afterDelay:1.05];
    
    [super viewDidLoad];
    
    
}
#pragma MyOrderTableViewDelegate
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    id myOrderObject=[_tableViewArray objectAtIndex:indexPath.row];
    if ([myOrderObject isKindOfClass:[MyOrderItem class]]) {
        return 93;
    }
    else if([myOrderObject isKindOfClass:[CarRecivedItemDetail class]]) {
        return 126;
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
        NSArray *mutableImageURL=[imageNames componentsSeparatedByString:@","];
        if ([mutableImageURL count]>0) {
            imageName=[mutableImageURL objectAtIndex:0];
            imageName=[imageName stringByReplacingOccurrencesOfString:@"\"" withString:@""];
        }
        return imageName;
    }
    return @"";
    
}
-(NSMutableAttributedString *)createTextWith:(NSString *)passcode withPlateCount:(NSNumber *)numberWithString{
    NSMutableAttributedString *passcodeString=[[NSMutableAttributedString alloc]initWithString:[NSString stringWithFormat:@" %@ ",passcode]];
    [passcodeString addAttribute:NSBackgroundColorAttributeName value:[UIColor orangeColor] range:NSMakeRange(0, passcode.length+2)];
    [passcodeString addAttribute:NSForegroundColorAttributeName value:[UIColor whiteColor] range:NSMakeRange(0, passcode.length+2)];
    NSMutableAttributedString *returnString=[[NSMutableAttributedString alloc]initWithString:@"Conf# "];
    [returnString appendAttributedString:passcodeString];
    [returnString appendAttributedString:[[NSAttributedString alloc]initWithString:[NSString stringWithFormat:@" for %ld Plates",[numberWithString integerValue]]]];
    
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
    id myOrderObject=[_tableViewArray objectAtIndex:indexPath.row];
    if ([myOrderObject isKindOfClass:[MyOrderItem class]]) {
        item=[_tableViewArray objectAtIndex:indexPath.row];
    }
    else if([myOrderObject isKindOfClass:[CarRecivedItemDetail class]]) {
        cartview=[_tableViewArray objectAtIndex:indexPath.row];
    }
    if (!cell) {
        if ([myOrderObject isKindOfClass:[MyOrderItem class]]|[myOrderObject isKindOfClass:[NSDictionary class]]){
            cell= [myOrdertableView dequeueReusableCellWithIdentifier:@"MyOrdersCell"];
            countLable=(UILabel *)[cell viewWithTag:111];
        }
        else if([myOrderObject isKindOfClass:[CarRecivedItemDetail class]]){
            cell= [myOrdertableView dequeueReusableCellWithIdentifier:@"MyOrderDetailCell"];
            dinnerImage=(UIImageView *)[cell viewWithTag:100];
            title=(UILabel *)[cell viewWithTag:101];
            price=(UILabel *)[cell viewWithTag:102];
            sellerName=(UILabel *)[cell viewWithTag:103];
            passcode=(UILabel *)[cell viewWithTag:104];
            address=(UILabel *)[cell viewWithTag:105];
            //countLable=(UILabel *)[cell viewWithTag:106];
            deliveryOption=(UILabel *)[cell viewWithTag:107];
            
            
        }
        else{
            cell= [myOrdertableView dequeueReusableCellWithIdentifier:@"MyOrdersCellLoading"];
            UIActivityIndicatorView *activityView=(UIActivityIndicatorView *)[cell viewWithTag:555];
            [activityView startAnimating];
        }
    }
    
    if ([myOrderObject isKindOfClass:[MyOrderItem class]]){
        cell.textLabel.text=[item title];
        countLable.text=[item.listing.quantityOrdered stringValue];
        countLable.layer.cornerRadius = 5.0f;
        [countLable setClipsToBounds:YES];
        NSString *startServingTime=[Utility dateToStringFormat:@"hh:mm a" dateString:[Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:item.listing.startTime  timeZone:UTC]  timeZone:LOCAL];
        NSString *stopServingTime=[Utility dateToStringFormat:@"hh:mm a" dateString:[Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:item.listing.endDate  timeZone:UTC]  timeZone:LOCAL];
        cell.detailTextLabel.text=[NSString stringWithFormat:@"Serving %@ to %@",startServingTime,stopServingTime];
    }
    else if ([myOrderObject isKindOfClass:[CarRecivedItemDetail class]]){
//        dinnerImage=(UIImageView *)[cell viewWithTag:100];
//        title=(UILabel *)[cell viewWithTag:101];
//        price=(UILabel *)[cell viewWithTag:102];
//        sellerName=(UILabel *)[cell viewWithTag:103];
//        passcode=(UILabel *)[cell viewWithTag:104];
//        address=(UILabel *)[cell viewWithTag:105];
//        countLable=(UILabel *)[cell viewWithTag:106];
//        deliveryOption=(UILabel *)[cell viewWithTag:107];

        if ((NSString *)[NSNull null] != cartview.imageUri) {
            [Utility imageRequestOperation:[self makeImageURLfromimageName:cartview.imageUri] witImagView:dinnerImage];
        }
        else{
            [dinnerImage setImage:[UIImage imageNamed:@"noImage"]];
        }
        dinnerImage.layer.cornerRadius = 48.0f;
        dinnerImage.layer.masksToBounds = YES;
        //dinnerImage.layer.borderWidth = .5f;
        dinnerImage.layer.shadowColor = [UIColor orangeColor].CGColor;
        dinnerImage.layer.shadowOpacity = 0.4;
        dinnerImage.layer.shadowRadius = 48.0f;
        
        NSString *startServingTime=[Utility dateToStringFormat:@"hh:mm a" dateString:[Utility epochToDate:cartview.startTime]  timeZone:LOCAL];
        NSString *stopServingTime=[Utility dateToStringFormat:@"hh:mm a" dateString:[Utility epochToDate:cartview.endTime]  timeZone:LOCAL];
        title.text=cartview.title;
        price.text=[NSString stringWithFormat:@"$ %ld",(long)[cartview.totalPrice integerValue]];
        sellerName.text=cartview.buyerName;
        //passcode.text=[NSString stringWithFormat:@"Conf# %@ for %ld Plats",cartview.passCode,(long)[cartview.orderQuantity integerValue]];
        passcode.attributedText=[self createTextWith:cartview.passCode withPlateCount:cartview.orderQuantity];
        NSArray *addressArray=[NSArray arrayWithObjects:cartview.address_line1,cartview.address_line2,cartview.city,cartview.state,nil];
        address.text=[addressArray componentsJoinedByString: @","];
        //passcode.text=cartview.passCode;
        countLable.text=[NSString stringWithFormat:@"%ld Plats",(long)[cartview.orderQuantity integerValue]];
        deliveryOption.text=[NSString stringWithFormat:@"Served between %@ and %@",startServingTime,stopServingTime];
        
        
    }
    else if([myOrderObject isKindOfClass:[NSDictionary class]]){
        cell.textLabel.text=[myOrderObject objectForKey:@"message"];
        cell.detailTextLabel.text=@"     ";
        countLable.text= @"";
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
    startDate=[Utility dateToStringFormat:@"MM/dd/YYYY HH:mm:ss" dateString:dateValue timeZone:UTC];
    endDate=[Utility dateToStringFormat:@"MM/dd/YYYY HH:mm:ss" dateString:[dateValue dateByAddingTimeInterval:24*60*60] timeZone:UTC];
    selectedDate=[NSString stringWithFormat:@"%.0f",[dateValue timeIntervalSince1970]*1000];
    [self myOrderDinnerOptions:segmentedControl];

}
-(IBAction)myOrderDinnerOptions:(id)sender{
    UISegmentedControl *segmentControll=(UISegmentedControl *)sender;
    _tableViewArray=[NSArray arrayWithObject:@"Loading"];
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
@end
