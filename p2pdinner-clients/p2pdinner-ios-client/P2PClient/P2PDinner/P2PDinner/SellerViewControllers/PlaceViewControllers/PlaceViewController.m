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
@interface PlaceViewController()<LocationManagerDelegate,UITextViewDelegate>{
    LocationManger *locationMgr;
}
@end
@implementation PlaceViewController
@synthesize itemDetails,textVeiw;
- (void)viewDidLoad{
    [super viewDidLoad];
    locationMgr=[LocationManger sharedLocationManager];
    if (!([[self getAddressFromItemDetail:itemDetails] length]>0)) {
        [locationMgr updateLocation];
    }
    
    locationMgr.delegate=self;
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
    NSMutableArray *array=[[NSMutableArray alloc]initWithObjects:[itemDetail addressLine1],[itemDetail addressLine2],[itemDetail city],[itemDetail state],[itemDetail zipCode], nil];
    [array removeObject:@""];
    [itemDetails setAddressLine1:[[array valueForKey:@"description"] componentsJoinedByString:@","]];
    [itemDetails setAddressLine2:@""];
    [itemDetails setCity:@""];
    [itemDetails setState:@""];
    [itemDetails setZipCode:@""];
    //[[ItemDetailsShared sharedItemDetails] setSharedItemDetailsValue:itemDetails];
    return [self getAddressArray:array];
}
- (NSString *)getAddressPlacemark:(CLPlacemark *)placemark{
    
    NSMutableArray *array=[[NSMutableArray alloc]initWithObjects:[placemark subThoroughfare],[placemark thoroughfare],[placemark locality],[placemark administrativeArea],[placemark postalCode], nil];
    [array removeObject:@""];
    
    return [self getAddressArray:array];
    
    
}
- (NSString *)getAddressArray:(NSArray *)addressArray{
    return [[addressArray valueForKey:@"description"] componentsJoinedByString:@","];
}
- (void)currentUserLocation:(CLLocation *)Location{
    [[LocationManger sharedLocationManager]stopUpdatingLocation];
    CLGeocoder * geoCoder = [[CLGeocoder alloc] init];
    [geoCoder reverseGeocodeLocation:Location completionHandler:^(NSArray *placemarks, NSError *error) {
        for (CLPlacemark * placemark in placemarks)
        {
            NSArray *addressArray= [NSArray arrayWithObjects:[placemark subThoroughfare],[placemark thoroughfare],[placemark locality],[placemark administrativeArea], nil];
            addressArray=[Utility removeNilArrayOfString:addressArray];
            [itemDetails setAddressLine1:[addressArray componentsJoinedByString:@","]];
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
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(20, 10, tableView.frame.size.width, 18)];
    [label setTextColor:[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:1]];
    [label setFont:[UIFont fontWithName:@"Plantin" size:18]];
    
    NSString *string =@"Place";
    /* Section header is in 0th index... */
    [label setText:string];
    [view addSubview:label];
    [view setBackgroundColor:[UIColor clearColor]]; //your background color...
    return view;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return 2;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)
indexPath
{
    static NSString *simpleTableIdentifier= @"AddressCell";
    static NSString *simpleTableIdentifier1= @"DeliveryOptionsCell";
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
        [eateIn setOn:[self swichActionForString:itemDetails.dinnerDelivery withAction:@"Eat-In"]];
        toGo=(UISwitch *)[cell viewWithTag:22];
        [toGo setOn:[self swichActionForString:itemDetails.dinnerDelivery withAction:@"To-Go"]];
        willDeliver=(UISwitch *)[cell viewWithTag:33];
        [willDeliver setOn:[self swichActionForString:itemDetails.dinnerDelivery withAction:@"Will Deliver"]];
        
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
        [splNeedsStrinArray addObject:@"To-Go"];
    }
    if (willDeliver.isOn) {
        [splNeedsStrinArray addObject:@"Will Deliver"];
    }
    if (eateIn.isOn) {
        [splNeedsStrinArray addObject:@"Eat-In"];
    }
    return [splNeedsStrinArray componentsJoinedByString:@","];
}


- (IBAction)updateItem:(id)sender{
    itemDetails.dinnerDelivery=[self getSplNeedsString];
    //NSLog(@"%@",[self getSplNeedsString]);
    
    
}
- (IBAction)editButtonAction:(id)sender{
    UIButton *senderBtn=(UIButton *)sender;
    if ([senderBtn.titleLabel.text isEqualToString:@"Done"]) {
        
        [textVeiw setEditable:NO];
        [senderBtn setTitle:@"Edit" forState:UIControlStateNormal];
        [textVeiw resignFirstResponder];
    }
    else{
        [locationMgr updateLocation];
        [textVeiw setEditable:YES];
        [textVeiw becomeFirstResponder];
        [senderBtn setTitle:@"Done" forState:UIControlStateNormal];
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
