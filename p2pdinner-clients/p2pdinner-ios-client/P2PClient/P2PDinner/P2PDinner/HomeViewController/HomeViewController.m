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
@interface HomeViewController ()
{
    FacebookManager *fbmanager;
    ActivityView *activityView;
}
@end

@implementation HomeViewController
- (void)displayLaunchScreen{
    LaunchScreen *launchScreen=[[UIStoryboard storyboardWithName:@"LaunchScreen" bundle:NULL] instantiateViewControllerWithIdentifier:@"LaunchScreenController"];
    [launchScreen showLaunchScreen];
}
- (void)settingsAction
{
    
    if (activityView.isAnimating) {
        [activityView stopAnimating];
        [activityView removeFromSuperview];
    }
    else
    {
        [activityView startAnimating:@"Loading..."];
        [self.view addSubview:activityView];
    }
    
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

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */
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
}
@end
