//
//  ViewController.m
//  P2PDinner
//
//  Created by selvam on 2/3/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "FindDinnerViewController.h"
#import "AppConstants.h"
#import "Utility.h"
#import "Servicehandler.h"
#import "TimeRequest.h"
#import "TimeResponce.h"
#import "AddressRequest.h"
#import "BuyerHandler.h"
#import "ActivityView.h"
#import "AddressResponce.h"
#import "DinnerResultViewController.h"
#import "MyOrderItemHandler.h"
#import "AppDelegate.h"
@import CoreLocation;

@interface FindDinnerViewController () <CLLocationManagerDelegate>{
    ActivityView *activityView;
}
@property (strong, nonatomic) CLLocationManager *locationManager;
-(void)updateAddressFieldUsingService:(NSString *)RequestURL;
@end
@implementation FindDinnerViewController

- (void)viewDidLoad {
    activityView=[[ActivityView alloc]initWithFrame:self.view.frame];
    [self.navigationItem.backBarButtonItem setTitle:@"Back"];
    [super viewDidLoad];
    dinnerDate=[NSDate date];
    guestValue=2;
    AppDelegate *appdelegate=(AppDelegate *)[[UIApplication sharedApplication] delegate];
    LocationManger *locationMgr=[LocationManger sharedLocationManager];
    locationMgr.delegate=self;
    
    if (!appdelegate.lastAddress) {
        [locationMgr updateLocation];
    }
    
    findDinnerButton.layer.cornerRadius = 5;
    // This will remove extra separators from tableview
    dinnerUISetup.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
}
- (void)currentUserLocation:(CLLocation *)Location{
    AppDelegate *appdelegate=(AppDelegate *)[[UIApplication sharedApplication] delegate];
    appdelegate.lastLocation=Location;
    [[LocationManger sharedLocationManager]stopUpdatingLocation];
    CLGeocoder * geoCoder = [[CLGeocoder alloc] init];
    [geoCoder reverseGeocodeLocation:Location completionHandler:^(NSArray *placemarks, NSError *error) {
        for (CLPlacemark * placemark in placemarks)
        {
            appdelegate.localLocation=[NSString stringWithFormat:@"en_%@",[placemark ISOcountryCode]];
          NSArray *addressArray= [NSArray arrayWithObjects:[placemark thoroughfare],[placemark locality],[placemark administrativeArea], nil];
            addressArray=[Utility removeNilArrayOfString:addressArray];
            NSString *addresStr=[addressArray componentsJoinedByString:@","];
            if ([placemark subThoroughfare]) {
                addresStr=[NSString stringWithFormat:@"%@ %@",[placemark subThoroughfare],addresStr];
            }
            selectedAddressField.text=addresStr;
            
        }
    }];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma UISetupDinnerView
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return 3;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    UITableViewCell *cell ;
    if(indexPath.row==firstCell){
        cell= [tableView dequeueReusableCellWithIdentifier:PlaceCustomCellIdentifier];
        selectedAddressField=(UITextField *)[cell viewWithTag:111];
        AppDelegate *appdelegate=(AppDelegate *)[[UIApplication sharedApplication] delegate];
        if (appdelegate.lastAddress) {
            selectedAddressField.text=appdelegate.lastAddress;
        }
        [selectedAddressField.layer setBorderColor:[[[UIColor colorWithRed:0.97 green:0.62 blue:0.20 alpha:1.0] colorWithAlphaComponent:0.5] CGColor]];
        [selectedAddressField.layer setBorderWidth:0.5];
        
        //The rounded corner part, where you specify your view's corner radius:
        selectedAddressField.layer.cornerRadius = 5;
    }
    else if(indexPath.row==secondCell)
    {
        cell= [tableView dequeueReusableCellWithIdentifier:GuestCustomCellIdentifier];
        guestTableViewCell =(GuestTableviewCell *) cell;
        [guestTableViewCell setGuestCount:guestValue];
    }
    else if(indexPath.row==thirdCell)
    {
        cell= [tableView dequeueReusableCellWithIdentifier:DatePicCoustomCellIdentifier];
        selectedDate = (UILabel *) [cell viewWithTag:999];
//        cell.layer.shadowOffset = CGSizeMake(1, 0);
//        cell.layer.shadowColor = [[UIColor blackColor] CGColor];
//        cell.layer.shadowRadius = 5;
//        cell.layer.shadowOpacity = .25;
    }
    else if(indexPath.row==fourthCell)
    {
        cell= [tableView dequeueReusableCellWithIdentifier:FindDinnerCoustomCellIdentifier];
        UIButton *button = (UIButton *) [cell viewWithTag:555];
        button.layer.cornerRadius = 5;
    }
    return cell;
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    
    NSString *str = [textField.text stringByReplacingOccurrencesOfString:@" "
                                                              withString:@"+"];
    NSRegularExpression *re = [NSRegularExpression regularExpressionWithPattern:@"[@#$.,!\\d]" options:0 error:nil];
    
    str = [re stringByReplacingMatchesInString:str
                                       options:0
                                         range:NSMakeRange(0, [str length])
                                  withTemplate:@""];
    
    NSString *requestURL=[NSString stringWithFormat:@"https://maps.googleapis.com/maps/api/geocode/json?address=%@",str];
    [self updateAddressFieldUsingService:requestURL];
    
    [textField resignFirstResponder];
    return YES;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row==2) {
        return 108;
    }
    return 138;
}
-(NSArray *)shortByDistance:(NSArray *)dinnerResultArray{
    NSSortDescriptor *distance = [[NSSortDescriptor alloc] initWithKey:@"distance" ascending:YES];
    return [dinnerResultArray  sortedArrayUsingDescriptors:[NSArray arrayWithObject:distance]];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:DatePickerViewControllerSegue]) {
        DatePicker *datePickerViewController =(DatePicker *) segue.destinationViewController;
#warning Known issue Need to send old Date
        datePickerViewController.delegate=self;
    }
    if ([segue.identifier isEqualToString:@"DinnerResult"]) {
        DinnerResultViewController *dinnerResult=(DinnerResultViewController *) segue.destinationViewController;
        NSArray *dinnerListArray=[[MyOrderItemHandler sharedSellerHistoryHandler]getResultsArryForAllCurrentListRessponce:[sender objectForKey:@"results"] forSeachResultType:kOrderSearchResult];
        [dinnerResult setNoDinnerList:(dinnerListArray.count>0)?NO:YES];
        [dinnerResult setDinnerListArray:[self shortByDistance:dinnerListArray]];
    }
    
}
#pragma DatePicker Delegate
- (void)selectedDate:(NSDate *)selectedDateValue{
    dinnerDate=selectedDateValue;
    
    
    selectedDate.text=[Utility dateToStringFormat:selectedDateValue timeZone:LOCAL];
}
- (IBAction)findDinner:(id)sender{
    AppDelegate *appdelegate=(AppDelegate *)[[UIApplication sharedApplication] delegate];
    
    guestValue=guestTableViewCell.guestCount;
    [activityView startAnimating:@"Loading..."];
    [self.view addSubview:activityView];
    if ([Utility validateToday:dinnerDate]) {
        dinnerDate=[NSDate date];
    }else{
        dinnerDate=[Utility beginingOfDay:dinnerDate];
    }
    NSString *after_close_time=[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:dinnerDate timeZone:UTC];
    NSString *before_close_time=[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:[Utility endOfDay:dinnerDate] timeZone:UTC];
    NSMutableString *requestFormat=[NSMutableString stringWithFormat:@"%@&q=after_close_time::%@|before_close_time::%@|guests::%d",selectedAddressField.text,after_close_time,before_close_time,guestValue];
    if (appdelegate.localLocation.length>0) {
        NSString *locationPara=[NSString stringWithFormat:@"&locale=%@",appdelegate.localLocation];
        [requestFormat appendString:locationPara];
        }
//     NSLog(@"\n\n\nStart Date==> %@ \n EndDate ==>%@ \n CloseDate ==> %@\n\n\n",[startDate descriptionWithLocale:[NSLocale currentLocale]],[endDate descriptionWithLocale:[NSLocale currentLocale]
    
    [[BuyerHandler sharedBuyerHandler] getSearchResultBasedOnLoactionAndFilterRequest:requestFormat resultHandler:^(NSError *error, NSArray *response) {
        [activityView stopAnimating];
        [activityView removeFromSuperview];
        //Need to handle service error issue
        if (!error) {
            
            [self performSegueWithIdentifier:@"DinnerResult" sender:response];
        }
        
    }];
    
}

- (void)updateAddressFieldUsingService:(NSString *)requestURL
{
    [[LocationServiceHandler sharedLocationHandler] getLocationAdderess:requestURL serviceCallBack:^(NSError *error, NSString *response) {
        selectedAddressField.text=response;
    }];
    
    
}

//
@end
