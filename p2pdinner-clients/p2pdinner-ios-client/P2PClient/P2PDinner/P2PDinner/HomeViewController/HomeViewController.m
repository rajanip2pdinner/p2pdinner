//
//  HomeViewController.m
//  P2PDinner
//
//  Created by sudheerkumar on 2/25/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "HomeViewController.h"
#import "SellerHistoryHandler.h"
#import "LoginRequest.h"
#import "Utility.h"
#import "SharedLogin.h"
#import "ActivityView.h"
#import "DinnerLoginViewController.h"
#import "LocationManger.h"
#import "LocationServiceHandler.h"
#import "AppDelegate.h"
#import "AgreementsViewController.h"
@interface HomeViewController ()<LocationManagerDelegate>
{
    FacebookManager *fbmanager;
    ActivityView *activityView;
    LocationManger *locationMgr;
}
@end

@implementation HomeViewController
- (void)currentUserLocation:(CLLocation *)Location{
    AppDelegate *appdelegate=(AppDelegate *)[[UIApplication sharedApplication] delegate];
    appdelegate.lastLocation=Location;
    [[LocationManger sharedLocationManager]stopUpdatingLocation];
    CLGeocoder * geoCoder = [[CLGeocoder alloc] init];
    [geoCoder reverseGeocodeLocation:Location completionHandler:^(NSArray *placemarks, NSError *error) {
        for (CLPlacemark * placemark in placemarks)
        {
            appdelegate.localLocation=[NSString stringWithFormat:@"en_%@",[placemark ISOcountryCode]];
            //NSArray *addressArray= [NSArray arrayWithObjects:[placemark subThoroughfare],[placemark thoroughfare],[placemark locality],[placemark administrativeArea], nil];
            //Obtains the country code, that is the same whatever language you are working with (US, ES, IT ...)
            NSString *countryCode = [placemark ISOcountryCode];
            //Obtains a locale identifier. This will handle every language
            NSString *identifier = [NSLocale localeIdentifierFromComponents: [NSDictionary dictionaryWithObject: countryCode forKey: NSLocaleCountryCode]];
            //Obtains the country name from the locale BUT IN ENGLISH (you can set it as "en_UK" also)
            NSString *country =[ [[NSLocale alloc] initWithLocaleIdentifier:@"en_US"] displayNameForKey:NSLocaleLanguageCode value:identifier];
            
            
            NSLog(@"%@",country);
            //Continues your code
         
            
        }
    }];
    
    
}
- (void)displayLaunchScreen{
    LaunchScreen *launchScreen=[[UIStoryboard storyboardWithName:@"LaunchScreen" bundle:NULL] instantiateViewControllerWithIdentifier:@"LaunchScreenController"];
    [launchScreen showLaunchScreen];
}
- (void)settingsAction
{

    [self performSegueWithIdentifier:@"SettingsViewController" sender:self];
}
- (void)setUpSettingBarButton{
    UIImage* SettingsIcon = [UIImage imageNamed:@"SettingsIcon"];
    CGRect frameimg = CGRectMake(0, 0, SettingsIcon.size.width, SettingsIcon.size.height);
    UIButton *settingsButton = [[UIButton alloc] initWithFrame:frameimg];
    [settingsButton setBackgroundImage:SettingsIcon forState:UIControlStateNormal];
    [settingsButton addTarget:self action:@selector(settingsAction)
             forControlEvents:UIControlEventTouchUpInside];
    
    UIBarButtonItem *mailbutton =[[UIBarButtonItem alloc] initWithCustomView:settingsButton];
    self.navigationItem.rightBarButtonItem=mailbutton;
}
-(void)navigationBarsetup{
    UIColor *navBarColor=[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:1];
    NSDictionary *navbarTitleTextAttributes = [NSDictionary dictionaryWithObjectsAndKeys:
                                               [UIColor whiteColor],UITextAttributeTextColor,
                                               [UIFont fontWithName:@"Plantin" size:24], NSFontAttributeName,[NSValue valueWithUIOffset:UIOffsetMake(-1, 0)],UITextAttributeTextShadowOffset, nil];
    
    
    
    [self.navigationController.navigationBar  setTitleTextAttributes:navbarTitleTextAttributes];
    [self.navigationController.navigationBar setBarTintColor:navBarColor];
}
- (void)viewDidLoad {
//    locationMgr=[LocationManger sharedLocationManager];
//    [locationMgr updateLocation];
//     locationMgr.delegate=self;
    activityView=[[ActivityView alloc]initWithFrame:self.view.frame];
    [self setUpSettingBarButton];
    [self navigationBarsetup];
    [self displayLaunchScreen];
    
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(IBAction)tearmsAndCondition:(id)sender{
    [self performSegueWithIdentifier:@"AgreementViewController" sender:nil];
}

 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
     if ([[segue identifier] isEqualToString:@"AgreementViewController"])
     {
         AgreementsViewController *viewController=[segue destinationViewController];
         NSURL *targetURL = [NSURL URLWithString:@"legal/Terms.html"];
         [viewController setPdfURL:targetURL];
         [viewController setTitle:@"Terms & Conditions"];
     }
 }

#pragma UISetupDinnerView
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return 3;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell;
    
    cell= [tableView dequeueReusableCellWithIdentifier:@"menuCell"];
    UILabel *nameTitleValue=(UILabel *)[cell viewWithTag:101];
    UILabel *subTitleValue=(UILabel *)[cell viewWithTag:102];
    UIImageView *iconImage=(UIImageView *)[cell viewWithTag:100];
    
    
    if (cell) {
        if (indexPath.row==0)
        {
            nameTitleValue.text=@"I Want Dinner";
            subTitleValue.text=@"Find the perfect home made dinner!";
            [iconImage setImage:[UIImage imageNamed:@"IwantDinner"]];
        }
        else if (indexPath.row==1)
        {
            nameTitleValue.text=@"I Have Dinner";
            subTitleValue.text=@"Share your food for extra money!";
            [iconImage setImage:[UIImage imageNamed:@"IhaveDinner"]];
        }
        else if(indexPath.row==2)
        {
            nameTitleValue.text=@"My Orders";
            subTitleValue.text=@"View your current, previous orders!";
            [iconImage setImage:[UIImage imageNamed:@"MyOrders"]];
        }
    }
    
    
    
    return cell;
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
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {
    // This will create a "invisible" footer
    return 0.01f;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (indexPath.row==0) {
                [self performSegueWithIdentifier:@"wantDinner" sender:self];
                  }
    else if(indexPath.row==1){
        NSNumber *number=(NSNumber *)[[NSUserDefaults standardUserDefaults] objectForKey:@"userId"];
        if ([number integerValue]>0) {
            [[SharedLogin sharedLogin] setUserId:number];
            [self performSegueWithIdentifier:@"SuccessLoginOperation" sender:self];
        }else
        {
            UIStoryboard *mainStoryboard = [UIStoryboard storyboardWithName:@"Main"  bundle: nil];
            DinnerLoginViewController *dinnerLogin = (DinnerLoginViewController*)[mainStoryboard instantiateViewControllerWithIdentifier: @"DinnerLoginViewController"];
            [dinnerLogin getLoginResponse:^(NSError *error, NSString *emailId, BOOL successFull) {
                if (successFull) {
                     [self performSegueWithIdentifier:@"SuccessLoginOperation" sender:self];
                     [self removeLoginViewContrller];
                }
            }];
            [self.navigationController pushViewController:dinnerLogin animated:YES];
         }
            //[self performSegueWithIdentifier:@"haveDinner" sender:self];
    }
    else if(indexPath.row==2)
    {
        [self moveToMyOrderScreen];
    
    }
}
-(void)moveToMyOrderScreen{
    NSNumber *number=(NSNumber *)[[NSUserDefaults standardUserDefaults] objectForKey:@"userId"];
    if ([number integerValue]>0) {
        [[SharedLogin sharedLogin] setUserId:number];
        [self performSegueWithIdentifier:@"MyOrderViewController" sender:self];
    }else
    {
        UIStoryboard *mainStoryboard = [UIStoryboard storyboardWithName:@"Main"  bundle: nil];
        DinnerLoginViewController *dinnerLogin = (DinnerLoginViewController*)[mainStoryboard instantiateViewControllerWithIdentifier: @"DinnerLoginViewController"];
        [dinnerLogin getLoginResponse:^(NSError *error, NSString *emailId, BOOL successFull) {
            if (successFull) {
                [self performSegueWithIdentifier:@"MyOrderViewController" sender:self];
                [self removeLoginViewContrller];
            }
        }];
        [self.navigationController pushViewController:dinnerLogin animated:YES];
    }
}

@end
