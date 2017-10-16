//
//  PlaceViewController.m
//  P2PDinner
//
//  Created by Selvam M on 4/23/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "PlaceViewController.h"
#import "ItemDetailsShared.h"
#import "Utility.h"
#import "AppDelegate.h"
#import "StringConstants.h"
@interface PlaceViewController()<LocationManagerDelegate,UITextViewDelegate>{
    LocationManger *locationMgr;
    NSString *sharedAddress;
}
@end
@implementation PlaceViewController
@synthesize itemDetails,textVeiw;
- (void)viewDidLoad{
    [super viewDidLoad];
    AppDelegate *appdelegate=(AppDelegate *)[[UIApplication sharedApplication] delegate];
    if(![self vaidateAddressExist:itemDetails])
    {
    if (appdelegate.lastAddress) {
        sharedAddress=appdelegate.lastAddress;
    }
    else{
    locationMgr=[LocationManger sharedLocationManager];
    if (!([[self getAddressFromItemDetail:itemDetails] length]>0)) {
        [locationMgr updateLocation];
    }
    
    locationMgr.delegate=self;
    }
    }else{
      sharedAddress=[self getHistoryAddress:itemDetails];
    }
}
-(BOOL)vaidateAddressExist:(ItemDetails *)itemDetail{
    NSMutableArray *array=[[NSMutableArray alloc]initWithObjects:[itemDetail addressLine1],[itemDetail addressLine2],[itemDetail city],[itemDetail state],[itemDetail zipCode], nil];
    [array removeObject:@""];
    NSString *addressString=[[array valueForKey:kDescription] componentsJoinedByString:@","];
    if (addressString.length>0) {
        return TRUE;
    }
    return FALSE;
}
-(NSString *)getHistoryAddress:(ItemDetails *)itemDetail{
    NSMutableArray *array=[[NSMutableArray alloc]initWithObjects:[itemDetail addressLine1],[itemDetail addressLine2],[itemDetail city],[itemDetail state],[itemDetail zipCode], nil];
    [array removeObject:@""];
    return [[array valueForKey:kDescription] componentsJoinedByString:@","];
}
-(void)viewDidAppear:(BOOL)animated{
    [itemDetails setAddressLine1:textVeiw.text];
    [itemDetails setAddressLine2:@""];
    [itemDetails setCity:@""];
    [itemDetails setState:@""];
    [itemDetails setZipCode:@""];
    [[ItemDetailsShared sharedItemDetails] setSharedItemDetailsValue:itemDetails];
}
- (NSString *)getAddressFromItemDetail:(ItemDetails *)itemDetail{
    if (sharedAddress.length>0) {
        return sharedAddress;
    }else{
    NSMutableArray *array=[[NSMutableArray alloc]initWithObjects:[itemDetail addressLine1],[itemDetail addressLine2],[itemDetail city],[itemDetail state],[itemDetail zipCode], nil];
    [array removeObject:@""];
    [itemDetails setAddressLine1:[[array valueForKey:kDescription] componentsJoinedByString:@","]];
    [itemDetails setAddressLine2:@""];
    [itemDetails setCity:@""];
    [itemDetails setState:@""];
    [itemDetails setZipCode:@""];
    //[[ItemDetailsShared sharedItemDetails] setSharedItemDetailsValue:itemDetails];
    return [self getAddressArray:array];
    }
}
- (NSString *)getAddressPlacemark:(CLPlacemark *)placemark{

    NSMutableArray *array=[[NSMutableArray alloc]initWithObjects:[placemark thoroughfare],[placemark locality],[placemark administrativeArea],[placemark postalCode], nil];
    [array removeObject:@""];
    
    return [NSString stringWithFormat:@"%@ %@",[placemark subThoroughfare],[self getAddressArray:array]];
    
    
}
- (NSString *)getAddressArray:(NSArray *)addressArray{
    return [[addressArray valueForKey:kDescription] componentsJoinedByString:@","];
}
- (void)currentUserLocation:(CLLocation *)Location{
    AppDelegate *appdelegate=(AppDelegate *)[[UIApplication sharedApplication] delegate];
    appdelegate.lastLocation=Location;
    [[LocationManger sharedLocationManager]stopUpdatingLocation];
    CLGeocoder * geoCoder = [[CLGeocoder alloc] init];
    [geoCoder reverseGeocodeLocation:Location completionHandler:^(NSArray *placemarks, NSError *error) {
        for (CLPlacemark * placemark in placemarks)
        {
            appdelegate.localLocation=[NSString stringWithFormat:kLocalizaPatten,[placemark ISOcountryCode]];
            NSArray *addressArray= [NSArray arrayWithObjects:[placemark thoroughfare],[placemark locality],[placemark administrativeArea], nil];
            addressArray=[Utility removeNilArrayOfString:addressArray];
            NSString *addresStr=[addressArray componentsJoinedByString:@","];
            if ([placemark subThoroughfare]) {
                addresStr=[NSString stringWithFormat:@"%@ %@",[placemark subThoroughfare],addresStr];
            }
            [itemDetails setAddressLine1:addresStr];
            [itemDetails setAddressLine2:@""];
            [itemDetails setCity:@""];
            [itemDetails setState:@""];
            [itemDetails setZipCode:@""];
            [[ItemDetailsShared sharedItemDetails] setSharedItemDetailsValue:itemDetails];
            [_placeTableView reloadData];
            
        }
    }];
}

-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width, 58)];
    /* Create custom view to display section header... */
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(20, 30, tableView.frame.size.width, 18)];
    [label setTextColor:[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:1]];
    [label setFont:[UIFont fontWithName:kFont_Name size:18]];
    
    NSString *string =kPlace;
    /* Section header is in 0th index... */
    [label setText:string];
    [view addSubview:label];
    [view setBackgroundColor:[UIColor clearColor]]; //your background color...
    return view;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 58.0f;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return 2;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)
indexPath
{
    static NSString *simpleTableIdentifier= kAddressCell;
    static NSString *simpleTableIdentifier1= kDeliveryOptionsCell;
    UITableViewCell *cell;
    if (indexPath.row==0) {
        cell=(UITableViewCell *)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        UILabel *lable=(UILabel *)[cell viewWithTag:111];
        textVeiw=(UITextView *)[cell viewWithTag:998];
        textVeiw.delegate=self;
        textVeiw.text=[self getAddressFromItemDetail:itemDetails];
        [lable setText:[self getAddressFromItemDetail:itemDetails]];
    }
    else if (indexPath.row==1){
        cell=(UITableViewCell *)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier1];
        eateIn=(UISwitch *)[cell viewWithTag:11];
        [eateIn setOn:[self swichActionForString:itemDetails.dinnerDelivery withAction:kEat_In]];
        toGo=(UISwitch *)[cell viewWithTag:22];
        [toGo setOn:[self swichActionForString:itemDetails.dinnerDelivery withAction:kTo_Go]];

        
    }
    
    
    cell.selectionStyle =UITableViewCellSelectionStyleNone;
    
    
    return cell;
    
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row==0) {
        return 146;
    }
    return 250;
}
#pragma args switchAction
- (BOOL)swichActionForString:(NSString *)stringValue withAction:(NSString *)value{
    stringValue=[stringValue uppercaseString];
    value=[value uppercaseString];
    if ([stringValue rangeOfString:value].location == NSNotFound) {
        return NO;
    } else {
        return YES;
    }
    return NO;
}
- (NSString *)getSplNeedsString{
    NSMutableArray *splNeedsStrinArray=[[NSMutableArray alloc]init];
    if (toGo.isOn) {
        [splNeedsStrinArray addObject:kTo_Go];
    }
//    if (willDeliver.isOn) {
//        [splNeedsStrinArray addObject:@"Will Deliver"];
//    }
    if (eateIn.isOn) {
        [splNeedsStrinArray addObject:kEat_In];
    }
    return [splNeedsStrinArray componentsJoinedByString:@","];
}


- (IBAction)updateItem:(id)sender{
    itemDetails.dinnerDelivery=[self getSplNeedsString];
    //NSLog(@"%@",[self getSplNeedsString]);
    
    
}
- (IBAction)editButtonAction:(id)sender{
    UIButton *senderBtn=(UIButton *)sender;
    if ([senderBtn.titleLabel.text isEqualToString:kDone]) {
        
        [textVeiw setEditable:NO];
        [senderBtn setTitle:kEdit forState:UIControlStateNormal];
        [textVeiw resignFirstResponder];
    }
    else{
        [textVeiw setEditable:YES];
        [textVeiw becomeFirstResponder];
        [senderBtn setTitle:kDone forState:UIControlStateNormal];
    }
    
}
- (BOOL)textViewShouldEndEditing:(UITextView *)textView{
    [itemDetails setAddressLine1:textVeiw.text];
    [itemDetails setAddressLine2:@""];
    [itemDetails setCity:@""];
    [itemDetails setState:@""];
    [itemDetails setZipCode:@""];
    [[ItemDetailsShared sharedItemDetails] setSharedItemDetailsValue:itemDetails];
    return true;
}
@end
