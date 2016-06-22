//
//  DinnerLoginViewController.m
//  P2PDinner
//
//  Created by sudheerkumar on 2/12/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "DinnerLoginViewController.h"
#import "FacebookManager.h"
#import "FBUserInfo.h"
#import "AppDelegate.h"
#import "Utility.h"
#import "LoginRequest.h"
#import "LoginResponce.h"
#import "LoginServiceHandler.h"
#import "SharedLogin.h"
#import  "ActivityView.h"
//#import "P2PServiceHandler.h"
#define Permission @[@"public_profile", @"email"]
@interface DinnerLoginViewController (){
    FacebookManager *fbmanager;
    ActivityView *activityView;
}
@end

@implementation DinnerLoginViewController
- (IBAction)facebookLoginButtonAction:(id)sender{
    [self showActivityView];
    // If the session state is any of the two "open" states when the button is clicked
    fbmanager=[FacebookManager sharedFacebookManager];
    /*
     if (FBSession.activeSession.state == FBSessionStateOpen
     || FBSession.activeSession.state == FBSessionStateOpenTokenExtended) {
     NSLog(@"Going to clear session");
     // Close the session and remove the access token from the cache
     // The session state handler (in the app delegate) will be called automatically
     #warning fbsession need to manage logout operation
     //[FBSession.activeSession closeAndClearTokenInformation];
     
     // If the session state is not any of the two "open" states when the button is clicked
     } else {
     // Open a session showing the user the login UI
     // You must ALWAYS ask for public_profile permissions when opening a session
     */
    [FBSession openActiveSessionWithReadPermissions:@[@"public_profile"]
                                       allowLoginUI:YES
                                  completionHandler:
     ^(FBSession *session, FBSessionState state, NSError *error) {
         
         // Call the app delegate's sessionStateChanged:state:error method to handle session state changes
         [fbmanager sessionStateChanged:session state:state error:error userInfoAction:^(FBUserInfo *albumsObj, NSError *error) {
             
             
             if (!error&&albumsObj) {
                 // NSLog(@"Called");
                 [self makeLoginOperation:albumsObj];
                 
                 
             }
         }];
         
         
     }];
    //    }
    
}
- (void)backAction{
    [[LoginServiceHandler sharedServiceHandler] cancelRequest];
    [self.navigationController popViewControllerAnimated:YES];
    
}
#warning need to save secure
#warning Ask them to send userId afterLogin
- (void)getLoginResponse:(LoginResponseBlock)loginResponse{
    _loginResponseBlock=loginResponse;
}
- (void)saveUserId:(NSNumber *)userId{
    NSUserDefaults *userDefaults=[NSUserDefaults standardUserDefaults];
    [userDefaults setObject:userId forKey:@"userId"];
    [userDefaults synchronize];
}
- (void)makeLoginOperation:(FBUserInfo *)userInfo{
    
    LoginRequest *loginRequest=[[LoginRequest alloc]init];

    [loginRequest setEmailAddress:[NSString stringWithFormat:@"%@",userInfo.uid]];
    [loginRequest setPassword:[[[FBSession activeSession] accessTokenData] accessToken]];
    [loginRequest setName:userInfo.name];
    
    [[LoginServiceHandler sharedServiceHandler] loginServiceHandler:loginRequest serviceCallBack:^(NSError *error, LoginResponce *response) {
        if (!error) {
            NSLog(@"%@ %@",response.status,[[SharedLogin sharedLogin] emailAddress]);
             if (_loginResponseBlock) {
                [self saveUserId:[[SharedLogin sharedLogin] userId]];
                _loginResponseBlock(nil,[[SharedLogin sharedLogin] emailAddress],TRUE);
            }
        }else{
            if (_loginResponseBlock) {
                _loginResponseBlock(error,@"",false);
            }
        }
        
    }];
}
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark - ActivityView
- (void)showActivityView{
    if (!activityView) {
        activityView=[[ActivityView alloc]initWithFrame:self.view.frame];
    }
    [activityView startAnimating:@"Loading..."];
    [self.view addSubview:activityView];
}
- (void)removeActivityView{
    if (activityView.isAnimating) {
        [activityView stopAnimating];
    }
    [activityView removeFromSuperview];
    
}
/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end
